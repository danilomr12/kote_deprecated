package br.com.cotecom.tests.util

import br.com.cotecom.util.Path.Path
import br.com.cotecom.util.documents.excel.ExcelExport
import br.com.cotecom.util.documents.excel.ExcelFile
import grails.test.GrailsUnitTestCase

class ExcelExportTests extends GrailsUnitTestCase {


    void setUp(){
        super.setUp()
    }

    void tearDown(){
        super.tearDown()
    }

    void testeCrieTemplatEkOTE(){
        ExcelExport  excelExport = new ExcelExport()
        ExcelFile excelFile = new ExcelFile()
        excelFile = excelExport.crieTemplateKote(excelFile.criePlanilha())

        assertNotNull excelFile.graveArquivo(new Path().getPathArquivosDeTeste()+File.separator+"teste_imagem.xls")
        assertTrue excelFile.delete()
    }

}
