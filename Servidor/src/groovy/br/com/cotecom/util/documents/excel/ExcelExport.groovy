package br.com.cotecom.util.documents.excel

import br.com.cotecom.domain.cotacao.ICotacao
import br.com.cotecom.domain.item.ItemPedido
import br.com.cotecom.domain.item.ItemResposta
import br.com.cotecom.domain.pedido.Pedido
import br.com.cotecom.domain.resposta.IResposta
import br.com.cotecom.domain.resposta.Resposta

import br.com.cotecom.domain.usuarios.empresa.Endereco
import br.com.cotecom.domain.usuarios.empresa.Telefone
import br.com.cotecom.util.Path.Path
import org.apache.log4j.Logger
import org.apache.poi.hssf.usermodel.HSSFSheet

import org.apache.poi.ss.usermodel.*
import br.com.analise.domain.Compra
import br.com.analise.domain.RespostaCompra
import br.com.cotecom.domain.resposta.EstadoResposta
import br.com.analise.domain.Item

import org.apache.poi.ss.util.CellRangeAddress

public class ExcelExport {

    private static final log = Logger.getLogger(ExcelImport.class)

    private ExcelFile excelFile
    public static final short COR_MENOR_PRECO = IndexedColors.LIME.index
    public static final short COR_SEGUNDO_MENOR_PRECO = IndexedColors.LIGHT_YELLOW.index

    public ExcelExport(){
        excelFile = new ExcelFile()
    }

    public ExcelFile crieTemplateKote(Sheet planilha){
        if(!planilha){
            planilha = excelFile.criePlanilha()
        }
        planilha.setDisplayGridlines false
        def row1 = planilha.getRow(0)
        if(!row1)
            row1 = planilha.createRow(0)
        row1.setHeightInPoints(16.5f)
        def row2 = planilha.getRow(1)
        if(!row2)
            row2 = planilha.createRow(1)
        row2.setHeightInPoints(16.5f)
        def row3 = planilha.getRow(2)
        if(!row3)
            row3 = planilha.createRow(2)
        row3.setHeightInPoints(16.5f)
        def row4 = planilha.getRow(3)
        if(!row4)
            row4 = planilha.createRow(3)
        row4.setHeightInPoints(16.5f)

        def imagem = new Path().getPathImages() + File.separator + "templates" + File.separator + "topo_excel_kote.fw.png"
        excelFile.insiraImagem(planilha, imagem ,0 , 0)
        return excelFile
    }

    public String exporteFormularioRespostaCotacao(IResposta iResposta) {
        ICotacao iCotacao = iResposta.cotacao
        Sheet planilha = excelFile.criePlanilha("""cotação ${iCotacao.empresa.nomeFantasia} ${formateDataHifen(iCotacao.dataCriacao)}""")
        planilha.createRow(0).createCell(0).setCellValue(iResposta?.id)
        planilha.createRow(1)
        planilha.createRow(2)
        planilha.createRow(3)
        int indiceInicioItensCotacao = 3;
        indiceInicioItensCotacao = graveCabecalhoCompra(indiceInicioItensCotacao, planilha, iCotacao)
        String nomeRepresentanteENomeFantasiaETelefones = "${iResposta.representante.nome}"
        nomeRepresentanteENomeFantasiaETelefones += iResposta.representante?.empresa ? " / ${iResposta.representante?.empresa?.nomeFantasia}" : ""
        nomeRepresentanteENomeFantasiaETelefones += " - ${formateTelefones(iResposta.representante.telefones)}"
        planilha.createRow(indiceInicioItensCotacao).createCell(0).setCellValue(nomeRepresentanteENomeFantasiaETelefones)
        planilha.addMergedRegion(new CellRangeAddress(indiceInicioItensCotacao, indiceInicioItensCotacao, 0, 4))
        indiceInicioItensCotacao++
        graveCabecalhoItensCotacao(planilha, indiceInicioItensCotacao)
        preenchaItensRespostaCotacao(iResposta, planilha)
        planilha.setColumnWidth(0, 6*256);
        planilha.setColumnWidth(1, 6*256);
        planilha.autoSizeColumn(2);
        planilha.setColumnWidth(3, 15*256);
        planilha.setColumnWidth(4, 23*256);
        planilha.setColumnWidth(5, 24*256);
        planilha.setColumnWidth(6, 18*256);
        bloqueieCelulasPreenchidas(planilha, iResposta)
        //imagens são redimensionadas com resize de linhas e colunas por isso o template é adicionado aqui
        excelFile = crieTemplateKote(planilha)
        (planilha as HSSFSheet).protectSheet("Kote compras inteligentes!")
        return excelFile.graveArquivo(new Path().getPathArquivosExcelRespostas() + File.separator + iResposta.id.toString()
                + ".xls")
    }

