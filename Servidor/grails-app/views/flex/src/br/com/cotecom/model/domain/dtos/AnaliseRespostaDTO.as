package br.com.cotecom.model.domain.dtos {

import br.com.cotecom.model.domain.dtos.usuarios.Usuario;
import br.com.cotecom.model.utils.EstadoResposta;
import br.com.cotecom.view.util.DinheiroUtil;

[Bindable]
[RemoteClass(alias="br.com.cotecom.domain.dto.analise.AnaliseRespostaDTO")]
public class AnaliseRespostaDTO {

    public var id:*;
	public var estado:int;
    public var representanteId:int;
    public var nomeRepresentante:String;
	public var analise:AnaliseCotacaoDTO;
    public var empresaNomeFantasia:String;

    public function AnaliseRespostaDTO(){}

	public function get valorTotalFormatado():String {
		return DinheiroUtil.formatAsReal(this.valorTotal);
	}

    public function get valorTotal():Number {
		var valorTotal:Number = 0;
		for each(var analiseItem:ItemAnaliseCotacaoDTO in analise.itensAnaliseCotacao){
			if(analiseItem.respostas.length != 0 && !analiseItem.naoComprar){
				var respostaItem:ItemAnaliseRespostaDTO = analiseItem.respostas.getItemAt(0) as ItemAnaliseRespostaDTO;
				if(respostaItem.idRepresentante == this.representanteId){
					valorTotal = valorTotal + (respostaItem.precoAtribuido * analiseItem.quantidade);
				}
			}
		}
		return valorTotal;
	}

    public function get mixDeProdutos():Number {
        var mix:Number = 0;
        for each(var analiseItem:ItemAnaliseCotacaoDTO in analise.itensAnaliseCotacao){
			if(analiseItem.respostas.length != 0 && !analiseItem.naoComprar){
				var respostaItem:ItemAnaliseRespostaDTO = analiseItem.respostas.getItemAt(0) as ItemAnaliseRespostaDTO;
				if(respostaItem.idRepresentante == this.representanteId){
					mix = mix + 1;
				}
			}
		}
		return mix;
    }

	public function get descricaoEstadoVisaoComprador():String {
		return EstadoResposta.getDescricaoVisaoComprador(this.estado);
	}

    public function get descricaoEstadoVisaoRepresentante():String {
		return EstadoResposta.getDescricaoVisaoRepresentante(this.estado);
	}
}
}