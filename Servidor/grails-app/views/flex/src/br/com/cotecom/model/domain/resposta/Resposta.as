package br.com.cotecom.model.domain.resposta{
import br.com.cotecom.model.utils.EstadoResposta;
import flash.events.Event;
import mx.collections.ArrayCollection;
import mx.formatters.DateFormatter;

[Bindable]
[RemoteClass(alias="br.com.cotecom.domain.dto.resposta.RespostaDTO")]
public class Resposta{

    public var dataCriacao:Date;
    public var dataSalva:Date;
    public var dataValidade:Date;
    private var _status:int;
    public var representanteId:*;
    public var cotacaoId:*;
    public var id:*;
    public var clienteNomeFantasia:String;
    public var clienteRazaoSocial:String;
    public var pedidosDTOs:ArrayCollection;

    public function Resposta(
            dataCriacao:Date = null,
            dataSalva:Date = null,
            status:int = 0,
            representanteId:* = null,
            cotacaoId:* = null,
            id:* = null){

        this.dataCriacao = dataCriacao;
        this.dataSalva = dataSalva;
        this.status = status;
        this.representanteId = representanteId;
        this.cotacaoId = cotacaoId;
        this.id = id;
    }

    public function equalsId(outra:Resposta):Boolean{
        if(outra){
            return this.id == outra.id;
        }
        return false;
    }

    public function equals(outra:Resposta):Boolean{
        if(outra
                &&(this.dataCriacao.time == outra.dataCriacao.time)
                &&(this.dataSalva.time == outra.dataSalva.time)
                &&(this.status == outra.status)
                &&(this.cotacaoId == outra.cotacaoId)
                &&(this.representanteId == outra.representanteId)){
            return true;
        }
        return false;
    }


    public function set status(value:int):void{
        this._status = value;
        var event:Event = new Event("statusRespostaChanged");
        dispatchEvent(event)
    }

    public function get status():int{
        return this._status;
    }

    public function get dataCriacaoFormatada():String{
        var dateFormatter:DateFormatter = new DateFormatter();
        dateFormatter.formatString = "DD/MM/YY HH:NN";
        return dateFormatter.format(this.dataCriacao);
    }

    public function set dataSalvaFormatada(date:String):void{
    }

    public function get dataSalvaFormatada():String{
        var dateFormatter:DateFormatter = new DateFormatter();
        dateFormatter.formatString = "DD/MM/YY HH:NN";
        return dateFormatter.format(this.dataSalva);
    }

    public function get dataValidadeFormatada():String{
        var dateFormatter:DateFormatter = new DateFormatter();
        dateFormatter.formatString = "DD/MM/YY HH:NN";
        return dateFormatter.format(this.dataValidade);
    }

    [Bindable(event="statusRespostaChanged")]
    public function statusStringVisaoComprador():String{
        return EstadoResposta.getDescricaoVisaoComprador(this.status);
    }

    [Bindable(event="statusRespostaChanged")]
    public function statusStringVisaoRepresentante():String{
        return EstadoResposta.getDescricaoVisaoRepresentante(this.status);
    }
}
}