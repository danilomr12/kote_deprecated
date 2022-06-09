package br.com.cotecom.domain.cotacao

import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.empresa.Endereco
import br.com.cotecom.domain.usuarios.empresa.Cliente

public class CotacaoFactory {

	public static ICotacao crie(String titulo, String mensagem, Date dataEntrega, Date dataValidade, String prazoPagamento,
                               Cliente cliente, Endereco enderecoEntrega, Boolean persistida = true) {

        if(titulo == null || titulo.isEmpty())
            titulo = 'Rascunho'

        Cotacao cotacao = new Cotacao(titulo: titulo, mensagem: mensagem, dataCriacao: new Date(), dataEntrega:dataEntrega,
                dataValidade: dataValidade, prazoPagamento: prazoPagamento, empresa: cliente, dataSalva: new Date(),
                enderecoEntrega: enderecoEntrega?.save(flush:true), codigoEstadoCotacao: EstadoCotacao.RASCUNHO)

        if(persistida) cotacao.salve()
            return cotacao
	}
}