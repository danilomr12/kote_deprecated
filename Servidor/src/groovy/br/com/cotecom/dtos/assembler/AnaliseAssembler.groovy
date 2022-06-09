package br.com.cotecom.dtos.assembler

import br.com.cotecom.domain.analise.Analise
import br.com.cotecom.domain.analise.ItemAnalise
import br.com.cotecom.domain.analise.ItemRespostaFact
import br.com.cotecom.domain.cotacao.EstadoCotacao
import br.com.cotecom.domain.dto.analise.AnaliseCotacaoDTO
import br.com.cotecom.domain.dto.analise.ItemAnaliseCotacaoDTO
import br.com.cotecom.domain.dto.analise.ItemAnaliseRespostaDTO
import br.com.analise.domain.Compra
import br.com.analise.domain.Item

import br.com.analise.domain.Resposta
import br.com.cotecom.domain.dto.cotacao.CotacaoDTO
import br.com.analise.domain.RespostaCompra

public class AnaliseAssembler {

    static AnaliseCotacaoDTO crieAnaliseCotacaoDTO(Compra compra){
        if(!compra)
            return null
        AnaliseCotacaoDTO analiseCotacaoDTO = new AnaliseCotacaoDTO()
        analiseCotacaoDTO.id = compra.getId()
        analiseCotacaoDTO.idCotacao = compra.idCotacao
        //criando cotacaoDTO
        analiseCotacaoDTO.cotacao = new CotacaoDTO()
        analiseCotacaoDTO.cotacao.compradorId = compra.compradorId
        analiseCotacaoDTO.cotacao.codigoEstadoCotacao = compra.estado
        analiseCotacaoDTO.cotacao.id = compra.idCotacao
        analiseCotacaoDTO.cotacao.prazoPagamento = compra.prazoPagamento
        analiseCotacaoDTO.cotacao.titulo = compra.titulo
        analiseCotacaoDTO.cotacao.dataCriacao = compra.dataCriacao
        analiseCotacaoDTO.cotacao.dataEntrega = compra.dataEntrega
        analiseCotacaoDTO.cotacao.dataSalva = compra.dataSalva
        analiseCotacaoDTO.cotacao.dataValidade = compra.dataValidade
        analiseCotacaoDTO.cotacao.empresaId = compra.empresaId

        analiseCotacaoDTO.editavel = compra.estado == EstadoCotacao.EM_ANALISE

        analiseCotacaoDTO.respostas = []
        analiseCotacaoDTO.respostas = compra.respostasCompra.collect {
            RespostaAssembler.crieAnaliseRespostaDTO(it)
        }

        analiseCotacaoDTO.itensAnaliseCotacao = crieItensAnaliseCotacaoDTO(compra.itens)
                .sort{a, b -> a.descProduto.compareToIgnoreCase b.descProduto}
        analiseCotacaoDTO
    }

    static List<ItemAnaliseCotacaoDTO> crieItensAnaliseCotacaoDTO(List<Item> itens) {
        itens.collect {
            crieItemAnaliseCotacaoDTO(it)
        }
    }

    static ItemAnaliseCotacaoDTO crieItemAnaliseCotacaoDTO(Item item){
        def itemDto = new ItemAnaliseCotacaoDTO()
        itemDto.id = item.id
        itemDto.descProduto = item.descricao
        itemDto.quantidade = item.quantidade
        itemDto.naoComprar = item.naoComprar ? item.naoComprar : false
        itemDto.embalagem = item.embalagem
        itemDto.respostas = item.respostas.collect {crieItemAnaliseRespostaDTO(it, item.embalagem)}
        itemDto.saved = true
        itemDto
    }

    static ItemAnaliseRespostaDTO crieItemAnaliseRespostaDTO(Resposta resposta, String embalagem){
        def itemRespostaDTO = new ItemAnaliseRespostaDTO()
        itemRespostaDTO.id = resposta.idItemResposta
        itemRespostaDTO.idRepresentante = resposta.respostaCompra.idRepresentante
        itemRespostaDTO.descricaoRepresentante = resposta.respostaCompra.nomeRepresentante
        itemRespostaDTO.precoAtribuido = resposta.preco
        itemRespostaDTO.observacao = resposta.observacao
        itemRespostaDTO.embalagem = embalagem
        itemRespostaDTO.idRespostaCompra = resposta.respostaCompra.id
        return itemRespostaDTO
    }

    static List<Item> crieItensAAtualizar(List<ItemAnaliseCotacaoDTO> itemAnaliseCotacaoDTOs) {
        itemAnaliseCotacaoDTOs.collect {ItemAnaliseCotacaoDTO itemAnaliseCotacaoDTO ->
            new Item(id: itemAnaliseCotacaoDTO.id, naoComprar: itemAnaliseCotacaoDTO.naoComprar, quantidade: itemAnaliseCotacaoDTO.quantidade,
                    respostas: crieRespostasAtualizePreco(itemAnaliseCotacaoDTO.respostas))
        }
    }

    static List<Resposta> crieRespostasAtualizePreco(List<ItemAnaliseRespostaDTO> itemAnaliseRespostaDTOs) {
        itemAnaliseRespostaDTOs.collect {
            new Resposta(preco: it.precoAtribuido, idItemResposta: it.id, respostaCompra: new RespostaCompra(id: it.idRespostaCompra))
        }
    }

    //todo: metodos antigos, apagar os que não são necessarios