    public String exporteAnalise(Compra compra) {
        Sheet planilha = excelFile.criePlanilha("Analise da cotacao")

        Map indicesColunaFornecedores = crieTemplateAnalise(planilha, compra)

        preenchaProdutosEPrecos(compra, planilha, indicesColunaFornecedores)

        def totalrespostas = 2 + compra.respostasCompra.size()
        for(i in 0..totalrespostas){
            planilha.autoSizeColumn(i);
        }
        //imagens são redimensionadas com resize de linhas e colunas por isso o template é adicionado aqui
        excelFile = crieTemplateKote(planilha)
        criePlanilhasDePedidosEAsPreencha(compra)
        return excelFile.graveArquivo(new Path().getPathArquivosExcelAnalises() + File.separator + compra.id.toString()
                + ".xls")
    }

    public Map exportePedidosPrimeiraOrdem(Compra compra) {
        Map<Long,String> map = [:]
        compra.respostasCompra.findAll {
            (it.estadoResposta == EstadoResposta.RESPONDA_PEDIDO_PENDENTE || it.estadoResposta == EstadoResposta.PEDIDO_FATURADO)
        }.each {RespostaCompra respostaCompra->
            def resposta = Resposta.read(respostaCompra.idResposta)
            if(resposta.pedidos && resposta.pedidos?.get(0)){
                String pathCompleto = crieExcelPedido(compra, respostaCompra, resposta.pedidos?.get(0) as Pedido)
                map.put(resposta.representante.id, pathCompleto)
            }
        }
        return map
    }

    public String exportePedido(Compra compra, Pedido pedido){
        RespostaCompra respostaCompra = compra.respostasCompra.find {it.idResposta == pedido.resposta.id}
        if(pedido)
            return crieExcelPedido(compra, respostaCompra, pedido)
        return null
    }

    private void criePlanilhasDePedidosEAsPreencha(Compra compra) {
        compra.respostasCompra.each { RespostaCompra respostaCompra ->
            def resposta = Resposta.read(respostaCompra.idResposta)
            if((respostaCompra.estadoResposta == EstadoResposta.PEDIDO_FATURADO ||
                    respostaCompra.estadoResposta == EstadoResposta.RESPONDA_PEDIDO_PENDENTE ||
                    respostaCompra.estadoResposta == EstadoResposta.AGUARDANDO_RETORNO_DE_PEDIDOS ||
                    respostaCompra.estadoResposta == EstadoResposta.ANALISANDO_FALTAS) && resposta.pedidos != null
                    && resposta.pedidos.size() > 0){
                Pedido pedido = resposta.pedidos.get(0)
                int linhaInicioItensPedido = 0
                Sheet planilha
                if(respostaCompra.nomeFantasiaEmpresa){
                    planilha = excelFile.criePlanilha(excelFile.getPlanilha("Pedido ${respostaCompra.nomeFantasiaEmpresa}") ?
                            "Pedido ${respostaCompra.nomeFantasiaEmpresa} - ${respostaCompra.emailRepresentante} ": "Pedido ${respostaCompra.nomeFantasiaEmpresa}")
                }else{
                    planilha = excelFile.criePlanilha(excelFile.getPlanilha("Pedido ${respostaCompra.nomeRepresentante}") ?
                        "Pedido ${respostaCompra.nomeRepresentante} - ${respostaCompra.emailRepresentante}" : "Pedido ${respostaCompra.nomeRepresentante}")
                }
                planilha.setDisplayGridlines false
                def fornecedor = ""
                if(respostaCompra.nomeFantasiaEmpresa)
                    fornecedor += "${respostaCompra.nomeFantasiaEmpresa} /"
                fornecedor += "${respostaCompra.nomeRepresentante}"
                planilha.createRow(0).createCell(0).setCellValue(fornecedor)
                planilha.addMergedRegion(new CellRangeAddress(0, 0, 0, 2))
                planilha.createRow(1).createCell(0).setCellValue("$respostaCompra.emailRepresentante / ${respostaCompra.telefones}")
                planilha.addMergedRegion(new CellRangeAddress(1, 1, 0, 2))
                planilha.createRow(2).createCell(0).setCellValue("Ordem de compra:")
                planilha.addMergedRegion(new CellRangeAddress(2, 2, 0, 1))
                planilha.getRow(2).createCell(2).setCellValue("${pedido.id.toString()}")
                planilha.createRow(3).createCell(0).setCellValue("Mix total:")
                planilha.addMergedRegion(new CellRangeAddress(3, 3, 0, 1))
                planilha.getRow(3).createCell(2).setCellValue(pedido.itens.size())
                planilha.createRow(4).createCell(0).setCellValue("Valor total:")
                planilha.addMergedRegion(new CellRangeAddress(4, 4, 0, 1))
                planilha.getRow(4).createCell(2).setCellValue(pedido.valorTotalPedido())
                excelFile.setRegionStyle(planilha, 0..4, 0..3, crieEstiloCabecalho())
                planilha.getRow(4).getCell(2).setCellStyle(crieEstiloRealBR())
                excelFile.setFonte(planilha.getRow(4).getCell(2), crieFonteTitulo())
                linhaInicioItensPedido = 5
                preenchaItensDoPedido(planilha, linhaInicioItensPedido, pedido)
            }
        }
    }

