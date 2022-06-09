package br.com.cotecom.domain.dto.inbox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CotacaoItemInboxDTO implements ItemInboxDTO{

    public String id;
	public String nome;
	public String estado;
    public String estadoCount;
	public Date dataSalva;
    public Date dataCriacao;
	public String titulo;

    public List<RespostaItemInboxDTO> respostas = new ArrayList<RespostaItemInboxDTO>();

}