    static AnaliseCotacaoDTO crieAnaliseCotacaoDTO(Analise analiseDomain){
        AnaliseCotacaoDTO analiseDto = new AnaliseCotacaoDTO();
        analiseDto.id = analiseDomain.id.toString();
        analiseDto.idCotacao = analiseDomain.cotacao.id;
        analiseDto.version = analiseDomain.version;
        analiseDto.cotacao = CotacaoAssembler.crieCotacaoDTO(analiseDomain.cotacao);
        analiseDto.editavel = analiseDomain.cotacao.codigoEstadoCotacao == EstadoCotacao.EM_ANALISE;

        if(analiseDto.respostas == null)
            analiseDto.respostas = new ArrayList();

        analiseDto.respostas = analiseDomain.cotacao.respostas.collect { RespostaAssembler.crieAnaliseRespostaDTO(it) }

        if(analiseDto.itensAnaliseCotacao == null)
            analiseDto.itensAnaliseCotacao = new ArrayList();

        analiseDto.itensAnaliseCotacao = analiseDomain.itens.collect {
            crieItemAnaliseCotacaoDTO(it)
        }.sort{a, b -> a.descProduto.compareToIgnoreCase b.descProduto}

        return analiseDto;
    }

    static ItemAnaliseCotacaoDTO crieItemAnaliseCotacaoDTO(ItemAnalise itemAnalise){
        def itemDto = new ItemAnaliseCotacaoDTO()
        itemDto.id = itemAnalise.id
        itemAnalise.itensRespostaFact.removeAll {it==null}
        List<ItemRespostaFact> itensRespondidos = itemAnalise.itensRespostaFact.findAll{
            it.precoEmbalagem != null
        } as List<ItemRespostaFact>


        if(itemDto.respostas == null)
            itemDto.respostas = new ArrayList()
        itemDto.respostas = itensRespondidos.collect {crieItemAnaliseRespostaDTO(it, itemAnalise)}

        itemDto.descProduto = itemAnalise.descricao?.toUpperCase()
        itemDto.quantidade = itemAnalise.quantidade
        itemDto.naoComprar = itemAnalise.naoComprar ? itemAnalise.naoComprar : false
        itemDto.embalagem = itemAnalise.embalagem?.toUpperCase();
        itemDto.saved = true;

        return itemDto
    }

    static ItemAnaliseRespostaDTO crieItemAnaliseRespostaDTO(ItemRespostaFact itemAnaliseResposta, ItemAnalise itemAnalise){
        ItemAnaliseRespostaDTO itemRespostaDTO = new ItemAnaliseRespostaDTO()

        itemRespostaDTO.id = itemAnaliseResposta.id
        itemRespostaDTO.idRepresentante = itemAnaliseResposta.representanteId
        itemRespostaDTO.descricaoRepresentante = itemAnaliseResposta.representante.nome
        itemRespostaDTO.precoAtribuido = itemAnaliseResposta.precoEmbalagem
        itemRespostaDTO.observacao = itemAnaliseResposta.observacao
        itemRespostaDTO.embalagem = itemAnalise.embalagem?.toUpperCase()
        return itemRespostaDTO
    }

    static Analise crieAnalise(AnaliseCotacaoDTO analiseDTO){
        Analise analisePersistivel = Analise.get(analiseDTO.id)
        analisePersistivel.itens.each {ItemAnalise itemAnalise->
            ItemAnaliseCotacaoDTO itemAnaliseCotacaoDTO = analiseDTO.itensAnaliseCotacao.find {it.id == itemAnalise.id}
            itemAnalise.quantidade = itemAnaliseCotacaoDTO.quantidade
            itemAnalise.naoComprar = itemAnaliseCotacaoDTO.naoComprar
            itemAnaliseCotacaoDTO.respostas.eachWithIndex {ItemAnaliseRespostaDTO itemRespostaDTO, int posicao ->
                def itemRespostaIndex = itemAnalise.itensRespostaFact.findIndexOf {it.id == itemRespostaDTO.id}
                Collections.swap(itemAnalise.itensRespostaFact, itemRespostaIndex, posicao)
                itemAnalise.itensRespostaFact.get(itemRespostaIndex).precoEmbalagem = itemRespostaDTO.precoAtribuido
            }
        }
        return analisePersistivel
    }

    static ItemAnalise crieItemAnaliseCotacao(ItemAnaliseCotacaoDTO itemAnaliseCotacaoDTO){
        ItemAnalise itemAnalise = ItemAnalise.get(itemAnaliseCotacaoDTO.id)
        itemAnalise.quantidade = itemAnaliseCotacaoDTO.quantidade
        itemAnalise.naoComprar = itemAnaliseCotacaoDTO.naoComprar

        itemAnaliseCotacaoDTO.respostas.eachWithIndex {ItemAnaliseRespostaDTO itemRespostaDTO, int posicao ->
            def itemRespostaIndex = itemAnalise.itensRespostaFact.findIndexOf {it.id == itemRespostaDTO.id}
            Collections.swap(itemAnalise.itensRespostaFact, itemRespostaIndex, posicao)
            itemAnalise.itensRespostaFact.get(itemRespostaIndex).precoEmbalagem = itemRespostaDTO.precoAtribuido
        }
        return itemAnalise
    }

    static List<ItemAnalise> crieItensAnaliseCotacao(List<ItemAnaliseCotacaoDTO> itensAnaliseCotacaoDTO){
        itensAnaliseCotacaoDTO.collect {crieItemAnaliseCotacao(it)}
    }

}