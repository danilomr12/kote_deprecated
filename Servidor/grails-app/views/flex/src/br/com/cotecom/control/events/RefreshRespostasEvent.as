package br.com.cotecom.control.events{

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class RefreshRespostasEvent extends CairngormEvent {
		
		public static const REFRESH_RESPOSTAS : String = "RefreshRespostasEvent";
		
		public function RefreshRespostasEvent () {
			super(type);
		}
		
		public override function clone() : Event {
			return new RefreshRespostasEvent();
		}
		
	}
}
