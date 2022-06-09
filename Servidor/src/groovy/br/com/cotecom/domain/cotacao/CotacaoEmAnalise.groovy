
package br.com.cotecom.domain.cotacao

import br.com.analise.domain.Compra
import br.com.analise.domain.Item
import br.com.analise.domain.RespostaCompra
import br.com.analise.service.ICompraService
import br.com.cotecom.domain.item.ItemPedido
import br.com.cotecom.domain.item.ItemResposta
import br.com.cotecom.domain.pedido.Pedido
import br.com.cotecom.domain.resposta.EstadoResposta
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.domain.serviceinterfaces.INotifierService
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.empresa.Cliente
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.springframework.context.ApplicationContext

public class CotacaoEmAnalise extends EstadoCotacao {


    ApplicationContext ctx = (ApplicationContext)ApplicationHolder.getApplication().getMainContext();
    INotifierService notifierService = (INotifierService) ctx.getBean("notifierService");
    def compraService = (ICompraService)ctx.getBean("compraService")
    private static final int MAIL_SEND_RATE_PER_SECOND = 100
    def backgroundService = ctx.backgroundService
    def sessionFactory = ctx.sessionFactory


    public void analisar(ICotacao cotacao) {
        compraService.analiseCompra(cotacao.id)
    }

    public List<Pedido> gerePedidosPrimeiraOrdem(ICotacao cotacao){

        Map<Long,Pedido> pedidos = new HashMap<Long ,Pedido>()
        Compra compra = compraService.getCompraByCotacaoId(cotacao.id)

        compra.respostasCompra.each {RespostaCompra respostaCompra ->
            if(respostaCompra.estadoResposta != EstadoResposta.RECUSADA && respostaCompra.estadoResposta!= EstadoResposta.CANCELADA
                    && respostaCompra.estadoResposta!= EstadoResposta.PERDIDA) {
                def resposta = cotacao.respostas.find { it.id == respostaCompra.idResposta } as Resposta
                def pedido = Pedido.findByResposta(resposta)
                if (!pedido) {
                    def itensComMenorPreco = compra.itens.findAll {
                        it?.respostas?.size() > 0 &&
                                it.respostas?.get(0)?.respostaCompra?.idResposta == respostaCompra?.idResposta
                    }
                    if (itensComMenorPreco.size() > 0) {
                        pedido = new Pedido()
                        pedidos.put(respostaCompra.idResposta, pedido)
                        itensComMenorPreco.each { Item item ->
                            if (item.respostas.get(0).preco > 0 && !item.naoComprar) {
                                pedido.adicioneItem(
                                        new ItemPedido(itemResposta: ItemResposta.get(item.respostas.get(0).idItemResposta),
                                                qtdPedida: item.quantidade, precoEmbalagem: item.respostas.get(0).preco)
                                )
                            }
                        }
                    }
                    if (pedido) {
                        pedido.save()
                        resposta.addToPedidos pedido
                        resposta.save()
                        pedido.resposta = resposta
                        pedido.save(flush: true)
                    }
                    compraService.updateRespostaCompra(resposta.id, EstadoResposta.RESPONDA_PEDIDO_PENDENTE)
                    resposta.mudeEstadoPara(EstadoResposta.RESPONDA_PEDIDO_PENDENTE)
                    resposta.save()
                } else {
                    pedidos.put(respostaCompra.idResposta, pedido)
                }
            }
        }

        Cotacao.executeUpdate("update Cotacao b set b.codigoEstadoCotacao='${EstadoCotacao.AGUARDANDO_PEDIDOS}'" +
                "where b.id='${compra.idCotacao}'")
        compraService.updateEstadoCompraByCotacaoId(compra.idCotacao, EstadoCotacao.AGUARDANDO_PEDIDOS)
        def comprador = Comprador.read(compra.compradorId)
        def cliente = Cliente.read(compra.empresaId)
        def nomeComprador = comprador.nome
        def emailComprador = comprador.email
        def clienteNomeFantasia = cliente.nomeFantasia
        def idComprador = comprador.id
        Thread.start {
            if(pedidos!=null && pedidos.values() != null){
                pedidos.each {Long respostaId, Pedido pedido ->
                    Thread.currentThread().sleep(MAIL_SEND_RATE_PER_SECOND)
                    RespostaCompra respostaCompra = compra.respostasCompra.find{it.idResposta == respostaId}
                    notifierService.notifiquePedidoParaRepresentante(pedido, respostaCompra, nomeComprador, emailComprador, idComprador, clienteNomeFantasia)
                }

            }
        }
        return pedidos*.value
    }

    /*public List<Pedido> gerePedidosPrimeiraOrdemOld(ICotacao cotacao){
            Map<Long,Pedido> pedidos = new HashMap<Long ,Pedido>()
            Compra compra = compraService.getCompraByCotacaoId(cotacao.id)

            compra.itens.each {Item itemCompra ->
                if(!itemCompra.naoComprar)
                    adicioneAoPedidoCorrespondente(pedidos, itemCompra.respostas.get(0), itemCompra)
            }
            cotacao.respostas.each {Resposta resposta ->
                def pedido = pedidos.get(resposta.id)
                if(pedido){
                    pedido.resposta = resposta
                    resposta.addToPedidos pedido
                    pedido.save(flush:true)
                    resposta.save(flush:true)
                }
            }
            List<Pedido> pedidosList
            pedidosList = pedidos*.value
            if(pedidos!=null){
                cotacao.mudeEstadoPara(EstadoCotacao.PRONTA_PARA_ENVIO_PEDIDOS)
            }
            return pedidosList
        }

        private void adicioneAoPedidoCorrespondente(Map<Long, Pedido> pedidos, Resposta respostaItem, Item item){
            if(respostaItem.preco !=null && respostaItem.preco > 0.0){
                Pedido pedido = pedidos.get(respostaItem.respostaCompra.idResposta)
                if(pedido){
                    pedido.adicioneItem(
                            new ItemPedido(
                                    itemResposta: ItemResposta.get(respostaItem.idItemResposta),
                                    qtdPedida: item.qtdPedida, precoEmbalagem: respostaItem.preco
                            )
                    )
                }else {
                    pedido = new Pedido()
                    pedido.adicioneItem(new ItemPedido(itemResposta: ItemResposta.get(respostaItem.idItemResposta)))
                    pedidos.put(respostaItem.respostaCompra.idResposta, pedido)
                }
            }
        }
    */
    public boolean salve(ICotacao cotacao) {
        if(cotacao.save())
            return true
        return false
    }

}