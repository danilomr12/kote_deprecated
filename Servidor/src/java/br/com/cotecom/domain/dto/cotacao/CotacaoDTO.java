package br.com.cotecom.domain.dto.cotacao;

import br.com.cotecom.domain.dto.usuario.EnderecoDTO;

import java.io.Serializable;
import java.util.Date;

public class CotacaoDTO implements Serializable {

    //ID Cotacao
    public Long id;
    public Integer version;

    //Cabeçalho
    public String titulo;
    public String mensagem;
    public Date dataCriacao;
    public Date dataEntrega;
    public Date dataValidade;
    public Date dataSalva;
    public String prazoPagamento;
    public Integer codigoEstadoCotacao;
    public Long compradorId;
    public Long empresaId;
    public EnderecoDTO enderecoEntrega;

}
