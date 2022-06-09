package br.com.cotecom.domain.resposta

import br.com.cotecom.domain.item.ItemResposta
import br.com.cotecom.domain.pedido.Pedido

public interface IResposta {

    boolean addItemResposta(ItemResposta itemResposta)
    boolean addPedido(Pedido pedido)

    void mudeEstadoPara(Integer integer)
    boolean envie()
    void descarte()
    boolean cancele()
    void recuse()
    void aceite()
    Boolean faturePedido()
    IResposta ressuscite()

    IResposta salve()

    String criePlanilhaParaPreenchimentoOffLine()
    def importePlanilhaResposta(Resposta resposta, String pathPlanilha)

    double calculePrecoPedido()
    int calculeNumItensFaltantes()
}