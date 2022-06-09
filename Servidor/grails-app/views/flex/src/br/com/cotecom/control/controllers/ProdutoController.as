package br.com.cotecom.control.controllers{

import br.com.cotecom.control.commands.catalogo.RemoveProdutoCommand;
import br.com.cotecom.control.commands.catalogo.SaveProdutoCommand;
import br.com.cotecom.control.commands.catalogo.SearchProdutoCommand;
import br.com.cotecom.control.commands.catalogo.importePlanilhaProdutosCommand;
import br.com.cotecom.control.events.ImporteEvent;
import br.com.cotecom.control.events.RemoveEvent;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.events.SearchEvent;

import com.adobe.cairngorm.control.FrontController;

public class ProdutoController extends FrontController{

		public function ProdutoController(){
			initialiseCommands();
		}

		private function initialiseCommands() : void{
			
			addCommand( SearchEvent.PRODUTO, SearchProdutoCommand );
			addCommand( SaveEvent.PRODUTO, SaveProdutoCommand );
			addCommand( RemoveEvent.PRODUTO, RemoveProdutoCommand );
			addCommand( ImporteEvent.PLANILHA_PRODUTOS, importePlanilhaProdutosCommand)
		}
	}
}
