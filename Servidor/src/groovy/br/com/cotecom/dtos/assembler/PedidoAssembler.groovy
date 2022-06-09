package br.com.cotecom.dtos.assembler

import br.com.cotecom.domain.dto.pedido.PedidoDTO
import br.com.cotecom.domain.pedido.Pedido
import br.com.cotecom.domain.dto.tela.TelaPedidoDTO
import br.com.cotecom.domain.dto.pedido.ItemPedidoDTO
import br.com.cotecom.domain.item.ItemPedido
import br.com.analise.domain.Compra
import br.com.analise.domain.Item

static class PedidoAssembler {
    
    static List<PedidoDTO> criePedidosDTO(List<Pedido> pedidos){
        return pedidos.collect {criePedidoDTO(it)}
    }

    static PedidoDTO criePedidoDTO(Pedido pedido) {
        return new PedidoDTO(id: pedido.id,respostaId: pedido.resposta.id, cotacaoId: pedido.resposta.cotacao.id,
                totalItens: pedido.itens.size(), totalPedido: pedido.valorTotalPedido(),
                representanteId: pedido.resposta.representante.id, dataCriacao: pedido.dataCriacao, faturado: pedido.faturado)
    }


    static TelaPedidoDTO crieTelaPedidoDTO(Pedido pedido, Compra compra) {
        TelaPedidoDTO telaPedidoDTO = new TelaPedidoDTO()
        telaPedidoDTO.pedido = criePedidoDTO(pedido)
        telaPedidoDTO.itensPedido = crieItensPedidoDTO(compra, pedido)
        telaPedidoDTO.comprador = UsuarioAssembler.crieUsuarioDTO(pedido.resposta.cotacao.empresa.comprador)
        return telaPedidoDTO
    }

    static List<ItemPedidoDTO> crieItensPedidoDTO(Compra compra, Pedido pedido) {
        pedido.itens.sort {it.itemResposta.itemCotacao.produto.descricao}.collect {ItemPedido itemPedido->
            def item = compra.itens.find {it.idProduto == itemPedido.itemResposta.itemCotacao.produto.id}
            new ItemPedidoDTO(
                    id: itemPedido.id,
                    qtdPedida: itemPedido.qtdPedida,
                    descricao: item.descricao,
                    produtoId: item.idProduto,
                    precoEmbalagem: item?.respostas?.get(0)?.preco,
                    embalagem: item.embalagem,
                    obs: item?.respostas?.get(0)?.observacao
            )
        }
    }
}
