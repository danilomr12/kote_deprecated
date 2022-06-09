package br.com.cotecom.domain.dto.resposta;

import br.com.cotecom.domain.dto.produto.ProdutoDTO;

import java.math.BigDecimal;

public class ItemRespostaDTO {

    //ID
    public Long id;
    public Long version;

    //Item IResposta
    public Integer respostaId;
    public ProdutoDTO produtoAlternativo;
    public String observacao;
    public BigDecimal precoEmbalagem;
    public Integer quantidade;
    public Integer produtoId;
    public String descricao;
    public String embalagem;
    public int qtdEmbalagem;
    public String categoria;
    public Boolean saved = true;
    
}
