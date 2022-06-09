package br.com.cotecom.dtos.assembler

import br.com.cotecom.domain.cotacao.Cotacao
import br.com.cotecom.domain.cotacao.CotacaoFactory
import br.com.cotecom.domain.cotacao.EstadoCotacao
import br.com.cotecom.domain.cotacao.ICotacao
import br.com.cotecom.domain.cotacao.counter.AbstractCounter
import br.com.cotecom.domain.dto.cotacao.CotacaoDTO
import br.com.cotecom.domain.dto.cotacao.ItemCotacaoDTO
import br.com.cotecom.domain.dto.inbox.CotacaoItemInboxDTO
import br.com.cotecom.domain.dto.inbox.ItemInboxDTO
import br.com.cotecom.domain.dto.inbox.RespostaItemInboxDTO
import br.com.cotecom.domain.dto.tela.TelaCotacaoDTO
import br.com.cotecom.domain.item.ItemCotacao
import br.com.cotecom.domain.item.Produto
import br.com.cotecom.domain.resposta.IResposta
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.empresa.Cliente

public class CotacaoAssembler {

    static ICotacao crieCotacao(CotacaoDTO cotacaoDto) {
        ICotacao cotacao
        if (cotacaoDto.id == null) {
            cotacao = CotacaoFactory.crie(cotacaoDto.titulo, cotacaoDto.mensagem, cotacaoDto.dataEntrega,
                    cotacaoDto.dataValidade, cotacaoDto.prazoPagamento, Cliente.get(cotacaoDto?.empresaId),
                    EnderecoAssembler.crieEndereco(cotacaoDto.enderecoEntrega))
        } else {
            cotacao = Cotacao.get(cotacaoDto.id)
            cotacao.titulo = cotacaoDto.titulo
            cotacao.mensagem = cotacaoDto.mensagem
            cotacao.dataCriacao = cotacaoDto.dataCriacao
            cotacao.dataEntrega = cotacaoDto.dataEntrega
            cotacao.dataValidade = cotacaoDto.dataValidade
            cotacao.dataSalva = cotacaoDto.dataSalva
            cotacao.prazoPagamento = cotacaoDto.prazoPagamento
            cotacao.codigoEstadoCotacao = cotacaoDto.codigoEstadoCotacao
            cotacao.enderecoEntrega = EnderecoAssembler.crieEndereco(cotacaoDto.enderecoEntrega)
        }
        return cotacao
    }

    static CotacaoDTO crieCotacaoDTO(ICotacao cotacao) {
        CotacaoDTO cotacaoDTO = new CotacaoDTO(prazoPagamento: cotacao?.prazoPagamento, dataCriacao: cotacao?.dataCriacao,
                dataEntrega: cotacao?.dataEntrega, dataValidade: cotacao?.dataValidade, titulo: cotacao?.titulo,
                codigoEstadoCotacao: cotacao?.codigoEstadoCotacao, empresaId: cotacao?.empresa?.id,
                id: cotacao.id, version: cotacao.version, dataSalva: cotacao?.dataSalva, mensagem: cotacao?.mensagem)
        cotacaoDTO.enderecoEntrega = EnderecoAssembler.crieEnderecoDTO(cotacao?.enderecoEntrega)
        return cotacaoDTO
    }

    static ItemCotacaoDTO crieItemCotacaoDTO(ItemCotacao itemCotacao) {
        return new ItemCotacaoDTO(quantidade: itemCotacao.quantidade, cotacaoId: itemCotacao?.cotacao?.id,
                id: itemCotacao.id, produtoId: itemCotacao.produto.id, descricao: itemCotacao.produto.descricao?.toUpperCase(),
                categoria: itemCotacao.produto.categoria?.toUpperCase(), embalagem: itemCotacao.produto.embalagem.toString()?.toUpperCase(),
                qtdMaster: itemCotacao.produto.qtdMaster)
    }

    static List<ItemCotacaoDTO> crieItensCotacaoDTO(Set<ItemCotacao> itensCotacao) {
        return itensCotacao.collect {
            ItemCotacao item -> crieItemCotacaoDTO(item)
        }.sort {a, b -> a.descricao.compareToIgnoreCase b.descricao}
    }

    static ItemCotacao crieItemCotacao(ItemCotacaoDTO itemCotacaoDTO) {
        ItemCotacao itemCotacao
        if (itemCotacaoDTO.id) {
            itemCotacao = ItemCotacao.get(itemCotacaoDTO.id)
            itemCotacao.quantidade = itemCotacaoDTO.quantidade
        } else {
            itemCotacao = new ItemCotacao(quantidade: itemCotacaoDTO.quantidade,
                    produto: Produto.get(itemCotacaoDTO.produtoId))
        }
        return itemCotacao
    }

    static Set<ItemCotacao> crieItensCotacao(List<ItemCotacaoDTO> itensCotacaoDTO) {
        itensCotacaoDTO.collect {crieItemCotacao it} as Set
    }

    static CotacaoItemInboxDTO crieCotacaoItemInboxDTO(ICotacao cotacao) {
        ItemInboxDTO item = new CotacaoItemInboxDTO()
        item.id = cotacao.id
        item.titulo = cotacao.titulo
        item.dataSalva = cotacao.dataSalva
        item.dataCriacao = cotacao.dataCriacao
        setEstadoItemInbox(cotacao, item)
        item.respostas = new ArrayList<RespostaItemInboxDTO>()

        Set respostas = cotacao.respostas

        item.respostas = respostas.collect {IResposta resposta ->
            RespostaItemInboxDTO respostaDTO = new RespostaItemInboxDTO()
            respostaDTO.id = resposta.id
            respostaDTO.representante = resposta.representante.nome
            respostaDTO.empresa = resposta.representante.empresa?.nomeFantasia
            respostaDTO.status = resposta.codigoEstado
            respostaDTO.dataSalva = resposta.dataSalva
            return respostaDTO
        }
        return item
    }

    private static void setEstadoItemInbox(ICotacao cotacao, CotacaoItemInboxDTO cotacaoItemInboxDTO) {
        cotacaoItemInboxDTO.estado = EstadoCotacao.descricao.get(cotacao.codigoEstadoCotacao) as String
        try {
            AbstractCounter counter = cotacao.makeRespostasCounter()
            cotacaoItemInboxDTO.estadoCount = counter.recebido + "/" + (counter.recebido + counter.faltando)
        } catch (UnsupportedOperationException e) {
            cotacaoItemInboxDTO.estadoCount = ""
        }
    }

    static TelaCotacaoDTO crieTelaCotacaoDTO(ICotacao cotacao) {
        TelaCotacaoDTO telaCotacaoDTO = new TelaCotacaoDTO()
        telaCotacaoDTO.cotacaoDTO = CotacaoAssembler.crieCotacaoDTO(cotacao)
        telaCotacaoDTO.itensCotacaoDTO = CotacaoAssembler.crieItensCotacaoDTO(cotacao.itens)
        cotacao.respostas.each { Resposta resposta ->
            telaCotacaoDTO.representantesId.add(resposta.representante.id)
        }
        return telaCotacaoDTO
    }
}