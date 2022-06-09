package br.com.cotecom.domain.cotacao

import br.com.analise.domain.Compra
import br.com.analise.domain.Item
import br.com.analise.service.ICompraService
import br.com.cotecom.domain.item.ItemCotacao
import br.com.cotecom.domain.item.ItemResposta
import br.com.cotecom.domain.resposta.EstadoResposta
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.perf4j.LoggingStopWatch

public class AnaliseFactory {

    private static final log = Logger.getLogger(AnaliseFactory.class)

    def ctx = ApplicationHolder.application.mainContext
    def sessionFactory = ctx.sessionFactory
    def compraService = (ICompraService)ctx.getBean("compraService")

    public void crie(Long cotacaoId) {
        if(!cotacaoId) return
        log.debug("criando análise de cotação - id: ${cotacaoId}")
        Cotacao cotacao = Cotacao.get(cotacaoId)
        def comprador = cotacao.empresa.comprador

        Compra compra = new Compra(titulo: cotacao.titulo, mensagem: cotacao.mensagem, dataCriacao: cotacao.dataCriacao,
                dataEntrega: cotacao.dataEntrega,dataValidade: cotacao.dataValidade, dataSalva: cotacao.dataSalva,
                prazoPagamento: cotacao.prazoPagamento,estado: cotacao.codigoEstadoCotacao, endereco: cotacao.enderecoEntrega.toString(),
                compradorId: comprador.id, empresaId: cotacao.empresa.id, idCotacao: cotacao.id)

        def stopwatch = new LoggingStopWatch()

        def itens = []
        cotacao.itens.eachWithIndex {ItemCotacao itemCotacao, int i ->
            def produto = itemCotacao.produto
            itens << new Item(descricao: produto.descricao, embalagem: produto.embalagem.toString(), idProduto: produto.id,
                    categoria: produto.categoria, codigoBarras: produto.barCode, fabricante: produto.fabricante,
                    marca: produto.marca, peso: produto.peso, quantidade: itemCotacao.quantidade, naoComprar: false,
                    idItemCompra: itemCotacao.id)
        }
        stopwatch.stop("tempo criacao itens de analise (itens compra)")

        compra.itens = itens
        compra = compraService.saveCompra(compra)
        stopwatch.stop("Tempo para salvar compra com itens salvos via rmi midleware")
        sessionFactory.currentSession.flush()
        log.debug("Compra criada com sucesso - id: ${compra.id}")
    }

    public void crieItensRespostaCompra(Long idResposta) {
        log.debug("Inciando criação de itens de resposta da compra a partir da resposta - id: ${idResposta}")
        br.com.cotecom.domain.resposta.Resposta sessionResposta = br.com.cotecom.domain.resposta.Resposta.get(idResposta)
        Compra compra = compraService.getCompraByCotacaoId(sessionResposta.cotacao.id)

        def respostaCompra = compra?.respostasCompra?.find {it.idResposta == idResposta}

        sessionResposta.itens.eachWithIndex {ItemResposta itemResposta, int i ->
            if(itemResposta?.preco?.embalagem > 0){
                br.com.analise.domain.Resposta resposta = new br.com.analise.domain.Resposta()
                resposta.respostaCompra = respostaCompra
                resposta.idItemResposta = itemResposta.id
                resposta.observacao = itemResposta.observacao
                resposta.preco = itemResposta?.preco?.embalagem

                Item item = compra.itens.find { it.idItemCompra == itemResposta.itemCotacao.id }
                if(item.respostas == null)
                    item.respostas = []
                //todo: testar isso -> item.respostas.removeAll(item.respostas.findAll{it.respostaCompra.id == respostaCompra.id})
                item.respostas.removeAll {resposta.respostaCompra.id == it.respostaCompra.id}

                item.respostas.add(resposta)
            }
        }

        compra.itens.each {Item item ->
            item.respostas?.sort {br.com.analise.domain.Resposta first, br.com.analise.domain.Resposta sec ->
                if (first.preco == null || first.preco == 0)
                    return 1
                if (sec.preco == null || sec.preco == 0)
                    return -1
                return first.preco <=> sec.preco
            }
        }

        sessionResposta.mudeEstadoPara(EstadoResposta.AGUARDANDO_OUTRAS_RESPOSTAS)
        sessionResposta.save()
        sessionFactory.currentSession.flush()
        compraService.updateItens(compra.itens)
        compraService.updateRespostaCompra(sessionResposta.id, EstadoResposta.AGUARDANDO_OUTRAS_RESPOSTAS)
        log.debug("Teminado criação de itens da resposta - id: ${idResposta}")
    }

}