    private String formateTelefones(Set telefones) {
        String result = ""
        telefones.eachWithIndex {Telefone telefone, int index ->
            result += "${telefone.toString()}"
            if(index<telefones.size()-1)
                result += " / "
        }
        return result
    }

    private CellStyle crieEstiloRealBR() {
        DataFormat format = excelFile.workbook.createDataFormat()
        CellStyle estiloMoeda = excelFile.workbook.createCellStyle()
        estiloMoeda.setDataFormat(format.getFormat("R\$#,##0.00"))
        estiloMoeda.setAlignment(CellStyle.ALIGN_CENTER)
        return estiloMoeda
    }

    private void preenchaProdutosEPrecos(Compra compra, Sheet planilha, Map indicesColunaFornecedores) {
        def linhaPrimeiroItem = 0
        Iterator rowIterator = planilha.rowIterator()

        while(rowIterator.hasNext()){
            rowIterator.next()
            linhaPrimeiroItem++
        }
       // linhaPrimeiroItem++

        compra.itens
                .sort {it.descricao}
                .eachWithIndex {Item item, int indexItem ->
            Row row = planilha.createRow(indexItem + linhaPrimeiroItem)
            row.createCell(0).setCellValue(item.quantidade)
            excelFile.setAligment(row.getCell(0),CellStyle.ALIGN_CENTER)
            row.createCell(1).setCellValue(item.descricao)
            row.createCell(2).setCellValue(item.embalagem?.toUpperCase())

            item.respostas.eachWithIndex {br.com.analise.domain.Resposta resposta, int indexResposta ->
                Cell celula = row.createCell(getIndiceColuna(indicesColunaFornecedores, resposta))

                if(resposta.preco == null || resposta.preco == 0){
                    celula.setCellValue("-")
                } else if (indexResposta == 0) {
                    celula.setCellValue(resposta.preco)
                    excelFile.setBackgroundColor(celula, COR_MENOR_PRECO)
                } else if (indexResposta == 1) {
                    celula.setCellValue(resposta.preco)
                    excelFile.setBackgroundColor(celula, COR_SEGUNDO_MENOR_PRECO)
                }else{
                    celula.setCellValue(resposta.preco)
                }
            }
        }
    }

    private Map crieTemplateAnalise(Sheet planilha, Compra compra) {
        planilha.createRow(0)
        planilha.createRow(1)
        planilha.createRow(2)
        planilha.createRow(3)
        int indiceInicioItensCotacao = 3
        Map indicesColunaFornecedores = new HashMap()
        indiceInicioItensCotacao = graveCabecalhoCompra(indiceInicioItensCotacao, planilha, compra)
        indiceInicioItensCotacao = graveDadosRepresentantes(compra, planilha, indiceInicioItensCotacao)
        indiceInicioItensCotacao = graveLegenda(indiceInicioItensCotacao, planilha)
        graveCabecalhoItensCotacaoEItensResposta(indiceInicioItensCotacao, planilha, compra, indicesColunaFornecedores)
        return indicesColunaFornecedores
    }

