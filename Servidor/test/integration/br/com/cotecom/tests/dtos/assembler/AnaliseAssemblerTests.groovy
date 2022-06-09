package br.com.cotecom.tests.dtos.assembler

import br.com.cotecom.domain.analise.Analise
import br.com.cotecom.domain.analise.ItemAnalise
import br.com.cotecom.domain.analise.ItemRespostaFact
import br.com.cotecom.domain.dto.analise.AnaliseCotacaoDTO
import br.com.cotecom.dtos.assembler.AnaliseAssembler
import br.com.cotecom.services.CotacaoService
import br.com.cotecom.util.fixtures.CotacaoFixture
import grails.test.GrailsUnitTestCase

public class AnaliseAssemblerTests extends GrailsUnitTestCase {

    CotacaoService cotacaoService
    Analise analisePersistida
    AnaliseCotacaoDTO analiseDTO

    void setUp() {
		def cotacao = CotacaoFixture.crieCotacaoRespondida()
        analisePersistida = cotacaoService.analisar(cotacao.id)
        analiseDTO = AnaliseAssembler.crieAnaliseCotacaoDTO(analisePersistida)
        super.setUp()
    }

    void testCrieAnaliseParaPersistir() {
        //reverte a ordem das respostas
        analiseDTO.itensAnaliseCotacao.each {
            it.respostas = it.respostas.reverse()
        }

        Analise analiseParaPersistir = AnaliseAssembler.crieAnalise(analiseDTO)

        assertNotNull analiseParaPersistir
        assertTrue "A ordenação das respostas não corresponde com a esperada" ,
                isAnaliseOrdenadaCorretamente(analiseDTO, analiseParaPersistir)
    }

    def isAnaliseOrdenadaCorretamente(AnaliseCotacaoDTO analiseDTO, Analise analisePersistivel){
        def ordenadaCorretamente = true
        //itera entre as respostas e verifica se os ids das analises sao iguais
        analisePersistivel.itens.eachWithIndex { ItemAnalise analiseItem, int itemIndex  ->
            analiseItem.itensRespostaFact.eachWithIndex { ItemRespostaFact respostaPosicaoDuvidosa , int respIndex ->
                if(respostaPosicaoDuvidosa.precoEmbalagem){
                    def respostaPosicaoCorreta = analiseDTO.itensAnaliseCotacao.asList().getAt(itemIndex).respostas.asList().getAt(respIndex)
                    if(respostaPosicaoCorreta.id != respostaPosicaoDuvidosa.id)
                        ordenadaCorretamente = false
                }
            }
        }
        return ordenadaCorretamente        
    }

}