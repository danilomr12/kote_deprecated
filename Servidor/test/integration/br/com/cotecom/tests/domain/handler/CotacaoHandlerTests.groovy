package br.com.cotecom.tests.domain.handler

import br.com.cotecom.domain.cotacao.CotacaoHandler
import br.com.cotecom.util.test.RespostaUnitTestCase

public class CotacaoHandlerTests  extends RespostaUnitTestCase{

    CotacaoHandler cotacaoHandler
            
    void testTrateEnvioResposta(){
        CotacaoHandler.getInstance().dispatch(CotacaoHandler.ENVIO_RESPOSTA, cotacao.respostas.asList().get(0), false)
    }

}