package br.com.cotecom.control.commands.fornecedores
{
import br.com.cotecom.control.delegates.UsuarioDelegate;
import br.com.cotecom.control.responders.fornecedores.LoadEmpresasFornecedoresResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class LoadEmpresaFornecedorCommand implements ICommand
	{
		public function LoadEmpresaFornecedorCommand(){}
		
		public function execute(event:CairngormEvent):void
		{
			var responder:LoadEmpresasFornecedoresResponder = 
				new LoadEmpresasFornecedoresResponder();
			var delegate:UsuarioDelegate = new UsuarioDelegate();
			delegate.loadEmpresasFornecedores(responder);
		}
	}
}