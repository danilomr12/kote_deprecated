package br.com.cotecom.control.commands.aplicacao
{
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.events.aplicacao.ShowViewCotacaoEnviadaEvent;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.cotacao.CotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;
import br.com.cotecom.view.screens.BaseCaixaDeEntrada;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class ShowViewCotacaoEnviadaCommand implements ICommand {

    private var session:Session = Session.getInstance();

    public function ShowViewCotacaoEnviadaCommand(){}

    public function execute(event:CairngormEvent):void {
        session.viewState.applicationViewStack.selectedIndex = 1;//todo criar constante na classe
        var baseCxEntrada2:BaseCaixaDeEntrada = session.viewState.applicationViewStack.selectedChild as BaseCaixaDeEntrada;
        baseCxEntrada2.selectedIndex = 2;//todo criar constante na classe

        var evt:ShowViewCotacaoEnviadaEvent = event as ShowViewCotacaoEnviadaEvent;
        var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();
        var cotacaoSelecionada:CotacaoDTO = gerenciadorCotacao.cotacaoSelecionada;
/*        if(cotacaoSelecionada == null ||cotacaoSelecionada.id == null || evt.cotacaoId != cotacaoSelecionada.id || cotacaoSelecionada.alterada ||
                evt.codigoEstadoCotacao != cotacaoSelecionada.codigoEstadoCotacao){*/
            //todo: verificar status das respostas para, se algum houver mudado carregar cotacao
            gerenciadorCotacao.clearCotacaoSelecionada();
            new LoadEvent(LoadEvent.ANALISE_COTACAO, evt.cotacaoId).dispatch();
        /*}*/
    }
}
}