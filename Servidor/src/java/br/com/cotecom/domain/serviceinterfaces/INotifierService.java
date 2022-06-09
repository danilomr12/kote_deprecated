package br.com.cotecom.domain.serviceinterfaces;

import br.com.analise.domain.RespostaCompra;
import br.com.cotecom.domain.pedido.Pedido;

public interface INotifierService {

    boolean notifiquePedidoParaRepresentante(Pedido pedido, RespostaCompra respostaCompra, String nomeComprador, String emailComprador, Long idComprador, String nomeFantasiaCliente);

    void notifiqueNovaCotacao(String emailComprador, String nomeRepresentante, String emailRepresentante, Long respostaId,
                              String mensagem, String nomeFantasiaCliente);
}
