package br.com.cotecom.model.domain.resposta{
import br.com.cotecom.model.domain.dtos.Produto;

[RemoteClass(alias="br.com.cotecom.domain.dto.resposta.ItemRespostaDTO")]
[Bindable]
public class ItemResposta{

    public var quantidade:int;
    public var precoEmbalagem:Number;
    public var embalagem:String;
    public var descricao:String;
    public var categoria:String;
    public var observacao:String;
    private var _produtoAlternativo:Produto = null;
    public var produtoId:*;
    public var respostaId:*;
    public var version:*;
    public var id:*;
    public var qtdEmbalagem:int;
    public var saved:Boolean;
    public var qtdPedido:int;

    public function ItemResposta(
            quantidade:int = 0,
            precoUnitario:Number = 0,
            precoEmbalagem:Number = 0,
            embalagem:String = "",
            descricao:String = "",
            categoria:String = "",
            produtoId:* = null,
            saved:Boolean = false,
            id:* = null){

        this.quantidade = quantidade;
        this.precoEmbalagem = precoEmbalagem;
        this.embalagem = embalagem;
        this.descricao = descricao;
        this.categoria = categoria;
        this.produtoId = produtoId;
        this.id = id;
        this.saved = saved;
    }

    public function set produtoAlternativo(prodAlternativo:Produto):void{
        this._produtoAlternativo = prodAlternativo;
        if(this._produtoAlternativo != null){
            this.observacao = "O preço informado é de um produto diferente do solicitado.\n" +
                    this._produtoAlternativo.toString()+"."
        }else{
            this.observacao = null;
        }
    }

    public function get produtoAlternativo():Produto{
        return this._produtoAlternativo;
    }

    public function equalsId(outro:ItemResposta):Boolean{
        if(outro){
            return this.id == outro.id;
        }
        return false;
    }

    public function equals(outro:ItemResposta):Boolean{
        return outro
                && (this.descricao == outro.descricao)
                && (this.embalagem == outro.embalagem)
                && (this.precoEmbalagem == outro.precoEmbalagem)
                && (this.quantidade == outro.quantidade)
                && (this.categoria == outro.categoria)
                && (this.produtoId == outro.produtoId);

    }

    public function setSaved():void{
        this.saved = true;
    }

    public function setNotSaved():void {
        this.saved = false;
    }

    public function get precoUnitario():Number{
        return this.precoEmbalagem/this.qtdEmbalagem;
    }

    public function set precoUnitario(preco:Number):void{
        this.precoEmbalagem = preco*this.qtdEmbalagem
    }
}
}