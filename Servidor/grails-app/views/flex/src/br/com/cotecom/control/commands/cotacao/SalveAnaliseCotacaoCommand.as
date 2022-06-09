package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.responders.cotacao.SalveAnaliseCotacaoResponder;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class SalveAnaliseCotacaoCommand implements ICommand {

    [Bindable]
    public var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();

    public function SalveAnaliseCotacaoCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var responder:SalveAnaliseCotacaoResponder = new SalveAnaliseCotacaoResponder();
        if(gerenciadorCotacao.cotacaoSelecionada.analise.alterada){
            var delegate:CotacaoDelegate = new CotacaoDelegate();
            delegate.salveItensAnaliseCotacao(responder, gerenciadorCotacao.cotacaoSelecionada.analise.getItensNaoSalvos())
        }
    }
}

}
