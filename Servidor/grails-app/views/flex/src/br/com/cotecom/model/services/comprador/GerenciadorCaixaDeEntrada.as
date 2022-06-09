package br.com.cotecom.model.services.comprador{
import br.com.cotecom.control.events.cotacao.VerificaNovasRespostasEvent;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.inbox.CotacaoItemInboxDTO;
import br.com.cotecom.model.domain.dtos.inbox.RespostaItemInboxDTO;
import br.com.cotecom.model.domain.dtos.inbox.TelaCaixaDeEntradaDTO;
import br.com.cotecom.view.screens.CaixaDeEntradaView;

import com.hydraframework.components.growler.model.GrowlDescriptor;
import com.hydraframework.components.growler.view.events.GrowlEvent;

import flash.events.Event;
import flash.events.TimerEvent;
import flash.utils.Timer;

import mx.collections.ArrayCollection;
import mx.core.Application;

[Bindable]
public class GerenciadorCaixaDeEntrada{

    public var inBox:ArrayCollection;
    public var view:CaixaDeEntradaView;
    public var inBoxObject:TelaCaixaDeEntradaDTO;
    public var timerAtualizacaoCxEntrada:Timer;

    private static var instance:GerenciadorCaixaDeEntrada;
    private var session:Session = Session.getInstance();
    private var caixaDeEntradaAberta:Boolean = false;

    public static function getInstance():GerenciadorCaixaDeEntrada{
        if(instance == null)
            instance = new GerenciadorCaixaDeEntrada(new SingletonEnforcer());
        return instance;
    }

    public function GerenciadorCaixaDeEntrada(enforcer:SingletonEnforcer){
        if(!enforcer is SingletonEnforcer)
            throw new Error("use the get instance function");
        this.inBox = new ArrayCollection();
        this.timerAtualizacaoCxEntrada = new Timer(60000,1);
        timerAtualizacaoCxEntrada.addEventListener(TimerEvent.TIMER, verifiqueNovasRespostas);
       // monitoreNovasRespostas()
    }

    public function monitoreNovasRespostas():void {
        this.caixaDeEntradaAberta = true;
        this.timerAtualizacaoCxEntrada.start();
    }

    private function verifiqueNovasRespostas(evt:Event):void {
        new VerificaNovasRespostasEvent().dispatch();
    }

    public function getCotacaoMother(respostaId:String):String{
        this.inBox;
        return "";
    }

    public function getCotacao(respostaId:String):CotacaoItemInboxDTO{
        var result:CotacaoItemInboxDTO;
        for each(var item:CotacaoItemInboxDTO in this.inBoxObject.itensCaixaDeEntrada){
            for each(var resp:RespostaItemInboxDTO in item.respostas){
                if(resp.id == respostaId)
                    result = item
            }
        }
        return result;
    }

    public function getRespostasDaMesmaCotacao(respostaId:String):ArrayCollection{
        var resultArray:ArrayCollection = new ArrayCollection();
        var cotacao:CotacaoItemInboxDTO = getCotacao(respostaId);
        for each(var item:RespostaItemInboxDTO in cotacao.respostas){
            resultArray.addItem(item)
        }
        return resultArray;
    }

    public function setInbox(value:ArrayCollection):void{
        if(value.length){
            this.inBox = value;
        }
    }

    public function atualizeCaixaDeEntrada(event:Event):void{
        session.caixaDeEntradaService.getAllItens();
        resetTimerAtualizacaoCxEntrada();
    }

    public function crieNotificacaoNovaResposta():void {
        var appMain:CompradorMain = Application.application as CompradorMain;
        appMain.growlerNovaResposta.removeEventListener(GrowlEvent.CLICK, atualizeCaixaDeEntrada);

        appMain.growlerNovaResposta.closeFuntion = resetTimerAtualizacaoCxEntrada;
        appMain.growlerNovaResposta.addEventListener(GrowlEvent.CLICK, atualizeCaixaDeEntrada);
        appMain.growlerNovaResposta.x = (appMain.width-220);

        appMain.growlerNovaResposta.growl(new GrowlDescriptor("Nova Resposta Alterada",
                "Deseja atualizar a caixa de entrada?",
                "Sim",
                0xFFCA51));
    }

    public function pareMonitoramento():void {
        caixaDeEntradaAberta = false;
        this.timerAtualizacaoCxEntrada.stop();
    }

    public function resetTimerAtualizacaoCxEntrada(evt:Event = null):void {
        if(caixaDeEntradaAberta){
            this.timerAtualizacaoCxEntrada.reset();
            this.timerAtualizacaoCxEntrada.start();
        }
    }
}
}

class SingletonEnforcer{};