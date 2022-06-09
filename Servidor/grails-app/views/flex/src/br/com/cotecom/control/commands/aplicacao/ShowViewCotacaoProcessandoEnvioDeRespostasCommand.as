package br.com.cotecom.control.commands.aplicacao
{
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.AnaliseCotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;
import br.com.cotecom.view.components.cotacao.analise.CotacaoAnaliseView;
import br.com.cotecom.view.screens.BaseCaixaDeEntrada;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class ShowViewCotacaoProcessandoEnvioDeRespostasCommand implements ICommand {

    private var session:Session = Session.getInstance();
    private var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();

    public function ShowViewCotacaoProcessandoEnvioDeRespostasCommand(){}

    public function execute(event:CairngormEvent):void {
        session.viewState.applicationViewStack.selectedIndex = 1;//todo criar constante na classe
        var baseCxEntrada2:BaseCaixaDeEntrada = session.viewState.applicationViewStack.selectedChild as BaseCaixaDeEntrada;
        baseCxEntrada2.selectedIndex = 2;//todo criar constante na classe
        gerenciadorCotacao.clearCotacaoSelecionada();
        (baseCxEntrada2.getChildAt(2) as CotacaoAnaliseView).analiseCotacao = new AnaliseCotacaoDTO();
        (baseCxEntrada2.getChildAt(2) as CotacaoAnaliseView).currentState = "processandoEnvio";


    }
}
}