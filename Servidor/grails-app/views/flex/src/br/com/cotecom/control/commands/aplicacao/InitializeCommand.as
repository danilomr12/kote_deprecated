package br.com.cotecom.control.commands.aplicacao {
import br.com.cotecom.control.errors.ErrorHandlerFactory;
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.events.StatusMessageChangeEvent;
import br.com.cotecom.control.message.MessageHandlerFactory;
import br.com.cotecom.control.services.handlers.ServerCommunicationHandler;
import br.com.cotecom.model.Session;

import com.adobe.cairngorm.commands.Command;
import com.adobe.cairngorm.control.CairngormEvent;

public class InitializeCommand implements Command {

    public var model:Session = Session.getInstance();

    public function InitializeCommand(){}

    public function execute( event : CairngormEvent ) : void{

        model.errorHandler = ErrorHandlerFactory.createErrorHandler(ErrorHandlerFactory.DEFAULT);
        model.messageHandler = MessageHandlerFactory.createMessageHandler(MessageHandlerFactory.DEFAULT);
        model.communicationHandler = new ServerCommunicationHandler();

        new LoadEvent(LoadEvent.USUARIO_LOGADO).dispatch();
        new StatusMessageChangeEvent("started").dispatch();
    }
}
}
