package br.com.cotecom.control.controllers{

import br.com.cotecom.control.commands.catalogo.SaveProdutoCommand;
import br.com.cotecom.control.commands.catalogo.SearchProdutoCommand;
import br.com.cotecom.control.commands.cotacao.RefreshRespostasCommand;
import br.com.cotecom.control.commands.pedido.DownloadPedidoCommand;
import br.com.cotecom.control.commands.pedido.FaturePedidoCommand;
import br.com.cotecom.control.commands.pedido.LoadPedidoCommand;
import br.com.cotecom.control.commands.resposta.AceiteRespostaCommand;
import br.com.cotecom.control.commands.resposta.DownloadPlanilhaRespostaCommand;
import br.com.cotecom.control.commands.resposta.EnvieRespostaCommand;
import br.com.cotecom.control.commands.resposta.ImportePlanilhaRespostaCommand;
import br.com.cotecom.control.commands.resposta.LoadRespostaCommand;
import br.com.cotecom.control.commands.resposta.LoadRespostasCommand;
import br.com.cotecom.control.commands.resposta.RecuseRespostaCommand;
import br.com.cotecom.control.commands.resposta.SalveItensRespostaCommand;
import br.com.cotecom.control.commands.usuario.LoginCommand;
import br.com.cotecom.control.events.AcceptEvent;
import br.com.cotecom.control.events.DownloadFileEvent;
import br.com.cotecom.control.events.ImporteEvent;
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.events.RefreshRespostasEvent;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.events.SearchEvent;
import br.com.cotecom.control.events.pedido.FaturePedidoEvent;
import br.com.cotecom.control.events.pedido.FaturePedidoEvent;
import br.com.cotecom.control.events.resposta.RespostaEvent;
import br.com.cotecom.control.events.resposta.SendEvent;
import br.com.cotecom.control.events.usuario.LoginEvent;

import com.adobe.cairngorm.control.FrontController;

public class RepresentanteController extends FrontController{

    public function RepresentanteController(){
        initialiseCommands();
    }

    private function initialiseCommands() : void{
        addCommand(LoadEvent.RESPOSTAS, LoadRespostasCommand);
        addCommand(SaveEvent.RESPOSTA, SalveItensRespostaCommand);
        addCommand(SendEvent.RESPOSTA, EnvieRespostaCommand);
        addCommand(LoadEvent.RESPOSTA, LoadRespostaCommand);
        addCommand(RespostaEvent.RECUSAR, RecuseRespostaCommand);
        addCommand(RefreshRespostasEvent.REFRESH_RESPOSTAS, RefreshRespostasCommand);
        addCommand(AcceptEvent.RESPOSTA, AceiteRespostaCommand);
        addCommand(DownloadFileEvent.PLANILHA_COTACAO_RESPOSTA_OFF_LINE, DownloadPlanilhaRespostaCommand);
        addCommand(ImporteEvent.PLANILHA_RESPOSTA, ImportePlanilhaRespostaCommand);
        addCommand(SearchEvent.PRODUTO, SearchProdutoCommand );
        addCommand(SaveEvent.PRODUTO, SaveProdutoCommand );
        addCommand(LoadEvent.PEDIDO, LoadPedidoCommand);
        addCommand(FaturePedidoEvent.EVENT_NAME, FaturePedidoCommand);
        addCommand(LoginEvent.EVENT_NAME, LoginCommand);
        addCommand(DownloadFileEvent.PEDIDO, DownloadPedidoCommand);
    }
}
}
