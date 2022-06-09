package br.com.cotecom.services

import br.com.cotecom.domain.analise.Analise
import br.com.cotecom.domain.cotacao.Cotacao
import br.com.cotecom.domain.cotacao.EstadoCotacao
import br.com.cotecom.domain.cotacao.ICotacao
import br.com.cotecom.domain.item.ItemCotacao
import br.com.cotecom.domain.pedido.Pedido

import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.util.documents.excel.ExcelExport
import org.apache.log4j.Logger

import br.com.cotecom.util.documents.excel.ExcelFile
import br.com.cotecom.domain.cotacao.CotacaoFactory
import org.apache.poi.hssf.usermodel.HSSFRow
import br.com.cotecom.domain.item.Produto
import br.com.cotecom.domain.item.EmbalagemVenda
import br.com.cotecom.domain.usuarios.empresa.Endereco
import br.com.cotecom.domain.analise.ItemAnalise
import br.com.cotecom.domain.usuarios.empresa.Cliente


class CotacaoService {

    private static final log = Logger.getLogger(CotacaoService.class)

    boolean transactional = true
    def grailsApplication
    def notifierService
    def usuarioService
    def fileService
    def compraService

    def getCotacao(Long id) {
        return Cotacao.get(id)
    }

    def salve(ICotacao cotacao, flush = false) {
        if (cotacao.save(flush:true)) {
            return cotacao
        }
        return null
    }

    boolean cancele(Long cotacaoId) {
        ICotacao cotacao = Cotacao.get(cotacaoId)
        cotacao != null && cotacao.cancele()
    }

    boolean envieCotacao(ICotacao cotacao, List<Representante> representantes) {
        return cotacao.envie(representantes)
    }

    Analise getAnalise(Long id) {
        return Cotacao.get(id).analise
    }

    void analisar(Long id) {
        Cotacao.get(id).analisar()
    }

    Resposta getRespostaCotacao(def idItemResposta) {
        def sql = "from Resposta as resp join resp.itens as item where item.id = '${idItemResposta}'"
        def result = Resposta.executeQuery(sql).get(0)
        return result[0] as Resposta
    }

    String exporteAnaliseExcel(Long cotacaoId) {
        Cotacao cotacao = Cotacao.get(cotacaoId)
        String pathArquivo = cotacao.exporteExcelAnalise()
        if(fileService.convertFile(pathArquivo, "xls"))
            return pathArquivo
        return null
    }

    boolean possuiNovaResposta(Long idCotacao, int qtddRespostasConcluidas) {
        ICotacao cotacao = Cotacao.get(idCotacao);
        def respostasConcluidas = cotacao.respostas.findAll {it.concluida()}
        return respostasConcluidas.size() != qtddRespostasConcluidas
    }

    List<Pedido> gerePedidosPrimeiraOrdem(def idCotacao) {
        return Cotacao.get(idCotacao)?.gerePedidosPrimeiraOrdem()
    }

    String exportePedidoExcel(Long pedidoId) {
        def pedido = Pedido.get(pedidoId)
        def compra = compraService.getCompraByCotacaoId(pedido.resposta.cotacao.id)
        String pathArquivo = new ExcelExport().exportePedido(compra, pedido)
        if(fileService.convertFile(pathArquivo, "xls"))
            return pathArquivo
        return null
    }

    Boolean reenvieEmailCotacao(long respostaId) {
        def resposta = Resposta.get(respostaId)
        if (resposta) {
            notifierService.notifiqueNovaCotacao(resposta.cotacao.empresa.comprador.email, resposta.representante.nome,
                    resposta.representante.email, respostaId, resposta.cotacao.mensagem, resposta.cotacao.empresa.nomeFantasia)
            return true
        } else {
            return false
        }
    }

    Set<ItemCotacao> adicioneItensACotacao(Set<ItemCotacao> itensCotacao, Cotacao cotacao) {
        itensCotacao.each {
            it.cotacao = cotacao
            it.save(flush:true)
        }
        itensCotacao
    }

    ItemCotacao removaItemDaCotacao(ItemCotacao itemCotacao) {
        Cotacao cotacao = Cotacao.get(itemCotacao.cotacao?.id)
        cotacao.removeFromItens itemCotacao
        cotacao.save()
        itemCotacao.delete()
        if (!cotacao.itens.contains(itemCotacao))
            return itemCotacao
        return null
    }

    boolean finalizeCotacao(Cotacao cotacao) {
        cotacao.finalize()
        return cotacao.codigoEstadoCotacao == EstadoCotacao.FINALIZADA
    }

    def crieCotacaRascunhoAPartirDeExcel(ExcelFile excelFile, Cliente empresa, int linhasCabecalho) {
        def endereco = new Endereco(empresa.endereco?.properties)?.save()
        ICotacao cotacao = CotacaoFactory.crie("Cotação", "", new Date() + 1, new Date(), "35 dias", empresa,
                endereco, true)
        cotacao.dataSalva = new Date()
        cotacao.save()
        def produtosNaoAdicionados = []
        Iterator linhas = excelFile.getRowIteratorFromSheet(0)
        linhasCabecalho.times {
            linhas.next() as HSSFRow //pulando cabeçalho
        }
        while (linhas.hasNext()) {
            HSSFRow linha = (HSSFRow) linhas.next()
            def qtd = excelFile.getCellValue(0, 0, linha.getRowNum())
            String descricao = excelFile.getCellValue(0, 1, linha.getRowNum())?.replaceAll(/\s+/, " ")?.replaceAll("\\u00a0"," ")
            if(descricao == null || descricao == " ")
                print("erro $descricao" + "linha $linha")
            String embalagem = excelFile.getCellValue(0, 2, linha.getRowNum())

            Produto produto
            //Expressao regular em groovy para validar o formato da embalagem -> CX/0012/UN
            def p = "[a-zA-Z]{2}/[0-9][0-9]?[0-9]?[0-9]?/[a-zA-Z]{2}[\\s]*"
            if (qtd instanceof Number && descricao instanceof String && embalagem instanceof String && embalagem ==~ p) {
                String query = "+descricao: $descricao +qtdeDeUnidadesNaEmbalagemDeVenda: ${embalagem.subSequence(3,7)} "+
                    "+embVendadescricao: ${embalagem.subSequence(0,2)} +embUnidadedescricao: ${embalagem.subSequence(8,10)}"
                List<Produto> produtos = Produto.searchEvery(query)
                if(!produtos || produtos.size() < 1){
                    produto = new Produto(descricao: descricao, embalagem: EmbalagemVenda.setEmbalagem(embalagem),
                            dataDelecao: new Date(), empresaId: empresa.id)
                    produto.save(flush:true)
                    if (produto == null || produto.hasErrors()) {
                        produto.errors.each {
                            print "\n ${it}"
                        }
                    }
                }else{
                    produto = Produto.get(produtos.get(0).id)
                }

                ItemCotacao itemCotacao = new ItemCotacao(quantidade: qtd as Integer, produto: produto)
                cotacao.addToItens(itemCotacao)
            }
            else{
                produtosNaoAdicionados << qtd + " " + descricao + " " + embalagem + "\n"
            }
        }
        if(cotacao.itens.size() > 0 && salve(cotacao) instanceof Cotacao){
            return [cotacao, produtosNaoAdicionados]
        } else {
            return null
        }
    }

    List<ItemAnalise> saveItensAnalise(List<ItemAnalise> itensAnalise) {
        itensAnalise.collect {
            it.save(flush:true)
        }
    }
}

