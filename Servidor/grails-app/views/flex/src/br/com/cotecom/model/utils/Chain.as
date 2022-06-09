package br.com.cotecom.model.utils{
import com.adobe.cairngorm.control.CairngormEvent;

import mx.collections.ArrayCollection;

public class Chain{
		
		public var events:ArrayCollection;
		public var currentIndex:int;
		
		public function Chain(){
			this.events = new ArrayCollection;
			this.currentIndex = 0;
		}
		
		public function get currentEvent():CairngormEvent{
			return this.events.getItemAt(currentIndex) as CairngormEvent;
		}
		
		public function addEvent(event:CairngormEvent):void{
			this.events.addItem(event)
		}
		
		public function dispatch():void{
			if(events.length){
				this.currentEvent.dispatch();
			}
		}
		
		public function dispatchNext():void{
			this.currentIndex++;
			if(this.currentIndex<events.length){
				this.dispatch();
			}else{
				finish();
			}
		}
		
		public function finish():void{
			
		}
	}
}