package br.com.cotecom.control.controllers{

import br.com.cotecom.control.commands.catalogo.DownloadPlanilhaExemploCommand;
import br.com.cotecom.control.commands.aplicacao.ShowViewCaixaDeEntradaCommand;
import br.com.cotecom.control.commands.aplicacao.ShowViewCatalogoCommand;
import br.com.cotecom.control.commands.aplicacao.ShowViewCotacaoEnviadaCommand;
import br.com.cotecom.control.commands.aplicacao.ShowViewCotacaoProcessandoEnvioDeRespostasCommand;
import br.com.cotecom.control.commands.aplicacao.ShowViewCotacaoRascunhoCommand;
import br.com.cotecom.control.commands.aplicacao.ShowViewFornecedoresCommand;
import br.com.cotecom.control.commands.aplicacao.ShowViewNovaCotacaoCommand;
import br.com.cotecom.control.commands.aplicacao.ShowViewPedidoCommand;
import br.com.cotecom.control.commands.aplicacao.ShowViewRespostaCommand;
import br.com.cotecom.control.commands.cotacao.DownloadPlanilhaAnaliseCotacaoCommand;
import br.com.cotecom.control.events.DownloadFileEvent;
import br.com.cotecom.control.events.aplicacao.ShowViewCaixaDeEntradaEvent;
import br.com.cotecom.control.events.aplicacao.ShowViewCatalogoEvent;
import br.com.cotecom.control.events.aplicacao.ShowViewCotacaoEnviadaEvent;
import br.com.cotecom.control.events.aplicacao.ShowViewCotacaoProcessandoEnvioDeRespostasEvent;
import br.com.cotecom.control.events.aplicacao.ShowViewCotacaoRascunhoEvent;
import br.com.cotecom.control.events.aplicacao.ShowViewFornecedoresEvent;
import br.com.cotecom.control.events.aplicacao.ShowViewNovaCotacaoEvent;
import br.com.cotecom.control.events.aplicacao.ShowViewPedidoEvent;
import br.com.cotecom.control.events.aplicacao.ShowViewRespostaEvent;

import com.adobe.cairngorm.control.FrontController;

public class CompradorController extends FrontController{

    public function CompradorController(){
        initialiseCommands();
    }

    private function initialiseCommands() : void{

        addCommand(ShowViewCaixaDeEntradaEvent.EVENT_NAME, ShowViewCaixaDeEntradaCommand);
        addCommand(ShowViewNovaCotacaoEvent.EVENT_NAME, ShowViewNovaCotacaoCommand);
        addCommand(ShowViewCotacaoRascunhoEvent.EVENT_NAME, ShowViewCotacaoRascunhoCommand);
        addCommand(ShowViewCotacaoEnviadaEvent.EVENT_NAME, ShowViewCotacaoEnviadaCommand);
        addCommand(ShowViewRespostaEvent.EVENT_NAME, ShowViewRespostaCommand);
        addCommand(ShowViewPedidoEvent.EVENT_NAME, ShowViewPedidoCommand);
        addCommand(ShowViewCatalogoEvent.EVENT_NAME, ShowViewCatalogoCommand);
        addCommand(ShowViewFornecedoresEvent.EVENT_NAME, ShowViewFornecedoresCommand);
        addCommand(DownloadFileEvent.PLANILHA_IMPORTACAO_PRODUTOS_EXEMPLO, DownloadPlanilhaExemploCommand);
        addCommand(DownloadFileEvent.PLANILHA_ANALISE_COTACAO, DownloadPlanilhaAnaliseCotacaoCommand);
        addCommand(ShowViewCotacaoProcessandoEnvioDeRespostasEvent.EVENT_NAME, ShowViewCotacaoProcessandoEnvioDeRespostasCommand);
    }
}
}
