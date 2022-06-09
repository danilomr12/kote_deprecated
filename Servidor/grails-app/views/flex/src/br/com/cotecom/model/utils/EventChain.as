package br.com.cotecom.model.utils{
import mx.collections.ArrayCollection;

public class EventChain{

		public static var chains:ArrayCollection = new ArrayCollection();
		
		public static function dispatchChain(events:ArrayCollection):void{
			var newChain:Chain = new Chain();
			newChain.events = events;
			chains.addItem(newChain);
			newChain.dispatch();
		}
		
		private static function getChainByEventType(type:String):Chain{
			for each(var chain:Chain in chains){
				if(chain.currentEvent.type == type){
					return chain;
				}
			}
			return null;
		}
		
		public static function dispatchEventAfter(type:String):void{
			getChainByEventType(type).dispatchNext();
		}
	}
}