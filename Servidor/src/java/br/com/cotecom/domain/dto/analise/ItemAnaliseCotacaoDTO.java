package br.com.cotecom.domain.dto.analise;

import java.util.List;

public class ItemAnaliseCotacaoDTO {

    public String id;
    public List<ItemAnaliseRespostaDTO> respostas;
    public String descProduto;
    public int quantidade;
    public Boolean naoComprar;
    public String embalagem;
    public Boolean saved = true;

}