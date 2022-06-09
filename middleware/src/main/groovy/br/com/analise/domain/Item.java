package br.com.analise.domain;

import java.io.Serializable;
import java.util.List;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotNull;

@Document
public class Item implements Serializable{
	
	private static final long serialVersionUID = 4248063507507623680L;

	@Id 
	public String id;
    
    /**
     * Dados do produto
     */
    @NotNull
    public String descricao;
    public String embalagem;

    /**
     * por enquanto nao usados para nada
     */
    @NotNull
    public Long idProduto;
    public String codigoBarras;
    public String categoria;
    public String fabricante;
    public String marca;
    public String peso;
    
    /**
     * Dados da compra
     */
    @NotNull
    public Integer quantidade;
    public Boolean naoComprar;
    public List<Resposta> respostas;

 	/**
 	 * IDs cliente
 	 */
     @NotNull
    public Long idItemCompra; // ID Item cotacao

}
