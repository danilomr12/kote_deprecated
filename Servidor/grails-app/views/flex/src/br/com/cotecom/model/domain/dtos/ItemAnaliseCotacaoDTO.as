package br.com.cotecom.model.domain.dtos{


import br.com.cotecom.model.domain.resposta.ItemResposta;
import br.com.cotecom.model.domain.resposta.Resposta;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="br.com.cotecom.domain.dto.analise.ItemAnaliseCotacaoDTO")]
public class ItemAnaliseCotacaoDTO {

    public var id:*;
    public var respostas:ArrayCollection = new ArrayCollection();
    public var descProduto:String;
    public var quantidade:int;
    public var naoComprar:Boolean;
    public var embalagem:String;
    public var saved:Boolean;

    public function ItemAnaliseCotacaoDTO() {}

    public function isItemEscolhidoOMaisBarato():Boolean {

        if(respostas.length == 0 || respostas.length == 1)
            return true;

        var respostaEscolhida:ItemAnaliseRespostaDTO = respostas.getItemAt(0) as ItemAnaliseRespostaDTO;
        for each(var resposta:ItemAnaliseRespostaDTO in respostas){
            if(respostaEscolhida.precoAtribuido > resposta.precoAtribuido){
                return false;
            }
        }
        return true;
    }

    public function  get diferencaEntrePrimeiroESegundoPrecos():Number{
        if (respostas.length > 1 && this.primeiraOpcao != null &&
                this.segundaOpcao.precoEmbalagem != "Não Respondido" ) {
            var precoAtribuido:Number = this.segundaOpcao.precoAtribuido;
            var precoAtribuido2:Number = this.primeiraOpcao.precoAtribuido;
            var number:Number = (precoAtribuido/precoAtribuido2)*100-100;
            return number;
        } else
            return 0;
    }

    public function get possuiPrecoDistuante():Boolean{
        var mediana:Number = calculeMediana();
        for each(var itemResposta:ItemAnaliseRespostaDTO in respostas){
            if(itemResposta.precoEmbalagem != "Não Respondido" && estaDistuante(itemResposta, mediana)){
                return true;
            }
        }
        return false;
    }

    private function estaDistuante(itemResposta:ItemAnaliseRespostaDTO, mediana:Number):Boolean {
        if(itemResposta.precoEmbalagem == "Não Respondido")
            return false;

        var difPercentual:Number = (itemResposta.precoAtribuido/ mediana)*100 - 100;
        return Math.abs(difPercentual) > 30;
    }

    private function calculeMediana():Number {
        var mediana:Number = 0;
        var respostasLength:int = 0;
        for each(var resposta:ItemAnaliseRespostaDTO in respostas){
            if(resposta.precoEmbalagem != "Não Respondido"){
                respostasLength ++;
            }
        }
        if(respostasLength < 1){
            return 0;
        }else if (respostas.length % 2 == 0) {
            var preco1:Number = (respostas.getItemAt((respostasLength / 2) - 1) as ItemAnaliseRespostaDTO).precoAtribuido;
            var preco2:Number = (respostas.getItemAt((respostasLength / 2)) as ItemAnaliseRespostaDTO).precoAtribuido;
            mediana = (preco1 + preco2) / 2
        } else {
            mediana = (respostas.getItemAt((respostasLength - 1) / 2) as ItemAnaliseRespostaDTO).precoAtribuido
        }
        return mediana;
    }

    public function get primeiraOpcao():ItemAnaliseRespostaDTO{
        if(respostas != null && respostas.length > 0 ){
            var precoAtribuido:String = respostas.getItemAt(0).precoEmbalagem;
            var b:Boolean = precoAtribuido != "Não Respondido";
            if(b)
                return ItemAnaliseRespostaDTO(respostas.getItemAt(0));
        }
        return null
    }

    public function get segundaOpcao():ItemAnaliseRespostaDTO{
        if(respostas.length <= 1) return null;
        var segundaOpcao:ItemAnaliseRespostaDTO = ItemAnaliseRespostaDTO(respostas.getItemAt(1));
        if(respostas.length == 2) return segundaOpcao;
        /*for (var i:int = 2; i < respostas.length ; i++) {
            var resposta:ItemAnaliseRespostaDTO = ItemAnaliseRespostaDTO(respostas.getItemAt(i));
            if(segundaOpcao.precoAtribuido > resposta.precoAtribuido){
                segundaOpcao = resposta;
            }
        }*/
        return segundaOpcao;
    }

    public function get qtdEmbalagem():int{
        if(this.embalagem.length==10){
            return Number(this.embalagem.substring(3,7))
        }
        return 0;
    }

    public function precoEstaDistuante(itemAnaliseRespostaDTO:ItemAnaliseRespostaDTO):Boolean {
        return estaDistuante(itemAnaliseRespostaDTO, calculeMediana());
    }

    public function setSaved():void {
        this.saved = true;
    }

    public function setUnsaved():void {
        this.saved = false;
    }
}
}