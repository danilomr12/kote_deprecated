package br.com.cotecom.model.domain.cotacao
{
import br.com.cotecom.model.domain.Endereco;
import br.com.cotecom.model.domain.dtos.AnaliseCotacaoDTO;
import br.com.cotecom.model.utils.EstadoCotacao;

import mx.collections.ArrayCollection;

[RemoteClass(alias="br.com.cotecom.domain.dto.cotacao.CotacaoDTO")]
[Bindable]
public class CotacaoDTO{

    public var id:*;
    public var version:*;
    public var titulo:String;
    public var mensagem:String;
    public var dataCriacao:Date;
    public var dataValidade:Date;
    public var dataEntrega:Date;
    public var dataSalva:Date;
    public var prazoPagamento:String;
    public var codigoEstadoCotacao:int;
    public var compradorId:String;
    public var enderecoEntrega:Endereco;
    public var pedidos:ArrayCollection;
    public var empresaId:*;

    public var analise:AnaliseCotacaoDTO;
    public var alterada:Boolean;

    public function CotacaoDTO(compradorId:String = null, titulo:String = "", mensagem:String = "",  dataCriacao:Date = null,
                               validade:Date = null, prazoDeEntrega:Date = null, enderecoEntrega:Endereco = null,
                               codigoEstadoCotacao:int = 0, prazoDePagamento:String = null, id:* = null,
                               analise:AnaliseCotacaoDTO = null){
        this.compradorId = compradorId;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.dataCriacao = dataCriacao;
        this.dataValidade = validade;
        this.dataEntrega = prazoDeEntrega;
        this.codigoEstadoCotacao = codigoEstadoCotacao;
        this.enderecoEntrega = enderecoEntrega;
        this.prazoPagamento = prazoDePagamento;
        this.analise = analise;
        this.id = id;
    }

    public function isEditavel():Boolean{
        return this.codigoEstadoCotacao == EstadoCotacao.RASCUNHO ||
                this.codigoEstadoCotacao == EstadoCotacao.EM_ANALISE
    }

    public function isFinalizada():Boolean {
        return this.codigoEstadoCotacao == EstadoCotacao.FINALIZADA;
    }

    public function isAguardandoRespostas():Boolean {
        return this.codigoEstadoCotacao == EstadoCotacao.AGUARDANDO_RESPOSTAS;
    }
    public function isEmAnalise():Boolean {
        return this.codigoEstadoCotacao == EstadoCotacao.EM_ANALISE;
    }
}
}