package br.com.cotecom.model.services.resposta
{

import br.com.cotecom.control.events.AcceptEvent;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.events.resposta.RespostaEvent;
import br.com.cotecom.control.events.resposta.SendEvent;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.pedido.TelaPedido;
import br.com.cotecom.model.domain.resposta.ItemResposta;
import br.com.cotecom.model.domain.resposta.TelaResposta;
import br.com.cotecom.view.util.Icons;

import flash.events.TimerEvent;
import flash.utils.Timer;

import mx.collections.ArrayCollection;

[Bindable]
public class GerenciadorResposta{

    private static var instance:GerenciadorResposta;

    private var _respostas:ArrayCollection;
    public var telaResposta:TelaResposta;
    public var telaPedidoDTO:TelaPedido;
    public var editavel:Boolean;
    private var timerSalvamentoItens:Timer;

    public function set respostas(respostas:ArrayCollection):void{
        this._respostas = respostas;
    }

    public function get respostas():ArrayCollection{
        return this._respostas;
    }

    public function aceite():void{
        var acceptEvent:AcceptEvent = new AcceptEvent(AcceptEvent.RESPOSTA, telaResposta.resposta.id);
        acceptEvent.dispatch();
    }

    public function salveItens():void{
        if(telaResposta != null && telaResposta.getUnsavedItens().length > 0){
            var saveEvent:SaveEvent = new SaveEvent(SaveEvent.RESPOSTA, telaResposta.getUnsavedItens());
            saveEvent.dispatch();
        }else{
            resetTimerSalvamento();
        }
    }

    public function send():void{
        var sendEvent:SendEvent = new SendEvent(SendEvent.RESPOSTA, telaResposta);
        sendEvent.dispatch();
        interrompaSalvamentoAutomatico();
    }

    public function recuse():void{
        var refuseEvent:RespostaEvent = new RespostaEvent(RespostaEvent.RECUSAR, telaResposta.resposta.id);
        refuseEvent.dispatch();
    }

    public function GerenciadorResposta(enforcer:SingletonEnforcer){
        if(!enforcer is SingletonEnforcer)
            throw new Error("use the get instance function");
        timerSalvamentoItens = new Timer(10000, 1);
        timerSalvamentoItens.addEventListener(TimerEvent.TIMER, timerHandler);
    }

    private function timerHandler(event:TimerEvent):void {
        salveItens();
    }

    public static function getInstance():GerenciadorResposta{
        if(instance == null)
            instance = new GerenciadorResposta(new SingletonEnforcer());
        return instance;
    }
    
    public function alerteUsuario():void {
        var msg:String = "Você não pode mais editar esta cotação.";
        Session.getInstance().messageHandler.showTextMessage("Atenção!", msg, Icons.WARNING);
    }

    public function inicieSalvamentoAutomatico():void {
        if(timerSalvamentoItens.running)
            interrompaSalvamentoAutomatico();
        timerSalvamentoItens.start();
    }

    public function interrompaSalvamentoAutomatico():void {
        timerSalvamentoItens.stop();
    }

    public function resetTimerSalvamento():void {
        this.timerSalvamentoItens.reset();
        this.timerSalvamentoItens.start();
    }

    public function setAllItensAsSaved():void {
        telaResposta.setAllItensAsSaved();
    }

    public function setItemAsNotSaved(item:ItemResposta):void {
        item.setNotSaved();
        if(item.respostaId == telaResposta.resposta.id)
            telaResposta.alterada = true;
    }
}
}
class SingletonEnforcer{};