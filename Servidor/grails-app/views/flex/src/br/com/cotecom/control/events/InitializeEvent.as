package br.com.cotecom.control.events{

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class InitializeEvent extends CairngormEvent{
		
		public static const NAME : String = "Initialize";

		public function InitializeEvent(){
			super(NAME);
		}

		public override function clone() : Event{
			return new InitializeEvent();
		}
	}
}
