package br.com.cotecom.domain.dto.tela;

import br.com.cotecom.domain.dto.cotacao.CotacaoDTO;
import br.com.cotecom.domain.dto.cotacao.ItemCotacaoDTO;

import java.io.Serializable;
import java.util.List;

public class TelaCotacaoDTO implements Serializable{

    public CotacaoDTO cotacaoDTO;
    public List<Long> representantesId;
    public List<ItemCotacaoDTO> itensCotacaoDTO;


}
