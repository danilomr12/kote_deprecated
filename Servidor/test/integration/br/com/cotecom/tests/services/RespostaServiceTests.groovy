package br.com.cotecom.tests.services

import br.com.cotecom.domain.cotacao.ICotacao
import br.com.cotecom.domain.item.ItemCotacao
import br.com.cotecom.domain.item.ItemResposta
import br.com.cotecom.domain.item.Preco
import br.com.cotecom.domain.resposta.EstadoResposta
import br.com.cotecom.domain.resposta.IResposta
import br.com.cotecom.domain.resposta.Resposta

import br.com.cotecom.domain.usuarios.empresa.Endereco
import br.com.cotecom.domain.usuarios.empresa.Telefone
import br.com.cotecom.services.RespostaService
import br.com.cotecom.util.Path.Path
import br.com.cotecom.util.documents.excel.Celula
import br.com.cotecom.util.documents.excel.ExcelFile
import br.com.cotecom.util.fixtures.CotacaoFixture
import br.com.cotecom.util.test.RespostaUnitTestCase
import java.math.MathContext
import java.math.RoundingMode
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.DataFormat
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import br.com.cotecom.services.DataService
import br.com.cotecom.domain.resposta.RespostaAguardandoOutrasRespostas

public class  RespostaServiceTests extends RespostaUnitTestCase {

    RespostaService respostaService
    DataService dataService

    void testGetResposta() {
        cotacao.respostas.each { Resposta resposta ->
            Resposta respostaFromDigest = respostaService.getRespostaByDigest(resposta.respostaUrlDigest)
            assertEquals resposta, respostaFromDigest
        }

        String urlDigest = "DIGEST_INVALIDO"
        IResposta respostaDigestInvalido = respostaService.getRespostaByDigest(urlDigest)
        assertNull respostaDigestInvalido

    }

    void testSaveResposta() {
        IResposta resposta = cotacao.respostas.asList().get(0)
        IResposta respostaDigest = respostaService.getRespostaByDigest(resposta.respostaUrlDigest)
        assertTrue respostaService.aceite(respostaDigest)

        ItemResposta item = respostaDigest.itens.asList().get(0)
        item.preco = new Preco(unitario: 3f)
        item.preco = new Preco(embalagem: 27f)
        assertTrue respostaService.salve(respostaDigest)

        IResposta respostaAfterSave = respostaService.getRespostaByDigest(resposta.respostaUrlDigest)
        respostaAfterSave.itens.each { ItemResposta itemResposta ->
            if(itemResposta.equals(item)){
                assertEquals item.preco, itemResposta.preco
                assertEquals item.preco.embalagem, itemResposta.preco.embalagem
                assertEquals item.preco.unitario, itemResposta.preco.unitario
            }
        }
    }

    void testCursoCorretoDeResposta(){
        List<String> digests = new ArrayList<String>()
        cotacao.respostas.each {IResposta resposta ->
            digests.push resposta.respostaUrlDigest
        }
        digests.each {String digest ->
            IResposta resposta = respostaService.getRespostaByDigest(digest)
            assertNotNull resposta
            assertEquals EstadoResposta.NOVA_COTACAO, resposta.codigoEstado
            assertTrue respostaService.aceite(resposta)
            assertEquals EstadoResposta.RESPONDENDO, resposta.codigoEstado
            resposta.itens.each {ItemResposta item ->
                def precoEmbalgem = 3 * 12
                item.preco = new Preco(embalagem: precoEmbalgem)
                item.preco = new Preco(unitario: 3)
                assertTrue respostaService.salveItem(item)
            }
            assertTrue respostaService.salve(resposta)
            resposta = respostaService.getRespostaByDigest(digest)
            assertNotNull resposta
            assertEquals EstadoResposta.RESPONDENDO, resposta.codigoEstado
            assertTrue respostaService.envie(resposta)
            assertEquals EstadoResposta.PEDIDO_FATURADO, resposta.codigoEstado

        }

    }

