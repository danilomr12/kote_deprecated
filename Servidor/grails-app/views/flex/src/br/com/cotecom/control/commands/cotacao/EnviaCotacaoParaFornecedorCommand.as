package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.cotacao.EnviaCotacaoParaFornecedorEvent;
import br.com.cotecom.control.responders.cotacao.EnviaCotacaoParaFornecedorResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class EnviaCotacaoParaFornecedorCommand implements ICommand {

    public function EnviaCotacaoParaFornecedorCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var envieEvent:EnviaCotacaoParaFornecedorEvent = event as EnviaCotacaoParaFornecedorEvent;

        var responder:EnviaCotacaoParaFornecedorResponder =
                new EnviaCotacaoParaFornecedorResponder();

        var delegate:CotacaoDelegate = new CotacaoDelegate();
        //TODO- MUDAR QD COTACAOSELECIONADA ESTIVER SENDO POPULADA CORRETAMENTE
        delegate.envieCotacaoParaFornecedor(responder, envieEvent.idCotacao, envieEvent.idRepresentante);
    }
}
}