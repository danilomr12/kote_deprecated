package br.com.cotecom.tests.services.remote

import br.com.cotecom.domain.analise.Analise
import br.com.cotecom.domain.cotacao.Cotacao
import br.com.cotecom.domain.cotacao.ICotacao
import br.com.cotecom.domain.pedido.Pedido
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.empresa.Fornecedor
import br.com.cotecom.domain.usuarios.empresa.Telefone
import br.com.cotecom.dtos.assembler.AnaliseAssembler
import br.com.cotecom.services.remote.RemoteCotacaoService
import br.com.cotecom.util.fixtures.CotacaoFixture
import grails.test.GrailsUnitTestCase

public class RemoteCotacaoServiceTests extends GrailsUnitTestCase {

    RemoteCotacaoService remoteCotacaoService

    void testEnvieCotacaoJaRespondidaParaFornecedor() {
        def idCotacaoRespondida = CotacaoFixture.crieCotacaoRespondida().id
        def idRepresentante = Representante.build().id
        remoteCotacaoService.envieCotacaoParaRepresentante(idCotacaoRespondida, idRepresentante)
        assertTrue "Nao foi encontrada resposta para representante de id=$idRepresentante",
                representantePossuiRespostaCadastrada(Cotacao.get(idCotacaoRespondida), idRepresentante)
    }

    boolean representantePossuiRespostaCadastrada(ICotacao cotacao, def idRepresentante) {
        return cotacao.respostas.find {
            it.representante.id == idRepresentante
        } as boolean
    }

    void testGerepedidosDeCotacaoComRepresentanteSemFornecedor() {
        Analise analisePersistida = CotacaoFixture.crieCotacaoAnalisada().analise
        Representante representante = (analisePersistida.cotacao.respostas.asList().get(0) as Resposta).representante
        Fornecedor fornecedor = representante.empresa
        fornecedor.removeFromUsuarios(representante)
        fornecedor.save(flush:true)
        List<Pedido> pedidos = remoteCotacaoService.gerePedidosEEnvieParaRepresentantes(AnaliseAssembler.crieAnaliseCotacaoDTO(analisePersistida))
        assertNotNull pedidos
        assertEquals analisePersistida.cotacao.respostas.size(), pedidos.size()
    }

    private String formateTelefones(List telefones) {
        String result = ""
        telefones.eachWithIndex {Telefone telefone, int index ->
            result += "${telefone.toString()}"
            if (index < telefones.size() - 1)
                result = +" / "
        }
        return result
    }

    private String formateData(Date data) {
        return String.format('%td/%<tm/%<tY', data)
    }

}