    private int graveLegenda(int indiceInicioItensCotacao, Sheet planilha) {
        Cell celulaLegendaMelhorPreco = planilha.createRow(indiceInicioItensCotacao).createCell(0)
        celulaLegendaMelhorPreco.setCellValue("Melhor Preco")
        excelFile.setBackgroundColor(celulaLegendaMelhorPreco, COR_MENOR_PRECO)
        indiceInicioItensCotacao++

        Cell celulaLegendaSegundoMelhorPreco = planilha.createRow(indiceInicioItensCotacao).createCell(0)
        celulaLegendaSegundoMelhorPreco.setCellValue("Segundo Preco")
        excelFile.setBackgroundColor(celulaLegendaSegundoMelhorPreco, COR_SEGUNDO_MENOR_PRECO)
        indiceInicioItensCotacao++
        return indiceInicioItensCotacao
    }

    private void graveCabecalhoItensCotacaoEItensResposta(int indiceInicioItensCotacao, Sheet planilha, Compra compra, Map indicesColunaFornecedores) {
        CellStyle estiloColunasPreco = crieEstiloColunasPreco()
        Row linhaCabecalhoCotacao = planilha.createRow(indiceInicioItensCotacao)
        Row primeiraLinha = planilha.getRow(0)? planilha.getRow(0) : planilha.createRow(0)
        linhaCabecalhoCotacao.createCell(0).setCellValue("Qtd")
        linhaCabecalhoCotacao.createCell(1).setCellValue("Descrição")
        linhaCabecalhoCotacao.createCell(2).setCellValue("Embalagem")

        compra.respostasCompra.findAll {
            (it.estadoResposta == EstadoResposta.RESPONDA_PEDIDO_PENDENTE ||
                    it.estadoResposta == EstadoResposta.AGUARDANDO_RETORNO_DE_PEDIDOS||
                    it.estadoResposta == EstadoResposta.PEDIDO_FATURADO ||
                    it.estadoResposta == EstadoResposta.ANALISANDO_FALTAS)
        }.eachWithIndex {RespostaCompra resposta, int index ->
            linhaCabecalhoCotacao.createCell(index + 3).setCellValue(resposta.nomeRepresentante)
            primeiraLinha.createCell(index + 3).setCellValue(resposta.nomeRepresentante)
            primeiraLinha.getCell(index+3).setCellStyle(crieEstiloCabecalho())
            planilha.setDefaultColumnStyle(index + 3, estiloColunasPreco)
            indicesColunaFornecedores.putAt(resposta.emailRepresentante + resposta.idRepresentante, index + 3)
        }

        Iterator celulasCabecalhoIterator = linhaCabecalhoCotacao.cellIterator()
        while (celulasCabecalhoIterator.hasNext()) {
            Cell cell = (Cell) celulasCabecalhoIterator.next()
            cell.setCellStyle(crieEstiloCabecalho())
        }
        planilha.createFreezePane( 0, 1, 0, 1 )
    }

    private int graveDadosRepresentantes(Compra compra, Sheet planilha, int indiceInicioItensCotacao) {
        Row linha7 = planilha.createRow(indiceInicioItensCotacao)
        indiceInicioItensCotacao++
        linha7.createCell(0).setCellValue("Representantes")
        excelFile.setAligment(linha7.getCell(0), CellStyle.ALIGN_CENTER)
        excelFile.setFonte(linha7.getCell(0), crieFonteTitulo())
        planilha.addMergedRegion(new CellRangeAddress(linha7.rowNum, linha7.rowNum, 0, 2))

        compra.respostasCompra.eachWithIndex {RespostaCompra resposta, int index ->
            Row linhaDadosRepresentante = planilha.createRow(indiceInicioItensCotacao)
            String fornecedor = "$resposta.nomeRepresentante"
            if(resposta.nomeFantasiaEmpresa != null && resposta.nomeFantasiaEmpresa != "")
                fornecedor += "/ ${resposta.nomeFantasiaEmpresa}"
            linhaDadosRepresentante.createCell(0).setCellValue(fornecedor)
            def numLinha = linhaDadosRepresentante.rowNum
            planilha.addMergedRegion(new CellRangeAddress(numLinha, numLinha, 0, 1))
            def telefones = resposta.telefones as List
            if(telefones.size()>0){
                String telefone = telefones?.get(0)
                linhaDadosRepresentante.createCell(2).setCellValue(telefone)
            }
            if(resposta.estadoResposta == EstadoResposta.CANCELADA){
                linhaDadosRepresentante.createCell(3).setCellValue "*Representante excluído da cotação"
                planilha.addMergedRegion(new CellRangeAddress(linhaDadosRepresentante.rowNum, linhaDadosRepresentante.rowNum, 3, 6))
                CellStyle cellStyle = this.excelFile.workbook.createCellStyle()
                Font font = this.excelFile.workbook.createFont()
                font.setStrikeout true
                cellStyle.setFont font
                linhaDadosRepresentante.getCell(0)?linhaDadosRepresentante.getCell(0).setCellStyle(cellStyle):linhaDadosRepresentante.createCell(0).setCellStyle(cellStyle)
                linhaDadosRepresentante.getCell(2)?linhaDadosRepresentante.getCell(2).setCellStyle(cellStyle):linhaDadosRepresentante.createCell(2).setCellStyle(cellStyle)
                linhaDadosRepresentante.getCell(3)?linhaDadosRepresentante.getCell(3).setCellStyle(cellStyle):linhaDadosRepresentante.createCell(3).setCellStyle(cellStyle)
            }
            indiceInicioItensCotacao++
        }
        return indiceInicioItensCotacao
    }

