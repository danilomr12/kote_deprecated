package br.com.cotecom.domain.dto.tela;

import br.com.cotecom.domain.dto.pedido.ItemPedidoDTO;
import br.com.cotecom.domain.dto.pedido.PedidoDTO;
import br.com.cotecom.domain.dto.usuario.UsuarioDTO;

import java.util.List;

public class TelaPedidoDTO {

    public PedidoDTO pedido;
    public List<ItemPedidoDTO> itensPedido;
    public UsuarioDTO comprador;

}
