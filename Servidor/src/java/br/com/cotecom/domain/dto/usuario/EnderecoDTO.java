package br.com.cotecom.domain.dto.usuario;

import java.io.Serializable;

public class EnderecoDTO implements Serializable {

    public Long id;
    public Long version;

    public String logradouro;
    public String complemento;
    public String bairro;
    public String cidade;
    public String estado;
    public String numero;
    public String cep;

}
