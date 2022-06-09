package br.com.cotecom.domain.dto.usuario;

import java.util.List;

public class EmpresaDTO {

    public static int CLIENTE = 0;
    public static int FORNECEDOR = 1;

    public Long id;
    public Long version;

    public String nomeFantasia;
    public String razaoSocial;
    public String email;
    public String cnpj;
    public List<TelefoneDTO> telefones;
    public EnderecoDTO endereco;
    public int tipo;

}
