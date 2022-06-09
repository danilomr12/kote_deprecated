package br.com.cotecom.control.events.formularios {
import flash.events.Event;

public class RemoveEvent extends Event {

    public var index:int;

    public function RemoveEvent(type:String, index:int) {
        this.index = index;
        super(type, true);
    }
}
}