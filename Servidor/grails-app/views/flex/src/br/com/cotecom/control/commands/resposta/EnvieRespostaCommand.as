package br.com.cotecom.control.commands.resposta{

import br.com.cotecom.control.delegates.RespostaDelegate;
import br.com.cotecom.control.events.resposta.SendEvent;
import br.com.cotecom.control.responders.resposta.EnvieRespostaResponder;
import br.com.cotecom.model.domain.resposta.TelaResposta;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class EnvieRespostaCommand implements ICommand{
		
		public function EnvieRespostaCommand(){}
 
		public function execute( event : CairngormEvent ) : void{
			var envieEvent:SendEvent = event as SendEvent;
			
			var responder:EnvieRespostaResponder = new EnvieRespostaResponder();
			var delegate:RespostaDelegate = new RespostaDelegate();

            var dadosAEnviar:TelaResposta = new TelaResposta();
            var telaResposta:TelaResposta = (envieEvent.objectToSend as TelaResposta);

            dadosAEnviar.resposta = telaResposta.resposta;
            dadosAEnviar.itensResposta = telaResposta.getUnsavedItens();

            delegate.sendResposta(responder, dadosAEnviar);
		}
	}
}
