package br.com.cotecom.control.commands.aplicacao{

import br.com.cotecom.control.events.StatusMessageChangeEvent;
import br.com.cotecom.model.Session;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class StatusMessageChangeCommand implements ICommand {
		
		public var model:Session = Session.getInstance();
		
		public function StatusMessageChangeCommand(){}
 
		public function execute( event : CairngormEvent ) : void{
			var statusMessageChangeEvent:StatusMessageChangeEvent = event as StatusMessageChangeEvent;
		
			model.statusMessage = statusMessageChangeEvent.message;
		
		}
	}
}
