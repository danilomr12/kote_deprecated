package br.com.cotecom.domain.dto.resposta;

import java.util.Date;
import java.util.List;

public class RespostaDTO {

    //ID
    public Long id;

    //Cabeçalho
    public Date dataCriacao;
    public Date dataSalva;
    public Date dataValidade;
    public Integer status;
    public Integer cotacaoId;
    public String representanteId;
    public String clienteNomeFantasia;
    public List pedidosDTOs;
    public String clienteRazaoSocial;
    
}
