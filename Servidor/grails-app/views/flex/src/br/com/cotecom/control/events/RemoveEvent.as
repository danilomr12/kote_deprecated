package br.com.cotecom.control.events{

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class RemoveEvent extends CairngormEvent{

    public static const PRODUTO : String = "RemoveProduto";
    public static const COTACAO : String = "CanceleCotacao";
    public static const ATENDIMENTO : String = "RemovaAtendimento";
    public var objectToRemove:Object;

    public function RemoveEvent( type:String, objectToRemove:Object ){
        super( type );
        this.objectToRemove = objectToRemove;
    }

    public override function clone(): Event{
        return new RemoveEvent(this.type, this.objectToRemove);
    }
}
}
