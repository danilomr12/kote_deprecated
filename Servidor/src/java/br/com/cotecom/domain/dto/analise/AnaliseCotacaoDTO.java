package br.com.cotecom.domain.dto.analise;

import br.com.cotecom.domain.dto.cotacao.CotacaoDTO;

import java.util.List;

public class AnaliseCotacaoDTO {

    public String id;
    public Long idCotacao;
    public Integer version;
    public List<ItemAnaliseCotacaoDTO> itensAnaliseCotacao;
	public List<AnaliseRespostaDTO> respostas;
    public CotacaoDTO cotacao;
	public boolean editavel;

}