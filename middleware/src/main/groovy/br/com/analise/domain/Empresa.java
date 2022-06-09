package br.com.analise.domain;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document
public class Empresa implements Serializable{
	
	private static final long serialVersionUID = 362964345794301790L;

	@Id 
	public String id;

    @NotBlank
    public String nomeResponsavel;
    public String email;
    public List<String> telefones;
    public String nomeFantasia;
	
	/**
	 * IDs do cliente
	 */
    @NotNull
    public Long idRepresentante;
	public Long idEmpresa;
}
