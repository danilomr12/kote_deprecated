package br.com.cotecom.model.domain.dtos {
import br.com.cotecom.view.util.DinheiroUtil;

[Bindable]
[RemoteClass(alias="br.com.cotecom.domain.dto.analise.ItemAnaliseRespostaDTO")]
public class ItemAnaliseRespostaDTO {

    public var id:*;
    public var embalagem:String;
    public var idRepresentante:*;
    public var descricaoRepresentante:String;
    public var precoAtribuido:Number;
    public var observacao:String;
    public var idRespostaCompra:String;

    public function ItemAnaliseRespostaDTO(idRepresentante:* = null, descRepresentante:String = "",
                                           precoAtribuido:Number = 0, observacao:String = null)
    {
        this.idRepresentante = idRepresentante;
        this.descricaoRepresentante = descRepresentante;
        this.precoAtribuido = precoAtribuido;
        this.observacao = observacao;
    }

    public function get precoEmbalagem():String {
        if(precoAtribuido == 0)
            return "Não Respondido";
        return DinheiroUtil.formatAsReal(this.precoAtribuido);
    }

    public function get precoUnit():Number{
        if(qtdEmbalagem != 0){
            return this.precoAtribuido/this.qtdEmbalagem;
        }
        return 1;
    }

    public function set precoUnit(preco:Number):void{
        if(qtdEmbalagem != 0 && preco){
            this.precoAtribuido = preco*this.qtdEmbalagem;
        }
    }

    public function get precoEmb():Number{
        return this.precoAtribuido
    }

    public function set precoEmb(preco:Number):void{
        if(preco)
            this.precoAtribuido = preco;
    }

    public function get precoUnitario():String {
        if(precoAtribuido == 0)
            return "Não Respondido";
        if(this.qtdEmbalagem)
            return DinheiroUtil.formatAsReal(this.precoAtribuido/this.qtdEmbalagem);
        return " - ";
    }

    public function get qtdEmbalagem():int{
        if(this.embalagem.length==10){
            return Number(this.embalagem.substring(3,7))
        }
        return 0;
    }
}
}
