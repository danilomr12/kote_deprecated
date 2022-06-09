package br.com.analise.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Compra implements Serializable {
	
	private static final long serialVersionUID = -1857472931225887195L;

	@Id 
	String id; // ID da compra/cotacao
    
    /**
     * Dados da compra
     */
	public String titulo;
	public String mensagem;
    public Date dataCriacao;
    public Date dataEntrega;
    public Date dataValidade;
    public Date dataSalva;
    public String prazoPagamento;
    public int estado;
    public String endereco;
    public Double total;

    @NotNull
    public Long compradorId;
    @NotNull
    public Long empresaId;

	/**
	 * Itens sendo comprados
	 * Itens sendo comprados
	 */
	@NotEmpty
	@DBRef
	public List<Item> itens;

	/**
	 * IDs cliente
	 */
	public Long idCotacao; // ID Cotacao

    /**
     * Dados respostas e fornecedores
     */
    @DBRef
    public List<RespostaCompra> respostasCompra;

    public String getId(){
        return this.id;
    }
}
