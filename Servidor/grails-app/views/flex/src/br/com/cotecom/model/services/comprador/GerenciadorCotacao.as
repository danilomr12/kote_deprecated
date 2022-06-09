package br.com.cotecom.model.services.comprador
{
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.events.cotacao.AdicioneProdutoACotacaoEvent;
import br.com.cotecom.control.events.cotacao.CrieNovaCotacaoRascunhoEvent;
import br.com.cotecom.control.events.cotacao.VerificaNovasRespostasEvent;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.cotacao.CotacaoDTO;
import br.com.cotecom.model.domain.cotacao.ItemCotacaoDTO;
import br.com.cotecom.model.domain.cotacao.ItemCotacaoDTO;
import br.com.cotecom.model.domain.dtos.AnaliseCotacaoDTO;
import br.com.cotecom.model.domain.dtos.ItemAnaliseCotacaoDTO;
import br.com.cotecom.model.domain.dtos.ItemAnaliseRespostaDTO;
import br.com.cotecom.model.domain.dtos.Produto;
import br.com.cotecom.model.domain.resposta.ItemResposta;
import br.com.cotecom.model.domain.tela.TelaCotacaoDTO;
import br.com.cotecom.view.screens.CotacaoNovaView;

import com.hydraframework.components.growler.model.GrowlDescriptor;
import com.hydraframework.components.growler.view.events.GrowlEvent;

import flash.events.Event;
import flash.events.TimerEvent;
import flash.utils.Timer;

import mx.collections.ArrayCollection;
import mx.core.Application;

[Bindable]
public class GerenciadorCotacao {

    private static var instance:GerenciadorCotacao;
    public var timerAnalise:Timer;

    public var telaCotacaoDTO:TelaCotacaoDTO;
    public var viewNovaCotacao:CotacaoNovaView;
    public var cotacaoSelecionada:CotacaoDTO;
    private var analiseCotacaoAberta:Boolean = false;
    private var session:Session = Session.getInstance();

    public function GerenciadorCotacao(enforcer:SingletonEnforcer){
        if(!enforcer is SingletonEnforcer)
            throw new Error("use the get instance function");
        this.telaCotacaoDTO = new TelaCotacaoDTO();
        this.cotacaoSelecionada = new CotacaoDTO();
        this.timerAnalise = new Timer(60000, 1);
        timerAnalise.addEventListener(TimerEvent.TIMER, verifiqueNovasRespostasOuSalveAnalise);

    }

    public static function getInstance():GerenciadorCotacao{
        if(instance == null)
            instance = new GerenciadorCotacao(new SingletonEnforcer());
        return instance;
    }

    public function limpeTelaNovaCotacao():void{
        this.telaCotacaoDTO = new TelaCotacaoDTO();
        this.viewNovaCotacao.limpeTela();
    }

    public function adicioneNovoProdutoACotacao(produto:Produto, quantidade:int, alvo:ArrayCollection):void{
        var addEvent:AdicioneProdutoACotacaoEvent = new AdicioneProdutoACotacaoEvent(produto, alvo,quantidade);
        addEvent.dispatch();
    }

    public function clearCotacaoSelecionada():void{
        this.cotacaoSelecionada = new CotacaoDTO();
    }

    public function monitoreAnaliseSelecionada():void {
        analiseCotacaoAberta = true;
        this.timerAnalise.start();
    }

    private function verifiqueNovasRespostasOuSalveAnalise(event:Event):void {
        if (cotacaoSelecionada.analise != null) {
            if(cotacaoSelecionada.analise.cotacao.isEmAnalise()){
                salveItensAnalise()
            }else if(!cotacaoSelecionada.analise.cotacao.isAguardandoRespostas()){
                new VerificaNovasRespostasEvent(cotacaoSelecionada.id).dispatch();
            }
        }else{
            resetTimerAnalise();
        }
    }

    private function salveItensAnalise():void {
        if(cotacaoSelecionada.analise.alterada){
            new SaveEvent(SaveEvent.ANALISE_COTACAO, 1).dispatch()
        }else{
            resetTimerAnalise()
        }
    }

    private function atualizeCotacao(event:Event):void {
        new LoadEvent(LoadEvent.ANALISE_COTACAO, this.cotacaoSelecionada.id).dispatch();
        resetTimerAnalise()
    }

    public function crieNotificacaoNovaRespostaDaCotacao():void {
        var appMain:CompradorMain = Application.application as CompradorMain;
        appMain.growlerNovaRespostaDaCotacao.removeEventListener(GrowlEvent.CLICK, atualizeCotacao);

        appMain.growlerNovaRespostaDaCotacao.closeFuntion = resetTimerAnalise;
        appMain.growlerNovaRespostaDaCotacao.addEventListener(GrowlEvent.CLICK, atualizeCotacao);

        appMain.growlerNovaRespostaDaCotacao.x = (appMain.width-220);
        appMain.growlerNovaRespostaDaCotacao.growl(new GrowlDescriptor("Nova Resposta Desta Cotacao Alterada",
                "Deseja atualizar a cotacao?",
                "Sim",
                0xFFCA51));
    }

    public function resetTimerAnalise(evt:Event = null):void {
        if(analiseCotacaoAberta){
            this.timerAnalise.reset();
            this.timerAnalise.start();
        }
    }

    public function pareMonitoramento():void {
        analiseCotacaoAberta = false;
        this.timerAnalise.stop();
    }

    public function crieNovaCotacaoRascunho():void {
        limpeTelaNovaCotacao();
        new CrieNovaCotacaoRascunhoEvent().dispatch();
    }

    public function atualizeItemCotacaoDTO(itemAAtualizar:ItemCotacaoDTO):void {
        for each(var item:ItemCotacaoDTO in this.telaCotacaoDTO.itensCotacaoDTO){
            if(item.id == itemAAtualizar.id){
                item.saved = true
            }
        }
    }
}
}
class SingletonEnforcer{};