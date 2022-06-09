package br.com.cotecom.tests.util

import br.com.cotecom.util.Path.Path
import br.com.cotecom.util.documents.excel.ExcelFile
import grails.test.GrailsUnitTestCase
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.*

public class ExcelFileTests extends GrailsUnitTestCase {


    void setUp(){
        super.setUp()
    }

    void tearDown(){
        super.tearDown()
    }

    void testCrieEGraveNovaPlanilha(){
        ExcelFile excelFile = new ExcelFile()
        excelFile.criePlanilha("teste")
        String caminhoPlanilha = new Path().getPathArquivosDeTeste() + File.separator + "planilhaExcelFileTests.xls"
        String caminhoPlanilhaGravada = excelFile.graveArquivo(caminhoPlanilha)
        Workbook workbook = new HSSFWorkbook(new FileInputStream(caminhoPlanilhaGravada))
        assertNotNull workbook
        assertNotNull workbook.getSheet("teste")
        excelFile = new ExcelFile(caminhoPlanilhaGravada)
        assertNotNull excelFile.workbook
        assertNotNull excelFile.workbook.getSheet("teste")
        assertTrue excelFile.delete() 
    }

    void testGetCellValue(){
        ExcelFile excelFile = new ExcelFile()
        Sheet planilha = excelFile.criePlanilha()
        planilha.createRow(0).createCell(0).setCellValue(100.123)
        planilha.createRow(1).createCell(0).setCellValue("teste string")
        planilha.createRow(2).createCell(0).setCellValue(true)
        def cellValue = excelFile.getCellValue(0,0,0)
        assertTrue cellValue instanceof Double
        cellValue = excelFile.getCellValue(0,0,1)
        assertTrue cellValue instanceof String
        cellValue = excelFile.getCellValue(0,0,2)
        assertTrue cellValue instanceof Boolean
    }

    void testSetFontEsetAligment(){
        ExcelFile excelFile = new ExcelFile()
        Sheet planilha = excelFile.criePlanilha()

        Font fonte = excelFile.workbook.createFont()
        fonte.setBoldweight(Font.BOLDWEIGHT_BOLD)
        fonte.setFontName("Courrier New")

        CellStyle estiloInicial = excelFile.workbook.createCellStyle()
        estiloInicial.setFillPattern(CellStyle.SOLID_FOREGROUND)
        estiloInicial.setFillForegroundColor(IndexedColors.LIME.index)

        final CellStyle estiloIntermediariol = excelFile.workbook.createCellStyle()
        estiloIntermediariol.setFillPattern(CellStyle.SOLID_FOREGROUND)
        estiloIntermediariol.setFillForegroundColor(IndexedColors.LIME.index)
        estiloIntermediariol.setFont(fonte)

        Cell celula = planilha.createRow(0).createCell(0)
        celula.setCellStyle(estiloInicial)
        excelFile.setFonte(celula, fonte)

        CellStyle estiloCelula = celula.getCellStyle()

        assertEquals estiloCelula.getFillBackgroundColor(), estiloIntermediariol.getFillBackgroundColor()
        assertEquals estiloCelula.getFillForegroundColor(), estiloIntermediariol.getFillForegroundColor()
        assertEquals estiloCelula.fontIndex, estiloIntermediariol.fontIndex

        excelFile.setAligment(celula, CellStyle.ALIGN_CENTER)
        estiloCelula = celula.getCellStyle()

        final CellStyle estiloFinal = excelFile.workbook.createCellStyle()
        estiloFinal.setFillPattern(CellStyle.SOLID_FOREGROUND)
        estiloFinal.setFillForegroundColor(IndexedColors.LIME.index)
        estiloFinal.setFont(fonte)
        estiloFinal.setAlignment(CellStyle.ALIGN_CENTER)

        assertEquals estiloCelula.getFillBackgroundColor(), estiloFinal.getFillBackgroundColor()
        assertEquals estiloCelula.getFillForegroundColor(), estiloFinal.getFillForegroundColor()
        assertEquals estiloCelula.getFontIndex(), estiloFinal.getFontIndex()
        assertEquals estiloCelula.getAlignment(), estiloFinal.getAlignment()
    }

    void testBusqueCelula(){
        ExcelFile excelFile = new ExcelFile(new Path().getPathArquivosDeTeste()+File.separator +
                "lista_produtos_teste.xls")

        def query = "ACETONA CRUZEIRO                 100ML"
        def result = excelFile.busqueCelula(0, 0..2, 0..100, query)
        assertEquals result.size(), 1
        Cell celula = result[0] as Cell
        assertEquals celula.getColumnIndex(),1
        assertEquals celula.getRowIndex(), 13

        query = "acetona cruzeiro                 100ML"
        result = excelFile.busqueCelula(0, 0..2, 0..100, query)
        assertEquals result.size(), 1
        celula = result[0] as Cell
        assertEquals celula.getColumnIndex(),1
        assertEquals celula.getRowIndex(), 13

        query = "produto inexistente"
        result = excelFile.busqueCelula(0, 0..2, 0..100, query)
        assertNull result

        query = null
        result = excelFile.busqueCelula(0, 0..2, 0..100, query)
        assertNull result
    }

}