    void testCriePlanilhaExcelCotacaoParaPreenchimentoDeRespostaOffline(){
        cotacao = CotacaoFixture.crieCotacaoAguardandoRespostas()
        Resposta resposta = cotacao.respostas.asList().get(0) as Resposta
        String pathplanilha = respostaService.criePlanilhaCotacaoParaPreenchimentoOffLine(resposta)

        ExcelFile excelFile = new ExcelFile(pathplanilha)
        Sheet planilha1 = excelFile.getPlanilha(0)

        int indicePrimeiraLinhaPlanilha = 1

        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                cotacao.titulo + " - " + formateData(cotacao.dataCriacao)
        assertTrue planilha1.getRow(indicePrimeiraLinhaPlanilha).getCell(0).getCellStyle().locked
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                "Prazo de pagamento - " + cotacao.prazoPagamento + " dias"
        assertTrue planilha1.getRow(indicePrimeiraLinhaPlanilha).getCell(0).getCellStyle().locked
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                "Data de entrega - " + formateData(cotacao.dataEntrega)
        assertTrue planilha1.getRow(indicePrimeiraLinhaPlanilha).getCell(0).getCellStyle().locked
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                "Validade da cotacao - " + formateData(cotacao.dataValidade)
        assertTrue planilha1.getRow(indicePrimeiraLinhaPlanilha).getCell(0).getCellStyle().locked
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                "Endereço de entrega - " + formateEnderecoPrimeiraCelula(cotacao)
        assertTrue planilha1.getRow(indicePrimeiraLinhaPlanilha).getCell(0).getCellStyle().locked
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,1,indicePrimeiraLinhaPlanilha),
                fomateEnderecoSegundaCelula(cotacao)
        assertTrue planilha1.getRow(indicePrimeiraLinhaPlanilha).getCell(1).getCellStyle().locked
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                "${resposta.representante.nome} / ${resposta.representante.empresa.nomeFantasia}"
        assertTrue planilha1.getRow(indicePrimeiraLinhaPlanilha).getCell(0).getCellStyle().locked
        assertEquals excelFile.getCellValue(0,1,indicePrimeiraLinhaPlanilha),
                formateTelefones(resposta.representante.telefones)
        assertTrue planilha1.getRow(indicePrimeiraLinhaPlanilha).getCell(1).getCellStyle().locked
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                "Quantidade"
        assertTrue planilha1.getRow(indicePrimeiraLinhaPlanilha).getCell(0).getCellStyle().locked
        assertEquals excelFile.getCellValue(0,1,indicePrimeiraLinhaPlanilha),
                "Descricao"
        assertTrue planilha1.getRow(indicePrimeiraLinhaPlanilha).getCell(1).getCellStyle().locked
        assertEquals excelFile.getCellValue(0,2,indicePrimeiraLinhaPlanilha),
                "Embalagem"
        assertTrue planilha1.getRow(indicePrimeiraLinhaPlanilha).getCell(2).getCellStyle().locked
        assertEquals excelFile.getCellValue(0,3,indicePrimeiraLinhaPlanilha),
                "Preco Embalagem"
        assertTrue planilha1.getRow(indicePrimeiraLinhaPlanilha).getCell(3).getCellStyle().locked
        assertEquals excelFile.getCellValue(0,4,indicePrimeiraLinhaPlanilha),
                "Preco Unitario"
        assertTrue planilha1.getRow(indicePrimeiraLinhaPlanilha).getCell(4).getCellStyle().locked
        assertEquals excelFile.getCellValue(0,5,indicePrimeiraLinhaPlanilha),
                ("Observação")
        assertEquals excelFile.getCellValue(0,6,indicePrimeiraLinhaPlanilha),
                ("*Preencha o preço de cada produto em apenas na")
        assertEquals excelFile.getCellValue(0,6,indicePrimeiraLinhaPlanilha+1),
                ("coluna que te for conveniente. A outra será")
        assertEquals excelFile.getCellValue(0,6,indicePrimeiraLinhaPlanilha+2),
                ("calculada automaticamente")