    private int graveCabecalhoCompra(int indiceInicioItensCotacao, Sheet planilha, Compra compra) {
        //linha reservada para imagem
        indiceInicioItensCotacao++
        Row linha1 = planilha.createRow(indiceInicioItensCotacao)
        def tituloCotacao = compra.titulo + " - " + formateDataBrazil(compra.dataCriacao)
        linha1.createCell(0).setCellValue(tituloCotacao)
        planilha.addMergedRegion(new CellRangeAddress(indiceInicioItensCotacao, indiceInicioItensCotacao, 0, 4))
        excelFile.setAligment(linha1.getCell(0), CellStyle.ALIGN_CENTER)
        excelFile.setFonte(linha1.getCell(0), crieFonteTitulo())
        indiceInicioItensCotacao++
        Row linha2 = planilha.createRow(indiceInicioItensCotacao)
        linha2.createCell(0).setCellValue("Prazo de pagamento - " + compra.prazoPagamento + " dias")
        planilha.addMergedRegion(new CellRangeAddress(indiceInicioItensCotacao, indiceInicioItensCotacao, 0, 4))
        excelFile.setFonte(linha2.getCell(0), crieFonteTitulo())
        indiceInicioItensCotacao++
        Row linha3 = planilha.createRow(indiceInicioItensCotacao)
        linha3.createCell(0).setCellValue("Data de entrega - " + formateDataBrazil(compra.dataEntrega))
        planilha.addMergedRegion(new CellRangeAddress(indiceInicioItensCotacao, indiceInicioItensCotacao, 0, 4))
        excelFile.setFonte(linha3.getCell(0), crieFonteTitulo())
        indiceInicioItensCotacao++
        Row linha4 = planilha.createRow(indiceInicioItensCotacao)
        linha4.createCell(0).setCellValue("Validade da cotação - " + formateDataBrazil(compra.dataValidade))
        planilha.addMergedRegion(new CellRangeAddress(indiceInicioItensCotacao, indiceInicioItensCotacao, 0, 4))
        excelFile.setFonte(linha4.getCell(0), crieFonteTitulo())
        indiceInicioItensCotacao++

        Row linha5 = planilha.createRow(indiceInicioItensCotacao)
        linha5.createCell(0).setCellValue("Endereço de entrega - " + compra.endereco)
        planilha.addMergedRegion(new CellRangeAddress(indiceInicioItensCotacao, indiceInicioItensCotacao+2, 0, 4))
        excelFile.setFonte(linha5.getCell(0), crieFonteTitulo())
        indiceInicioItensCotacao+2
        return indiceInicioItensCotacao
    }

