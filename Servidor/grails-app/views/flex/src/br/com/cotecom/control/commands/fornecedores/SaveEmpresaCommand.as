package br.com.cotecom.control.commands.fornecedores{

import br.com.cotecom.control.delegates.UsuarioDelegate;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.responders.fornecedores.SaveEmpresaFornecedorResponder;
import br.com.cotecom.control.responders.usuario.SaveEmpresaClienteResponder;
import br.com.cotecom.model.domain.dtos.usuarios.Empresa;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class SaveEmpresaCommand implements ICommand{
		
		public function SaveEmpresaCommand(){}
		
		public function execute( event : CairngormEvent ) : void{
			var saveEvent:SaveEvent = event as SaveEvent;

            var responder:*;
            var delegate:UsuarioDelegate = new UsuarioDelegate();
			if(saveEvent.type == SaveEvent.EMPRESA_FORNECEDOR){
                responder = new SaveEmpresaFornecedorResponder();
                delegate.saveFornecedor(responder, saveEvent.objectToSave as Empresa);
            }else if(saveEvent.type == SaveEvent.EMPRESA_CLIENTE){
                responder = new SaveEmpresaClienteResponder();
                delegate.saveCliente(responder, saveEvent.objectToSave as Empresa);

            }
		}
	}
}
