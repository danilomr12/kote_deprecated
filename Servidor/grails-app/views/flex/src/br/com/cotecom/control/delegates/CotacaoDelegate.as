package br.com.cotecom.control.delegates {
import br.com.cotecom.control.responders.cotacao.AdicioneItemCotacaoResponder;
import br.com.cotecom.control.responders.cotacao.CrieNovaCotacaoRascunhoResponder;
import br.com.cotecom.control.responders.cotacao.RemovaItemCotacaoResponder;
import br.com.cotecom.control.responders.cotacao.SaveItemCotacaoResponder;
import br.com.cotecom.control.responders.cotacao.VerificaNovasRespostasDaCotacaoResponder;
import br.com.cotecom.control.responders.cotacao.VerificaNovasRespostasResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.cotacao.ItemCotacaoDTO;
import br.com.cotecom.model.domain.dtos.AnaliseCotacaoDTO;
import br.com.cotecom.model.domain.tela.TelaCotacaoDTO;

import mx.collections.ArrayCollection;
import mx.rpc.AsyncToken;
import mx.rpc.IResponder;
import mx.rpc.remoting.RemoteObject;

public class CotacaoDelegate {

    public const SERVICE_FACADE_NAME:String = "remoteCotacaoService";
    private var remoteObject:RemoteObject;
    public var model:Session = Session.getInstance();

    public function CotacaoDelegate() {
        this.remoteObject = new RemoteObject(SERVICE_FACADE_NAME);
        this.remoteObject.showBusyCursor = true;
    }

    public function saveCotacao(responder:IResponder, cotacao:TelaCotacaoDTO):void {
        setAsyncToken(this.remoteObject.saveCotacao(cotacao), responder);
    }

    public function sendCotacao(responder:IResponder, cotacaoAEnviar:TelaCotacaoDTO):void {
        setAsyncToken(this.remoteObject.envieCotacao(cotacaoAEnviar), responder);
    }

    public function canceleCotacao(responder:IResponder, cotacaoACancelar:Object):void {
        setAsyncToken(this.remoteObject.cancele(cotacaoACancelar.id), responder);
    }

    public function finalizeCotacao(responder:IResponder, idCotacao:Number):void {
        setAsyncToken(this.remoteObject.finalizeCotacao(idCotacao), responder);
    }

    public function loadAnaliseCotacao(responder:IResponder, id:*):void {
        setAsyncToken(this.remoteObject.getAnaliseCotacao(id), responder);
    }

    public function analiseCotacao(responder:IResponder, id:*):void {
        setAsyncToken(this.remoteObject.analiseCotacao(id), responder);
    }

    public function salveItensAnaliseCotacao(responder:IResponder, itensAnaliseCotacaoDTO:ArrayCollection):void {
        setAsyncToken(this.remoteObject.saveItensAnaliseCotacao(itensAnaliseCotacaoDTO), responder);
    }

    public function loadCotacao(responder:IResponder, idCotacao:int):void {
        setAsyncToken(this.remoteObject.getCotacao(idCotacao), responder);
    }

    public function envieCotacaoParaFornecedor(responder:IResponder, idCotacao:*, idRepresentante:*):void {
        setAsyncToken(this.remoteObject.envieCotacaoParaRepresentante(idCotacao, idRepresentante), responder);
    }

    public function atualizeRespostas(responder:IResponder, qtddRespostasConcluidas:int, idCotacao:*):void {
        setAsyncToken(this.remoteObject.atualizeRespostas(idCotacao, qtddRespostasConcluidas), responder);
    }

    public function geraEEnviaPedidosAosRepresentantes(responder:IResponder, analise:AnaliseCotacaoDTO):void {
        var analiseIdEItensAnaliseNaoSalvos:Object = new Object();
        analiseIdEItensAnaliseNaoSalvos.analiseId = analise.idCotacao;
        analiseIdEItensAnaliseNaoSalvos.itensAnalise = analise.getItensNaoSalvos();
        setAsyncToken(this.remoteObject.gerePedidosEEnvieParaRepresentantes(analiseIdEItensAnaliseNaoSalvos), responder);
    }

    public function gereEEnviePedidos(responder:IResponder, analise:AnaliseCotacaoDTO):void {
        var cotacaoIdEItensAnaliseNaoSalvos:Object = {cotacaoId: analise.idCotacao, itensAnalise: analise.getItensNaoSalvos()};
        setAsyncToken(this.remoteObject.gerePedidos(cotacaoIdEItensAnaliseNaoSalvos), responder)
    }

    public function enviePedidosAosRepresentantes(responder:IResponder, analise:AnaliseCotacaoDTO):void {
        setAsyncToken(this.remoteObject.enviePedidosPrimeiraOrdem(analise.id), responder);
    }

    public function downloadPlanilhaAnaliseCotacao(responder:IResponder, idAnalise:Object):void {
        setAsyncToken(this.remoteObject.downloadPlanilhaAnaliseCotacao(idAnalise), responder);
    }

    public function reenvieEmailCotacao(responder:IResponder, idResposta:int):void {
        setAsyncToken(this.remoteObject.reenvieEmailCotacao(idResposta), responder);
    }

    public function possuiNovasRespostas(responder:VerificaNovasRespostasResponder):void {
        this.remoteObject.showBusyCursor = false;
        setAsyncToken(this.remoteObject.possuiNovasRespostas(), responder);
    }

    public function possuiNovasRespostasDaCotacao(responder:VerificaNovasRespostasDaCotacaoResponder, idCotacao:int):void {
        this.remoteObject.showBusyCursor = false;
        setAsyncToken(this.remoteObject.possuiNovasRespostasDaCotacao(idCotacao), responder);
    }

    public function crieNovaCotacaoRascunho(responder:CrieNovaCotacaoRascunhoResponder):void {
        setAsyncToken(this.remoteObject.crieNovaCotacaoRascunho(), responder);
    }

    private function setAsyncToken(object:AsyncToken, responder:IResponder):void {
        object.addResponder(responder);
    }

    public function adicioneItensCotacao(responder:AdicioneItemCotacaoResponder, itensCotacao:ArrayCollection):void {
        setAsyncToken(this.remoteObject.adicioneItemCotacao(itensCotacao), responder);
    }

    public function removaItemCotacao(responder:RemovaItemCotacaoResponder, itemCotacaoDTO:ItemCotacaoDTO):void {
        setAsyncToken(this.remoteObject.removaItemCotacao(itemCotacaoDTO), responder);
    }

    public function saveItemCotacao(responder:SaveItemCotacaoResponder, itemCotacaoDTO:ItemCotacaoDTO):void {
        setAsyncToken(this.remoteObject.saveItemCotacao(itemCotacaoDTO), responder);
    }
}
}