package br.com.cotecom.control.commands.resposta
{
import br.com.cotecom.control.delegates.RespostaDelegate;
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.responders.resposta.LoadRespostasResponder;
import br.com.cotecom.model.Session;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class LoadRespostasCommand implements ICommand
{
    public var model:Session = Session.getInstance();

    public function LoadRespostasCommand(){}

    public function execute(event:CairngormEvent):void {
        var evt:LoadEvent = event as LoadEvent;
        var delegate:RespostaDelegate = new RespostaDelegate();
        var responder:LoadRespostasResponder = new LoadRespostasResponder();
        delegate.loadRespostas(responder)
    }

}
}