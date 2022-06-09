package br.com.cotecom.control.events.cotacao {
import br.com.cotecom.model.domain.cotacao.ItemCotacaoDTO;

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class RemovaItemCotacaoEvent extends CairngormEvent{

    public static const EVENT_NAME:String = "RemoveItemCotacao";
    public var itemCotacao:ItemCotacaoDTO;

    public function RemovaItemCotacaoEvent(itensCotacao:ItemCotacaoDTO) {
        super( EVENT_NAME );
        this.itemCotacao = itensCotacao;
    }

    public override function clone() : Event {
			return new RemovaItemCotacaoEvent(this.itemCotacao);
    }
}
}
