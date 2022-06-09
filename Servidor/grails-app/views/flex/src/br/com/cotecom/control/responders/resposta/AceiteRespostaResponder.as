package br.com.cotecom.control.responders.resposta
{
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.domain.resposta.TelaResposta;
import br.com.cotecom.model.services.resposta.GerenciadorResposta;
import br.com.cotecom.model.utils.EstadoResposta;

public class AceiteRespostaResponder extends ServerResponder {

		public function AceiteRespostaResponder(){
            super();
        }
		
		override public function result(data:Object):void {
            super.resetCommunication();
            if (hasErrors(data)) return;
            var gerenciadorResposta:GerenciadorResposta = GerenciadorResposta.getInstance();
            gerenciadorResposta.editavel = true;
            var telaResposta:TelaResposta = gerenciadorResposta.telaResposta;
            gerenciadorResposta.telaResposta = null;
            telaResposta.resposta.status = EstadoResposta.RESPONDENDO;
            gerenciadorResposta.telaResposta = telaResposta;
		}

	}
}