    private int graveCabecalhoCompra(int indiceInicioItensCotacao, Sheet planilha, ICotacao cotacao) {
        //linha reservada para imagem
        indiceInicioItensCotacao++
        Row linha1 = planilha.createRow(indiceInicioItensCotacao)
        def tituloCotacao = cotacao.titulo + " - " + formateDataBrazil(cotacao.dataCriacao)
        linha1.createCell(0).setCellValue(tituloCotacao)
        planilha.addMergedRegion(new CellRangeAddress(indiceInicioItensCotacao, indiceInicioItensCotacao, 0, 4))
        excelFile.setAligment(linha1.getCell(0), CellStyle.ALIGN_CENTER)
        excelFile.setFonte(linha1.getCell(0), crieFonteTitulo())
        indiceInicioItensCotacao++
        Row linha2 = planilha.createRow(indiceInicioItensCotacao)
        linha2.createCell(0).setCellValue("Prazo de pagamento - " + cotacao.prazoPagamento + " dias")
        planilha.addMergedRegion(new CellRangeAddress(indiceInicioItensCotacao, indiceInicioItensCotacao, 0, 4))
        excelFile.setFonte(linha2.getCell(0), crieFonteTitulo())
        indiceInicioItensCotacao++
        Row linha3 = planilha.createRow(indiceInicioItensCotacao)
        linha3.createCell(0).setCellValue("Data de entrega - " + formateDataBrazil(cotacao.dataEntrega))
        planilha.addMergedRegion(new CellRangeAddress(indiceInicioItensCotacao, indiceInicioItensCotacao, 0, 4))
        excelFile.setFonte(linha3.getCell(0), crieFonteTitulo())
        indiceInicioItensCotacao++
        Row linha4 = planilha.createRow(indiceInicioItensCotacao)
        linha4.createCell(0).setCellValue("Validade da cotacao - " + formateDataBrazil(cotacao.dataValidade))
        planilha.addMergedRegion(new CellRangeAddress(indiceInicioItensCotacao, indiceInicioItensCotacao, 0, 4))
        excelFile.setFonte(linha4.getCell(0), crieFonteTitulo())
        indiceInicioItensCotacao++

        Row linha5 = planilha.createRow(indiceInicioItensCotacao)
        linha5.createCell(0).setCellValue("Endereço de entrega - " + cotacao.enderecoEntrega?.toString())
        planilha.addMergedRegion(new CellRangeAddress(indiceInicioItensCotacao, indiceInicioItensCotacao+2, 0, 4))
        excelFile.setFonte(linha5.getCell(0), crieFonteTitulo())
        indiceInicioItensCotacao+2
        return indiceInicioItensCotacao
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
                endereco += ", "
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

    private String formateDataBrazil(Date data) {
        return String.format('%td/%<tm/%<tY', data)
    }

    private String formateDataHifen(Date data) {
        return String.format('%td-%<tm-%<tY', data)
    }

    private CellStyle crieEstiloCabecalho() {
        CellStyle estiloCabecalho = excelFile.workbook.createCellStyle()
        estiloCabecalho.setFont(crieFonteTitulo())
        return estiloCabecalho
    }

    private Integer getIndiceColuna(Map indicesColunaFornecedores , br.com.analise.domain.Resposta resposta) {
        return indicesColunaFornecedores.get(resposta.respostaCompra.emailRepresentante + resposta.respostaCompra.idRepresentante) as Integer
    }

    private CellStyle crieEstiloColunasPreco() {
        DataFormat format = excelFile.workbook.createDataFormat()
        CellStyle estiloColunasPrecos = excelFile.workbook.createCellStyle()
        estiloColunasPrecos.setDataFormat(format.getFormat("#,##0.00"))
        estiloColunasPrecos.setAlignment(CellStyle.ALIGN_CENTER)
        return estiloColunasPrecos
    }

    private Font crieFonteTitulo() {
        Font fonte = excelFile.workbook.createFont()
        fonte.setFontHeightInPoints(12 as short)
        fonte.setFontName("Courier New");
        fonte.setBoldweight(Font.BOLDWEIGHT_BOLD);
        return fonte
    }

    private String crieExcelPedido(Compra compra, RespostaCompra resposta, Pedido pedido) {
        excelFile = new ExcelFile()
        def dataDeCriacaoFormatada = formateDataBrazil(compra.dataCriacao).replaceAll("/", "-")
        def nomePlanilha = "Pedido ${dataDeCriacaoFormatada}"
        if(resposta.nomeFantasiaEmpresa)
            nomePlanilha += " ${resposta.nomeFantasiaEmpresa}"
        Sheet planilha = excelFile.criePlanilha(nomePlanilha)

        planilha.createRow(0)
        planilha.createRow(1)
        planilha.createRow(2)
        planilha.createRow(3)
        int linhaInicioItensPedido = 3;
        linhaInicioItensPedido = graveCabecalhoCompra(linhaInicioItensPedido, planilha, compra)

        Row linha = planilha.createRow(linhaInicioItensPedido)
        def nomeRepresentanteEEmpresa = resposta.nomeRepresentante
        if(resposta.nomeFantasiaEmpresa)
            nomeRepresentanteEEmpresa += " / " + resposta.nomeFantasiaEmpresa
        linha.createCell(0).setCellValue(nomeRepresentanteEEmpresa)
        linha.createCell(1)
        linha.createCell(2)
        linha.createCell(3)
        planilha.addMergedRegion(new CellRangeAddress(linhaInicioItensPedido, linhaInicioItensPedido, 0, 3))
        linhaInicioItensPedido++
        Row linhaInicioPedido = planilha.createRow(linhaInicioItensPedido)
        linhaInicioPedido.createCell(0).setCellValue("Ordem de compra nº:")
        planilha.addMergedRegion(new CellRangeAddress(linhaInicioItensPedido, linhaInicioItensPedido, 0, 1))
        linhaInicioPedido.createCell(2).setCellValue(pedido.id)
        excelFile.setAligment(linhaInicioPedido.getCell(2), CellStyle.ALIGN_LEFT)

        linhaInicioPedido.createCell(3).setCellValue("Mix total:")
        linhaInicioPedido.createCell(4).setCellValue(pedido.itens.size())
        excelFile.setAligment(linhaInicioPedido.getCell(4), CellStyle.ALIGN_LEFT)
        linhaInicioItensPedido++

        preenchaItensDoPedido(planilha, linhaInicioItensPedido, pedido)
        excelFile = crieTemplateKote(planilha)
        String pathCompleto = excelFile.graveArquivo(new Path().getPathArquivosExcelPedidos() + File.separator +
                pedido.id + ".xls")
        return pathCompleto
    }

    private def preenchaItensDoPedido(Sheet planilha, int linhaInicioItensPedido, Pedido pedido) {
        Row linha2 = planilha.createRow(linhaInicioItensPedido)
        linha2.createCell(0).setCellValue("Qtd")
        linha2.createCell(1).setCellValue("Descrição")
        linha2.createCell(2).setCellValue("Embalagem")
        linha2.createCell(3).setCellValue("Preco Unitário")
        linha2.createCell(4).setCellValue("Preco Embalagem")
        linha2.createCell(5).setCellValue("Valor Total")
        excelFile.setRegionStyle(planilha, linha2.rowNum..linha2.rowNum, 0..5, crieEstiloCabecalho())
        linhaInicioItensPedido++

        pedido.itens
                .sort {it.itemResposta.itemCotacao.produto.descricao}
                .each {ItemPedido itemPedido ->
            Row linhaNewItem = planilha.createRow(linhaInicioItensPedido)
            linhaNewItem.createCell(0).setCellValue(itemPedido.qtdPedida)
            linhaNewItem.createCell(1).setCellValue(itemPedido.itemResposta.itemCotacao.produto.descricao)
            linhaNewItem.createCell(2).setCellValue(itemPedido.itemResposta.itemCotacao.produto.embalagem.toString())
            linhaNewItem.createCell(3).setCellValue(itemPedido.calculePrecoUnitario())
            linhaNewItem.getCell(3).setCellStyle(crieEstiloColunasPreco())
            linhaNewItem.createCell(4).setCellValue(itemPedido.precoEmbalagem)
            linhaNewItem.getCell(4).setCellStyle(crieEstiloColunasPreco())
            linhaNewItem.createCell(5).setCellValue(itemPedido.precoEmbalagem*itemPedido.qtdPedida)
            linhaNewItem.getCell(5).setCellStyle(crieEstiloColunasPreco())

            excelFile.setAligment(linhaNewItem.getCell(0), CellStyle.ALIGN_CENTER)
            linhaInicioItensPedido++
        }

        Row linhaTotalPedido = planilha.createRow(linhaInicioItensPedido)
        linhaTotalPedido.createCell(0).setCellValue("Total pedido -->")
        linhaTotalPedido.getCell(0).setCellStyle(crieEstiloCabecalho())
        linhaTotalPedido.createCell(1)
        linhaTotalPedido.createCell(2)
        planilha.addMergedRegion(new CellRangeAddress(linhaInicioItensPedido, linhaInicioItensPedido, 0, 2))
        linhaTotalPedido.createCell(5).setCellValue(pedido.valorTotalPedido())
        linhaTotalPedido.getCell(5).setCellStyle(crieEstiloRealBR())
        excelFile.setFonte(linhaTotalPedido.getCell(5), crieFonteTitulo())
        linhaInicioItensPedido++

        planilha.setColumnWidth(0, 5*256);
        planilha.autoSizeColumn(1);
        planilha.setColumnWidth(2, 15*256);
        planilha.setColumnWidth(3, 23*256);
        planilha.setColumnWidth(4, 24*256);
        planilha.setColumnWidth(5, 20*256);
    }

    private void bloqueieCelulasPreenchidas(Sheet planilha, IResposta resposta) {
        def inicioProdutos = 10
        def fimItens = inicioProdutos + resposta.itens.size() - 1
        excelFile.setLocked(4..6, inicioProdutos..fimItens,planilha, false)
    }

    private void preenchaItensRespostaCotacao(IResposta iResposta, Sheet planilha) {
        CellStyle estiloColunasPreco = crieEstiloColunasPreco()
        int linhaPrimeiroItem = 0
        Iterator rowIterator = planilha.rowIterator()

        while(rowIterator.hasNext()){
            rowIterator.next()
            linhaPrimeiroItem++
        }
        linhaPrimeiroItem = linhaPrimeiroItem-2;//*linhas da Obs sobre preenchimento preços
        //linhaPrimeiroItem++
        (iResposta.itens as List<ItemResposta>)
                .sort{it.itemCotacao.produto.descricao}
                .eachWithIndex {ItemResposta itemResposta, int indexItemResposta ->
            Row row = planilha.getRow(indexItemResposta + linhaPrimeiroItem as int) ?
                planilha.getRow(indexItemResposta + linhaPrimeiroItem as int):
                planilha.createRow(indexItemResposta + linhaPrimeiroItem as int)
            row.createCell(0).setCellValue(indexItemResposta+1)
            row.createCell(1).setCellValue(itemResposta.itemCotacao.quantidade)
            excelFile.setAligment(row.getCell(1),CellStyle.ALIGN_CENTER)
            row.createCell(2).setCellValue(itemResposta.itemCotacao.produto.descricao)
            row.createCell(3).setCellValue(itemResposta.itemCotacao.produto.embalagem.toString().toUpperCase())
            row.createCell(4).setCellStyle estiloColunasPreco
            if(itemResposta.preco.embalagem)
                row.getCell(4).setCellValue(itemResposta.preco.embalagem)
            row.createCell(5).setCellStyle estiloColunasPreco
            if(itemResposta.preco.unitario)
                row.getCell(5).setCellValue(itemResposta.preco.unitario)
        }
        int indiceFimItens = linhaPrimeiroItem + iResposta.itens.size() - 1
        excelFile.setBorder(linhaPrimeiroItem..indiceFimItens, 0..6, planilha,
                CellStyle.BORDER_DASH_DOT_DOT, IndexedColors.GREY_25_PERCENT.index)
    }

    private void graveCabecalhoItensCotacao(Sheet planilha, int indiceInicioItensCotacao) {
        Row cabecalhoCotacao = planilha.createRow(indiceInicioItensCotacao)
        cabecalhoCotacao.createCell(0).setCellValue("Nº")
        cabecalhoCotacao.createCell(1).setCellValue("Qtd")
        cabecalhoCotacao.createCell(2).setCellValue("Descrição")
        cabecalhoCotacao.createCell(3).setCellValue("Embalagem")
        cabecalhoCotacao.createCell(4).setCellValue("Preco Embalagem")
        cabecalhoCotacao.createCell(5).setCellValue("Preco Unitário")
        cabecalhoCotacao.createCell(6).setCellValue("Observação")

        excelFile.setAligment(cabecalhoCotacao.rowNum..cabecalhoCotacao.rowNum, 0..6, planilha, CellStyle.ALIGN_CENTER)
        excelFile.setBorder(cabecalhoCotacao.rowNum..cabecalhoCotacao.rowNum, 0..6, planilha,
                CellStyle.BORDER_DASH_DOT_DOT, IndexedColors.GREY_25_PERCENT.index)
        excelFile.setBackgroundColor(cabecalhoCotacao.rowNum..cabecalhoCotacao.rowNum, 0..6, planilha,
                IndexedColors.GREY_25_PERCENT.index)
        excelFile.setFonte(cabecalhoCotacao.rowNum..cabecalhoCotacao.rowNum, 0..6, planilha,
                crieFonteTitulo())

        cabecalhoCotacao.createCell(7).setCellValue("*Preencha o preço de cada produto em apenas na")
        planilha.createRow(indiceInicioItensCotacao+1).createCell(7).setCellValue("coluna que te for conveniente. A outra será")
        planilha.createRow(indiceInicioItensCotacao+2).createCell(7).setCellValue("calculada automaticamente")
    }

}