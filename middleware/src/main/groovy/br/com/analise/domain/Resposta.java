package br.com.analise.domain;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class Resposta implements Serializable{
	
	private static final long serialVersionUID = 6219996719917923196L;
	
	public Double preco;
 	public String observacao;

 	@DBRef
 	public RespostaCompra respostaCompra;

 	/**
 	 * IDs do cliente
 	 */
 	public Long idItemResposta;
}
