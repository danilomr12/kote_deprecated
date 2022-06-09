package br.com.cotecom.tests.services.remote

import br.com.cotecom.domain.dto.resposta.ItemRespostaDTO
import br.com.cotecom.domain.dto.resposta.RespostaDTO
import br.com.cotecom.domain.dto.tela.TelaRespostaRepresentanteDTO
import br.com.cotecom.domain.resposta.EstadoResposta
import br.com.cotecom.domain.resposta.IResposta
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.services.remote.RemoteRespostaService
import br.com.cotecom.util.test.RespostaUnitTestCase

public class RemoteRespostaServiceTests extends RespostaUnitTestCase {

    RemoteRespostaService remoteRespostaService

    void testGetResposta() {
        IResposta resposta = cotacao.respostas.asList().get(0)
        TelaRespostaRepresentanteDTO telaRespostaDTO = remoteRespostaService.getResposta(resposta.respostaUrlDigest)
        assertNotNull telaRespostaDTO.resposta
        assertNotNull telaRespostaDTO.itensResposta
        assertEquals telaRespostaDTO.resposta.id, resposta.id
        assertEquals telaRespostaDTO.resposta.dataCriacao, resposta.dataCriacao
        assertEquals telaRespostaDTO.itensResposta.size(), resposta.itens.size()

        String urlDigest = "DIGEST_INVALIDO"
        TelaRespostaRepresentanteDTO respostaDigestInvalido = remoteRespostaService.getResposta(urlDigest)
        assertNull respostaDigestInvalido
    }

    void testSaveItensResposta() {
        IResposta resposta = cotacao.respostas.asList().get(0)
        TelaRespostaRepresentanteDTO telaRespostaDTO = remoteRespostaService.getResposta(resposta.respostaUrlDigest)
        assertTrue remoteRespostaService.aceiteResposta(telaRespostaDTO.resposta.id)

        telaRespostaDTO.itensResposta.eachWithIndex {ItemRespostaDTO item, int index ->
            item.precoEmbalagem = index * 12
        }
        assertTrue remoteRespostaService.salveItemResposta(telaRespostaDTO.itensResposta)
    }

    void testAceiteRespostaInexistente() {
        def errors = remoteRespostaService.aceiteResposta(0)
        assertLength 1, errors
        def error = errors[0]
        assertEquals error.acao, "Aceitar Resposta"
        assertEquals error.mensagem, "Não foi possível aceitar a cotação."
        assertEquals error.causa, "Resposta Inexistente"
    }

    void testRecuseResposta() {
        IResposta resposta = cotacao.respostas.asList().get(0)
        TelaRespostaRepresentanteDTO telaRespostaDTO = remoteRespostaService.getResposta(resposta.respostaUrlDigest)
        assertTrue remoteRespostaService.recuseResposta(telaRespostaDTO.resposta.id) instanceof RespostaDTO
        assertEquals EstadoResposta.RECUSADA, Resposta.get(telaRespostaDTO.resposta.id).codigoEstado
    }

    void testRecuseRespostaInexistente() {
        def errors = remoteRespostaService.recuseResposta(0)
        assertLength 1, errors
        def error = errors[0]
        assertEquals error.acao, "Recusar Resposta"
        assertEquals error.mensagem, "Não foi possível recusar sua cotação."
        assertEquals error.causa, "Resposta Inexistente"
    }

    void testEnvieResposta(){
        IResposta resposta = cotacao.respostas.asList().get(0)
        TelaRespostaRepresentanteDTO telaRespostaDTO = remoteRespostaService.getResposta(resposta.respostaUrlDigest)
        telaRespostaDTO.itensResposta.eachWithIndex {ItemRespostaDTO item, int index ->
            item.precoEmbalagem = index * 12            
        }
        assertTrue remoteRespostaService.aceiteResposta(telaRespostaDTO.resposta.id)
        assertTrue remoteRespostaService.salveItemResposta(telaRespostaDTO.itensResposta)
        assertTrue remoteRespostaService.envieResposta(telaRespostaDTO) instanceof RespostaDTO
    }

    void testEnvieRespostaInexistente(){
        def errors = remoteRespostaService.envieResposta(null)
        assertLength 1, errors
        def error = errors[0]
        assertEquals error.acao, "Enviar Resposta"
        assertEquals error.mensagem, "Não foi possível enviar sua cotação."
        assertEquals error.causa, "Resposta Inexistente"
    }
}