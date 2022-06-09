package br.com.cotecom.model.domain.dtos {

import br.com.cotecom.control.events.cotacao.AtualizeRespostasEvent;
import br.com.cotecom.model.domain.cotacao.CotacaoDTO;
import br.com.cotecom.model.utils.EstadoCotacao;
import br.com.cotecom.model.utils.EstadoResposta;
import br.com.cotecom.view.util.DinheiroUtil;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="br.com.cotecom.domain.dto.analise.AnaliseCotacaoDTO")]
public class AnaliseCotacaoDTO {

    public var id:*;
    public var version:*;
    public var idCotacao:*;
    public var cotacao:CotacaoDTO;
    public var respostas:ArrayCollection = new ArrayCollection();
    public var itensAnaliseCotacao:ArrayCollection = new ArrayCollection();
    public var alterada:Boolean = false;

    public function AnaliseCotacaoDTO(){}

    public function get valorTotal(): String {
        var TotalValue:Number = new Number(0);
        for each(var analiseItem:ItemAnaliseCotacaoDTO in this.itensAnaliseCotacao){
            if(analiseItem.respostas.length != 0 && !analiseItem.naoComprar){
                var resposta:ItemAnaliseRespostaDTO = analiseItem.respostas.getItemAt(0) as ItemAnaliseRespostaDTO;
                TotalValue = TotalValue + (resposta.precoAtribuido * analiseItem.quantidade);
            }
        }
        return DinheiroUtil.formatAsReal(TotalValue);
    }

    public function atualizeRespostas():void{
        new AtualizeRespostasEvent(qtddRespostasConcluidas, idCotacao).dispatch();
    }

    public function get qtddRespostasConcluidas():int{
        var respConcluidas:int = 0;
        for each(var resposta:AnaliseRespostaDTO in respostas){
            if(resposta.estado == EstadoResposta.PEDIDO_FATURADO){
                respConcluidas++;
            }
        }
        return respConcluidas;
    }

    public function get respostasAguardadas():ArrayCollection {
        var respAguardadas:ArrayCollection = new ArrayCollection();
        for each(var resposta:AnaliseRespostaDTO in respostas){
            if(resposta.estado == EstadoResposta.NOVA_COTACAO ||
                    resposta.estado == EstadoResposta.RESPONDENDO){
                respAguardadas.addItem(resposta);
            }
        }
        return respAguardadas;
    }

    public function get totalDeItens():Number {
        return this.itensAnaliseCotacao.length;
    }

    public function get editavel():Boolean {
        return this.cotacao.codigoEstadoCotacao == EstadoCotacao.EM_ANALISE;
    }

    public function set editavel(edit:Boolean):void {
       trace("nothing")
    }

    public function setItensAsSaved(itens:ArrayCollection):void{
        for each(var item:ItemAnaliseCotacaoDTO in itens){
            for each(var itemASerEncontrado:ItemAnaliseCotacaoDTO in this.itensAnaliseCotacao){
                if(item.id == itemASerEncontrado.id)
                    itemASerEncontrado.setSaved();
            }
        }
    }

    public function getItensNaoSalvos():ArrayCollection{
        var unsavedItens:ArrayCollection = new ArrayCollection();
        for each(var item:ItemAnaliseCotacaoDTO in this.itensAnaliseCotacao){
            if(!item.saved){
                unsavedItens.addItem(item);
            }
        }
        return unsavedItens;
    }


    public function setItemAsUnsaved(itemAnaliseCotacaoId:*):void {
        this.alterada = true;
        for each(var item:ItemAnaliseCotacaoDTO in this.itensAnaliseCotacao){
            if(item.id == itemAnaliseCotacaoId){
                item.setUnsaved();
            }
        }
    }

    public function get totalDeFaltas():int {
        var totalFaltas:int = 0;
        for each(var item:ItemAnaliseCotacaoDTO in this.itensAnaliseCotacao){
            if(item.respostas == null || item.respostas.length <= 0
                    || (item.primeiraOpcao == null)
                    || item.naoComprar){
                totalFaltas++;
            }
        }
        return totalFaltas
    }
}
}