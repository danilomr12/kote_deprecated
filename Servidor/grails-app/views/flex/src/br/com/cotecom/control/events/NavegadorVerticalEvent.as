package br.com.cotecom.control.events
{
import br.com.cotecom.view.components.ToggleButton;

import flash.events.Event;

public class NavegadorVerticalEvent extends Event
	{
		public var button:ToggleButton;
		public var view:*;
		
		public static const EVENT_NAME:String = "buttonClick";
		
		public function NavegadorVerticalEvent(button:ToggleButton, view:*, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			this.button = button;
			this.view = view;
			super(EVENT_NAME, bubbles, cancelable);
		}
	}
}