        indicePrimeiraLinhaPlanilha++
        Iterator rowIterator = planilha1.rowIterator()

        while(rowIterator.hasNext()){
            Row linhaAnaliseCotacao = (Row)rowIterator.next()
            if(linhaAnaliseCotacao.rowNum >= indicePrimeiraLinhaPlanilha &&
                    linhaAnaliseCotacao.rowNum < indicePrimeiraLinhaPlanilha+cotacao.itens.size()-1){

                int itemCotacaoIdx = linhaAnaliseCotacao.rowNum -indicePrimeiraLinhaPlanilha
                ItemCotacao itemCotacao = cotacao.itens.asList().get(itemCotacaoIdx)

                assertEquals itemCotacao.quantidade,
                        excelFile.getCellValue(0, 0, linhaAnaliseCotacao.rowNum)
                assertTrue linhaAnaliseCotacao.getCell(0).getCellStyle().locked
                assertEquals itemCotacao.produto.descricao,
                        excelFile.getCellValue(0, 1, linhaAnaliseCotacao.rowNum)
                assertTrue linhaAnaliseCotacao.getCell(1).getCellStyle().locked
                assertEquals itemCotacao.produto.embalagem.toString().toUpperCase(),
                        excelFile.getCellValue(0, 2, linhaAnaliseCotacao.rowNum)
                assertTrue linhaAnaliseCotacao.getCell(2).getCellStyle().locked

                DataFormat format = excelFile.workbook.createDataFormat()
                assertEquals linhaAnaliseCotacao.getCell(3).getCellStyle().dataFormat,
                        format.getFormat("#,##0.00")
                assertEquals linhaAnaliseCotacao.getCell(3).getCellStyle().alignment,
                        CellStyle.ALIGN_CENTER
                assertFalse linhaAnaliseCotacao.getCell(3).getCellStyle().locked
                assertEquals linhaAnaliseCotacao.getCell(4).getCellStyle().dataFormat,
                        format.getFormat("#,##0.00")
                assertEquals linhaAnaliseCotacao.getCell(4).getCellStyle().alignment,
                        CellStyle.ALIGN_CENTER
                assertFalse linhaAnaliseCotacao.getCell(4).getCellStyle().locked
            }
        }
        assertTrue excelFile.delete()
    }

    void testImportePlanilhaExcel(){
        cotacao = CotacaoFixture.crieCotacaoAguardandoRespostas()
        Resposta resposta = cotacao.respostas.asList().get(0) as Resposta
        resposta.aceite()
        String pathPlanilha = respostaService.criePlanilhaCotacaoParaPreenchimentoOffLine(resposta)

        ExcelFile excelFile = new ExcelFile(pathPlanilha)
        Sheet sheet = excelFile.getSpreadSheet(0)
        preenchaRespostaComPrecos(sheet)
        excelFile.graveArquivo(pathPlanilha)

        def respostaComPrecos = respostaService.importePlanilhaResposta(resposta, pathPlanilha, true)

        Iterator rowIterator = excelFile.getRowIteratorFromSheet(0)
        while(rowIterator.hasNext()){
            Row row = rowIterator.next()
            if(row.rowNum>=9){
                Celula celula = new Celula()
                ItemResposta itemRespostaEncontrado = respostaComPrecos.itens.find {ItemResposta itemResposta->
                    itemResposta.itemCotacao.produto.descricao == celula.getCellValue(row.getCell(1))
                } as ItemResposta

                def valorCelulaPrecoUnitario = celula.getCellValue(row.getCell(4))
                def valorCelulaPrecoEmbalagem = celula.getCellValue(row.getCell(3))
                boolean precoUnitarioCosistente = true
                boolean precoEmbalagemCosistente = true

                if(valorCelulaPrecoEmbalagem != null && valorCelulaPrecoEmbalagem != 0 && !(valorCelulaPrecoEmbalagem instanceof String)
                        && !(valorCelulaPrecoEmbalagem instanceof Boolean)){
                    precoEmbalagemCosistente = itemRespostaEncontrado.preco.embalagem == valorCelulaPrecoEmbalagem
                    precoUnitarioCosistente = itemRespostaEncontrado.preco.unitario == new BigDecimal (valorCelulaPrecoEmbalagem as double,
                            new MathContext(16, RoundingMode.HALF_UP)).divide(
                            itemRespostaEncontrado.itemCotacao.produto.embalagem.qtdeDeUnidadesNaEmbalagemDeVenda, 5, RoundingMode.HALF_UP)

                    assertNotNull itemRespostaEncontrado.preco.unitario
                    assertNotNull itemRespostaEncontrado.preco.embalagem
                }
                if(valorCelulaPrecoUnitario != null && valorCelulaPrecoUnitario != 0 &&
                        !(valorCelulaPrecoUnitario instanceof String) && !(valorCelulaPrecoUnitario instanceof Boolean)
                        && !precoEmbalagemCosistente && !precoUnitarioCosistente){
                    precoUnitarioCosistente = itemRespostaEncontrado.preco.unitario == valorCelulaPrecoUnitario
                    precoEmbalagemCosistente = itemRespostaEncontrado.preco.embalagem == itemRespostaEncontrado.preco.unitario?.multiply(
                            itemRespostaEncontrado.itemCotacao.produto.embalagem.qtdeDeUnidadesNaEmbalagemDeVenda)

                    assertNotNull itemRespostaEncontrado.preco.unitario
                    assertNotNull itemRespostaEncontrado.preco.embalagem
                }

                if((valorCelulaPrecoEmbalagem == null || valorCelulaPrecoEmbalagem == 0 ||
                        valorCelulaPrecoEmbalagem instanceof String || valorCelulaPrecoEmbalagem instanceof Boolean) &&
                        (valorCelulaPrecoUnitario == null || valorCelulaPrecoUnitario == 0 ||
                                valorCelulaPrecoUnitario instanceof String || valorCelulaPrecoUnitario instanceof Boolean)){

                    assertNull itemRespostaEncontrado.preco.embalagem
                    assertNull itemRespostaEncontrado.preco.unitario
                }
                assertTrue("valor da celula incorreto é: ${valorCelulaPrecoEmbalagem} na linha ${row.rowNum}", precoEmbalagemCosistente)
                assertTrue("valor da celula incorreto e: ${valorCelulaPrecoUnitario.toString()} na linha ${row.rowNum}", precoUnitarioCosistente)
            }
        }
        assertEquals respostaComPrecos.itens.get(7).observacao,"Produto com vencimento dia 12/08"
        assertEquals respostaComPrecos.itens.get(25).observacao,"emabalagem com 120 unidades"
        assertTrue respostaComPrecos.estado instanceof RespostaAguardandoOutrasRespostas
    }

    void testImportePlanilhaExcelInvalida(){
        cotacao = CotacaoFixture.crieCotacaoAguardandoRespostas()
        Resposta resposta = cotacao.respostas.asList().get(0) as Resposta
        resposta.aceite()
        String pathPlanilha = new Path().getPathArquivosExcelRespostas() + File.separator + "resposta_errada.xls"
        ExcelFile excelFile = new ExcelFile()
        excelFile.criePlanilha("respota invalida")
        pathPlanilha = excelFile.graveArquivo(pathPlanilha)
        assertNull respostaService.importePlanilhaResposta(resposta, pathPlanilha, true)
        excelFile.delete()

    }

    private def preenchaRespostaComPrecos(Sheet sheet) {
        sheet.getRow(10).getCell(3).setCellValue(123.22)
        sheet.getRow(11).getCell(4).setCellValue(12.12)
        sheet.getRow(12).getCell(3).setCellValue(66)
        sheet.getRow(13).getCell(3).setCellValue(12.4)
        sheet.getRow(13).getCell(4).setCellValue("asd")
        sheet.getRow(16).getCell(4).setCellValue(06.121)
        sheet.getRow(16).getCell(5).setCellValue("Produto com vencimento dia 12/08")
        sheet.getRow(17).getCell(3).setCellValue(875.123)
        sheet.getRow(18).getCell(4).setCellValue("R\$34.12")
        sheet.getRow(19).getCell(3).setCellValue(74.3)
        sheet.getRow(20).getCell(4).setCellValue(99.22)
        sheet.getRow(20).getCell(3).setCellValue(0.22)
        sheet.getRow(22).getCell(4).setCellValue("123.2")
        sheet.getRow(27).getCell(4).setCellValue(75)
        sheet.getRow(30).getCell(4).setCellValue(12)
        sheet.getRow(31).getCell(4).setCellValue(true)
        sheet.getRow(33).getCell(3).setCellValue(false)
        sheet.getRow(34).getCell(4).setCellValue(98765)
        sheet.getRow(34).getCell(5).setCellValue("emabalagem com 120 unidades")
        sheet.getRow(35).getCell(3).setCellValue(965.23)
        sheet.getRow(36).getCell(3).setCellValue(934.2)
        sheet.getRow(40).getCell(3).setCellValue("¬%¨\$¨%&")
        sheet.getRow(47).getCell(3).setCellValue(22)
        sheet.getRow(48).getCell(4).setCellValue("123,5152")
        sheet.getRow(55).getCell(4).setCellValue(1.22)
        sheet.getRow(56).getCell(3).setCellValue(0.22)
        sheet.getRow(57).getCell(3).setCellValue(14.22)
        sheet.getRow(58).getCell(4).setCellValue(14.12)
        sheet.getRow(59).getCell(4).setCellValue(92.15)
        sheet.getRow(61).getCell(3).setCellValue(3.32)
        sheet.getRow(62).getCell(4).setCellValue(3.75)
        sheet.getRow(63).getCell(3).setCellValue(4.90)
        sheet.getRow(70).getCell(3).setCellValue("1.2.4.12")
        sheet.getRow(74).getCell(3).setCellValue("¨*")
        sheet.getRow(75).getCell(3).setCellValue("(12)")
        sheet.getRow(76).getCell(3).setCellValue("-512")
        sheet.getRow(80).getCell(3).setCellValue("23,23")
        sheet.getRow(81).getCell(3).setCellValue("NULL")
        sheet.getRow(83).getCell(3).setCellValue(null)
        sheet.getRow(85).getCell(3).setCellValue(",")
        sheet.getRow(92).getCell(3).setCellValue("++")
        sheet.getRow(93).getCell(3).setCellValue("123%")
        sheet.getRow(94).getCell(3).setCellValue("=412*14")
        sheet.getRow(98).getCell(4).setCellValue(7.35)
    }

    private String formateData(Date data) {
        return String.format('%td/%<tm/%<tY', data)
    }

    private String formateEnderecoPrimeiraCelula(ICotacao cotacao) {
        String enderero = ""
        if(cotacao.enderecoEntrega.logradouro)
            enderero += cotacao.enderecoEntrega.logradouro
        if(cotacao.enderecoEntrega.complemento)
            enderero += ", " + cotacao.enderecoEntrega.complemento
        return enderero
    }

    private String fomateEnderecoSegundaCelula(ICotacao cotacao) {
        Endereco endereco1 = cotacao.enderecoEntrega
        String endereco = ""
        if(endereco1.cep){
            endereco += "cep:" + cotacao.enderecoEntrega.cep
        }
        if(endereco1.bairro){
            if(endereco1.cep)
                endereco1 += ", "
            endereco += "Setor " + cotacao.enderecoEntrega.bairro
        }
        if(endereco1.cidade){
            if(endereco1.bairro || endereco1.cep)
                endereco += " ,"
            endereco += cotacao.enderecoEntrega.cidade
        }
        if(endereco1.estado && endereco1.cidade){
            endereco += " - "
            endereco += cotacao.enderecoEntrega.estado
        }
        return endereco
    }

    private String formateTelefones(List telefones) {
        String result = ""
        telefones.eachWithIndex {Telefone telefone, int index ->
            result += "${telefone.toString()}"
            if(index<telefones.size()-1)
                result += " / "
        }
        return result
    }
}