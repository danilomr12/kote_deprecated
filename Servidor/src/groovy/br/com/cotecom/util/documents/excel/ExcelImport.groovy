package br.com.cotecom.util.documents.excel

import br.com.cotecom.domain.item.ItemResposta
import br.com.cotecom.domain.item.Preco
import br.com.cotecom.domain.item.Produto
import br.com.cotecom.domain.resposta.Resposta
import org.apache.log4j.Logger
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.ss.usermodel.Row
import br.com.cotecom.domain.error.ServerError
import br.com.cotecom.domain.error.ErrorHandler
import br.com.cotecom.domain.usuarios.empresa.Empresa

public class ExcelImport  {

    private static final log = Logger.getLogger(ExcelImport.class)
    ExcelFile excelFile
    ErrorHandler errorHandler = ErrorHandler.getInstance()

    public ExcelImport ( ExcelFile excelFile) {
        this.excelFile = excelFile
    }

    /**
     * Para que a importação funcione a ordem das colunas do arquivo excel deve estar
     * segundo o padrão definido colunas: A-> codigo de barras, B-> descrição, C-> embalagem,
     * D-> categoria, E-> marca, F-> fabricante, G-> peso, H->Embalagem Master, I->Quantidade Master
     */
    List<Produto> importProdutos(int indicePlanilha, Empresa empresa){
        List<Produto> produtosImportados = new ArrayList<Produto>()
        List<Produto> produtosComErro = new ArrayList<Produto>()
        Iterator linhas = excelFile.getRowIteratorFromSheet(indicePlanilha)

        HSSFRow cabecalho = linhas.next () as HSSFRow
        log.debug "---Inicio da importaçãoo de produtos---"
        while(linhas.hasNext()){
            HSSFRow linha = (HSSFRow) linhas.next()
            def codigoDeBarras = excelFile.getCellValue(indicePlanilha, 0, linha.getRowNum())
            def descricao = excelFile.getCellValue(indicePlanilha, 1, linha.getRowNum())
            def embalagem = excelFile.getCellValue(indicePlanilha, 2, linha.getRowNum())
            def categoria = excelFile.getCellValue(indicePlanilha, 3, linha.getRowNum())
            def marca = excelFile.getCellValue(indicePlanilha, 4, linha.getRowNum())
            def fabricante = excelFile.getCellValue(indicePlanilha, 5, linha.getRowNum())
            def pesoString = excelFile.getCellValue(indicePlanilha, 6, linha.getRowNum())?.toString()
            if(pesoString != null)
                pesoString = Double.parseDouble(pesoString)
            def peso = pesoString
            def embalagemMaster = excelFile.getCellValue(indicePlanilha, 7, linha.getRowNum())
            def qtdMaster = excelFile.getCellValue(indicePlanilha, 8, linha.getRowNum())?.toString()
            if(qtdMaster != null){
                try{
                    qtdMaster = Integer.parseInt(qtdMaster)
                }   catch (Exception e){
                    e.printStackTrace()
                }
            }
            def quantidadeMaster = qtdMaster

            Produto produto = new Produto()
            if(validateCells(codigoDeBarras, descricao, embalagem, categoria, marca, fabricante, peso, embalagemMaster,
                    quantidadeMaster)){
                produto = new Produto(barCode: codigoDeBarras, descricao: descricao,
                        embalagem: br.com.cotecom.domain.item.EmbalagemVenda.setEmbalagem(embalagem), categoria: categoria, marca: marca,
                        fabricante: fabricante, peso: peso, embalagemMaster: embalagemMaster, qtdMaster: quantidadeMaster, empresaId: empresa.id)
            }

            if(validateProduto(produto)){           //todo como validar produtos com erro?
                produtosImportados.push(produto)
            }else{
                log.debug "Não foi possivel importar produto: [barCode:${codigoDeBarras},descricao:${descricao},"+
                        "categoria:${categoria}, fabricante:${fabricante}, marca:${marca}, embalagem:${embalagem},"+
                        "peso:${peso}]"
                produtosComErro.push(produto)
            }
        }
        log.debug "Foram importados ${produtosImportados.size()} produtos"
        log.debug "---Fim importação arquivo---"
        return produtosImportados
    }

    private boolean validateCells(def codigoDeBarras, def descricao, def embalagem, def categoria, def marca,
                                  def fabricante, def peso, def embalagemMaster, def quantidadeMaster) {
        if(!(descricao instanceof String))
            return false
        if(embalagem != null)
            if(!(embalagem instanceof String))
                return false
        if(peso != null)
            if(!(peso instanceof Number))
                return false
        if(quantidadeMaster != null)
            if(!(quantidadeMaster instanceof Number))
                return false
        return true
    }

    private boolean validateProduto(Produto produto){
        if(produto == null || produto.descricao == null || produto.descricao.isEmpty())
            return false
        return true
    }

    def importePlanilhaResposta(Resposta resposta) {
        def keyPlanilha = this.excelFile.getCellValue(0,0,0)
        if(keyPlanilha != resposta.id)                                {
            def serverError = new ServerError(id: ServerError.UNSUPORTED_OPERATION_ID,
                    acao: "Importar resposta",
                    mensagem: "Cotação não encontrada",
                    causa: "Verifique se a planilha que está tentando importar é a mesma que foi gerada pelo sistema.")
            return errorHandler?.pushAndDispatchError(serverError)
        }
        Iterator iterator = excelFile.getRowIteratorFromSheet(0)
        def itensLidosDoExcel = [:]
        while(iterator.hasNext()){
            Row row = iterator.next()
            if(row.rowNum >= 7){
                Celula celula = new Celula()
                String descricao = celula.getCellValue(row.getCell(2))
                Preco preco = new Preco()
                def itemLidoDoExcel = [:]
                def cell3Value = celula.getCellValue(row.getCell(4))
                preco.embalagem = (cell3Value == null || cell3Value instanceof String || cell3Value instanceof  Boolean) ? null :
                    cell3Value as Double
                def cell4Value = celula.getCellValue(row.getCell(5))
                preco.setUnitario((cell4Value == null || cell4Value instanceof String || cell4Value instanceof  Boolean) ? null :
                    cell4Value as Double)
                itemLidoDoExcel.preco = preco
                itemLidoDoExcel.observacao = celula.getCellValue(row.getCell(6))
                itensLidosDoExcel.put(descricao, itemLidoDoExcel)
            }
        }

        itensLidosDoExcel.each { def itemDoExcel ->
            String descricaoDoProduto = itemDoExcel.key as String

            ItemResposta itemRespostaEncontrado = resposta.itens.find{ItemResposta itemResposta ->
                itemResposta.itemCotacao.produto.descricao == descricaoDoProduto
            } as ItemResposta

            //def itemencont = itensLidosDoExcel?.getAt(descricaoDoProduto)
            def preco = itemDoExcel?.value?.preco
            if(preco?.embalagem){
                itemRespostaEncontrado.preco = new Preco(embalagem: preco.embalagem)
            }else if(preco?.unitario){
                itemRespostaEncontrado.preco = new Preco(unitario: preco.unitario)
            }
            def observacao = itemDoExcel?.value?.observacao
            if(observacao)
                itemRespostaEncontrado?.observacao = observacao
        }
        return resposta.salve()
    }


}
