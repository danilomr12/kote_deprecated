package br.com.cotecom.control.errors
{
import br.com.cotecom.model.Session;
import br.com.cotecom.view.util.Icons;

import mx.collections.ArrayCollection;
import mx.rpc.events.FaultEvent;

public class DefaultErrorHandler implements IErrorHandler
	{
		public function DefaultErrorHandler(){}
	
		public var model:Session = Session.getInstance();

		public function showError(fault:Object = null): void{
			if(fault is FaultEvent){
				model.errorConsole += fault.toString() + "\n";
				model.messageHandler.showTextMessage(fault.type, fault.toString(), Icons.ERROR);
			}
			else if(fault is ArrayCollection){
				var errors:ArrayCollection = fault as ArrayCollection;
				var errorsToShow:String = "";
				
				for each(var error:ServerError in errors){
					errorsToShow += error.mensagem + "\n" + error.causa + "\n\n"	
				}
				if(errorsToShow == "") errorsToShow = "Ocorreu um Erro desconhecido. Tente novamente em instantes.";
				
				model.messageHandler.showTextMessage("Erro", errorsToShow, Icons.ERROR);
			}
			else if(fault is ServerError){
				var erro:ServerError = fault as ServerError;
				model.messageHandler.showTextMessage(erro.acao, erro.mensagem, Icons.ERROR);
			}
            else if(fault is String){
                model.messageHandler.showTextMessage("Falha", fault.toString(), Icons.ERROR);
            }
			else
				model.messageHandler.showTextMessage("Falha", "Ocorreu um Erro desconhecido. Por favor, contate o administrador do sistema.", Icons.ERROR);
		}

        private function handleFaultError(fault:FaultEvent):void{
            var error:String = fault.toString();
        }
	}
}