package br.com.cotecom.domain.dto.inbox;

import java.io.Serializable;
import java.util.Date;

public class RespostaItemInboxDTO implements Serializable {

    public String id;
    public String representante;
    public String empresa;
	public String status;
	public PedidoItemInboxDTO pedido;
	public FaltaItemInboxDTO falta;
    public Date dataSalva;
    
}