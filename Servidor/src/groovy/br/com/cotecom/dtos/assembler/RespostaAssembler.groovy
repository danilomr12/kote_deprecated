package br.com.cotecom.dtos.assembler

import br.com.cotecom.domain.dto.analise.AnaliseRespostaDTO
import br.com.cotecom.domain.dto.cotacao.CotacaoDTO
import br.com.cotecom.domain.dto.resposta.ItemRespostaDTO
import br.com.cotecom.domain.dto.resposta.RespostaDTO
import br.com.cotecom.domain.dto.tela.TelaRespostaRepresentanteDTO
import br.com.cotecom.domain.item.ItemResposta
import br.com.cotecom.domain.item.Preco
import br.com.cotecom.domain.resposta.IResposta
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.domain.dto.usuario.UsuarioDTO
import br.com.analise.domain.RespostaCompra
import br.com.cotecom.domain.dto.pedido.PedidoDTO
import br.com.cotecom.domain.pedido.Pedido

public static class RespostaAssembler {

    static AnaliseRespostaDTO crieAnaliseRespostaDTO(RespostaCompra resposta) {
        AnaliseRespostaDTO analiseResposta = new AnaliseRespostaDTO()
        analiseResposta.id = resposta.idResposta
        analiseResposta.representanteId = resposta.idRepresentante
        analiseResposta.nomeRepresentante = resposta.nomeRepresentante
        analiseResposta.empresaNomeFantasia = resposta.nomeFantasiaEmpresa
        analiseResposta.estado = resposta.estadoResposta
        return analiseResposta
    }

    static RespostaDTO crieRespostaDTO(IResposta resposta) {
        if(!resposta)
            return null
        return new RespostaDTO( id:resposta.id, dataCriacao: resposta.dataCriacao, dataSalva: resposta.dataSalva,
                dataValidade: resposta.cotacao.dataValidade, status: resposta.codigoEstado ,
                cotacaoId: resposta.cotacao.id, representanteId: resposta.representante.id,
                clienteNomeFantasia: resposta?.cotacao?.empresa?.nomeFantasia,
                clienteRazaoSocial: resposta?.cotacao?.empresa?.razaoSocial,
                pedidosDTOs: PedidoAssembler.criePedidosDTO(resposta.pedidos.collect {Pedido.get(it.id)}))
    }



    static IResposta crieResposta(RespostaDTO respostaDTO) {
        if(!respostaDTO || !respostaDTO.id)
            return null
        IResposta resposta = Resposta.get(respostaDTO.id)
        return resposta
    }

    static ItemRespostaDTO crieItemRespostaDTO(ItemResposta itemResposta) {
        if(!itemResposta.preco){
            return new ItemRespostaDTO(id:itemResposta.id, precoEmbalagem: null,
                    produtoId: itemResposta.itemCotacao.produto.id,
                    descricao: itemResposta.itemCotacao.produto?.descricao,
                    embalagem: itemResposta.itemCotacao.produto?.embalagem?.toString(),
                    categoria: itemResposta.itemCotacao.produto?.categoria,                    
                    quantidade: itemResposta.itemCotacao.quantidade,
                    observacao: itemResposta.observacao,
                    respostaId: itemResposta.resposta.id,
                    qtdEmbalagem: itemResposta.itemCotacao.produto.embalagem.qtdeDeUnidadesNaEmbalagemDeVenda,
                    produtoAlternativo: ProdutoAssembler.crieProdutoDTO(itemResposta?.produtoAlternativo),
                    saved: true)
        }
        return new ItemRespostaDTO(id:itemResposta.id, precoEmbalagem: itemResposta.preco.embalagem,
                produtoId: itemResposta.itemCotacao.produto.id,
                descricao: itemResposta.itemCotacao.produto?.descricao,
                embalagem: itemResposta.itemCotacao.produto?.embalagem?.toString(),
                categoria: itemResposta.itemCotacao.produto?.categoria,
                quantidade: itemResposta.itemCotacao.quantidade,
                observacao: itemResposta.observacao,
                respostaId: itemResposta.resposta.id,
                qtdEmbalagem: itemResposta.itemCotacao.produto.embalagem.qtdeDeUnidadesNaEmbalagemDeVenda,
                produtoAlternativo: ProdutoAssembler.crieProdutoDTO(itemResposta?.produtoAlternativo),
                saved: true)
    }

    static List<ItemRespostaDTO> crieItensRespostaDTO(Set<ItemResposta> itens) {
        if(!itens) return null
        itens.collect { crieItemRespostaDTO(it)}.sort{a, b -> a.descricao.compareToIgnoreCase b.descricao}
    }

    static TelaRespostaRepresentanteDTO crieTelaRespostaDTO(Resposta resposta) {
        List<ItemRespostaDTO> itens = RespostaAssembler.crieItensRespostaDTO(resposta.itens)
        RespostaDTO respostaDTO = RespostaAssembler.crieRespostaDTO(resposta)
        UsuarioDTO representanteDTO = UsuarioAssembler.crieUsuarioDTO(resposta.representante)
        UsuarioDTO compradorDTO = UsuarioAssembler.crieUsuarioDTO(resposta.cotacao.empresa.comprador)
        CotacaoDTO cotacaoDTO = CotacaoAssembler.crieCotacaoDTO(resposta.cotacao)

        return new TelaRespostaRepresentanteDTO(itensResposta: itens, resposta: respostaDTO,
                representante: representanteDTO, comprador: compradorDTO, cotacao: cotacaoDTO)

    }

    static ItemResposta crieItemResposta(ItemRespostaDTO itemRespostaDTO) {
        ItemResposta itemResposta = ItemResposta.get(itemRespostaDTO.id)
        if(itemResposta){
            itemResposta.preco = new Preco(embalagem: itemRespostaDTO.precoEmbalagem)
            itemResposta?.produtoAlternativo = ProdutoAssembler.crieProduto(itemRespostaDTO?.produtoAlternativo)
            itemResposta.observacao = itemRespostaDTO.observacao
        }
        return itemResposta
    }

}