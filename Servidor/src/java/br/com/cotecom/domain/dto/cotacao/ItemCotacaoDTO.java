package br.com.cotecom.domain.dto.cotacao;

import java.io.Serializable;

public class ItemCotacaoDTO implements Serializable {

    //ID
    public Long id;

    //Item ICotacao
    public Long cotacaoId;
    public Long produtoId;
    public String descricao;
    public String categoria;
    public String embalagem;
    public Integer quantidade;
    public String qtdMaster;
    public boolean saved;

}
