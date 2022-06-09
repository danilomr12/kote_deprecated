package br.com.cotecom.services

import br.com.cotecom.domain.cotacao.Cotacao
import br.com.cotecom.domain.cotacao.EstadoCotacao

import br.com.cotecom.domain.dto.inbox.ItemInboxDTO

import br.com.cotecom.domain.usuarios.Usuario
import br.com.cotecom.dtos.assembler.CotacaoAssembler
import org.apache.log4j.Logger

import br.com.cotecom.domain.usuarios.Estoquista
import br.com.cotecom.domain.usuarios.Comprador

class CaixaDeEntradaService {

    private static final log = Logger.getLogger(CaixaDeEntradaService.class)
    def usuarioService

    // TODO Limitar a query
    def loadCaixaDeEntrada(Usuario usuario){
        def usuarioLogado = usuarioService.sessionUser
        def cotacoes = []
        if(usuarioLogado instanceof Estoquista){
            cotacoes = Cotacao.executeQuery("from Cotacao where codigo_estado_cotacao='${EstadoCotacao.RASCUNHO}'ORDER BY dataCriacao DESC")
        }else{
            if(usuarioLogado?.clientes?.size() > 0){
                cotacoes = Cotacao.createCriteria().list {
                    'in'('empresa', usuarioLogado.clientes)
                    order("dataSalva", "desc")
                }
            }
        }
        ArrayList<ItemInboxDTO> itens = new ArrayList<ItemInboxDTO>()

        def cotacoesNaoCanceladas = cotacoes - cotacoes?.findAll {Cotacao cotacao ->
            cotacao.codigoEstadoCotacao == EstadoCotacao.CANCELADA
        }
        for (cotacao in cotacoesNaoCanceladas) {
            for (resposta in cotacao.respostas) {
                if (!resposta.lida) {
                    resposta.lida = true
                    resposta.save()
                }
            }
        }
        cotacoesNaoCanceladas.each {itens << CotacaoAssembler.crieCotacaoItemInboxDTO(it)}

        return itens
    }

}