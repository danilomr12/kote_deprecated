package br.com.cotecom.control.events{

import br.com.cotecom.model.domain.dtos.usuarios.Usuario;

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class RepresentanteEvent extends CairngormEvent {

    public static const REPRESENTANTE_SELECIONADO_EVENT : String = "representanteSelecionadoEvent";

    public var representante:Usuario;

    public function RepresentanteEvent (type:String, representante:Usuario= null) {
        super(type);
        this.representante = representante;
    }

    public override function clone() : Event {
        return new RepresentanteEvent (this.type , this.representante);
    }
}
}
