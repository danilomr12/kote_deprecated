package br.com.cotecom.util.documents.excel

import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.*

public class ExcelFile {

    private static final log = Logger.getLogger(ExcelFile.class)

    private Celula celula = new Celula()
    private Workbook workbook
    private String caminho

    public ExcelFile(String caminhoPlanilha){
        def length = caminhoPlanilha.length()
        if(caminhoPlanilha.substring(length-3, length) == "xls"){
            this.workbook = new HSSFWorkbook(new FileInputStream(caminhoPlanilha))
            this.caminho = caminhoPlanilha
        }else{
            log.debug "O caminho deve apontar um arquivo com extensão xls"
        }
    }

    public ExcelFile(){
        this.workbook = new HSSFWorkbook()
    }

    public Sheet getSpreadSheet(Integer numeroPlanilha) {
        return workbook.getSheetAt(numeroPlanilha)
    }

    public Workbook getWorkbook(){
        return this.workbook
    }

    public def getCellValueFirstPlan(Cell cell){
        return celula.getCellValue(cell)
    }

    public Iterator getRowIteratorFromSheet(Integer numeroPlanilha) {
        return getSpreadSheet(numeroPlanilha).rowIterator()
    }

    public def getCellValue(int numPlanilha, int numColuna, int numLinha){
        return celula.getCellValue(workbook?.getSheetAt(numPlanilha)?.getRow(numLinha)?.getCell(numColuna))
    }

    public def getCellValue(Sheet planilha, int numColuna, int numLinha){
        return celula.getCellValue(planilha.getRow(numLinha).getCell(numColuna))
    }

    public Sheet criePlanilha(String nomePlanilha){
        return this.workbook.createSheet(nomePlanilha)
    }

    public Sheet getPlanilha(int indice){
        return workbook.getSheetAt(indice)
    }

    public Sheet getPlanilha(String nome){
        return workbook.getSheet(nome)
    }

    public Sheet criePlanilha(){
        return this.workbook.createSheet()
    }

    public void setFonte(Cell cell, Font fonte){
        celula.setFont(cell, fonte)
    }

    public void setFonte(IntRange rowRange, IntRange columnRange, Sheet sheet, Font font) {
        rowRange.each {int rowNum ->
            Row row = sheet?.getRow(rowNum)
            if(!row)
                row = sheet.createRow(rowNum)
            columnRange.each {int columnNum ->
                Cell cell = row?.getCell(columnNum)
                if(!cell)
                    cell = row.createCell(columnNum)
                setFonte(cell, font)
            }
        }
    }

    public void setBorder(Cell cell, Short borderStyle, Short borderColor){
        celula.setBorder(cell, borderStyle, borderColor)
    }

    public void setBorder(IntRange rowRange, IntRange columnRange, Sheet sheet, Short borderStyle, Short borderColor){
        rowRange.each {int rowNum ->
            Row row = sheet?.getRow(rowNum)
            if(!row)
                row = sheet.createRow(rowNum)
            columnRange.each {int columnNum ->
                Cell cell = row?.getCell(columnNum)
                if(!cell)
                    cell = row.createCell(columnNum)
                setBorder(cell, borderStyle, borderColor)
            }
        }

    }

    public void setBackgroundColor(Cell cell, short color){
        celula.setFillColor(cell, color)
    }

    void setBackgroundColor(IntRange rowRange, IntRange columnRange, Sheet sheet, short colorIndex) {
        rowRange.each {int rowNum ->
            Row row = sheet?.getRow(rowNum)
            if(!row)
                row = sheet.createRow(rowNum)
            columnRange.each {int columnNum ->
                Cell cell = row?.getCell(columnNum)
                if(!cell)
                    cell = row.createCell(columnNum)
                setBackgroundColor(cell,colorIndex)
            }
        }
    }

    def setRegionStyle(Sheet sheet, IntRange rowRange, IntRange columnRange, CellStyle cellStyle) {
        rowRange.each {int rowNum ->
            Row row = sheet?.getRow(rowNum)
            if(!row)
                row = sheet.createRow(rowNum)
            columnRange.each {int columnNum ->
                row?.getCell(columnNum)?.setCellStyle(cellStyle)
            }
        }
    }

    public void setLocked(Cell cell, boolean locked){
        celula.setLocked(cell, locked)
    }

    void setLocked(IntRange columnRange, IntRange rowRange, Sheet planilha, boolean locked) {

        rowRange.each {int rowNum ->
            Row row = planilha?.getRow(rowNum)
            if(!row)
                row = planilha.createRow(rowNum)
            columnRange.each {int columnNum ->
                Cell cell = row?.getCell(columnNum)
                if(!cell)
                    cell = row.createCell(columnNum)
                setLocked cell,locked
            }
        }
    }

    public void setAligment(Cell cell, short aligmentIndex) {
        celula.setAligment(cell, aligmentIndex)
    }

    public void setAligment(IntRange rowRange, IntRange columnRange, Sheet sheet, short aligmentIndex) {
        rowRange.each {int rowNum ->
            Row row = sheet?.getRow(rowNum)
            if(!row)
                row = sheet.createRow(rowNum)
            columnRange.each {int columnNum ->
                Cell cell = row?.getCell(columnNum)
                if(cell)
                    setAligment(cell,aligmentIndex)
            }
        }
    }

    public String graveArquivo(String caminhoCompleto){
        FileOutputStream stream = new FileOutputStream(caminhoCompleto)
        workbook.write(stream)
        stream.flush()
        stream.close()
        this.caminho = caminhoCompleto
        return caminhoCompleto
    }

    public boolean delete(){
        if(caminho != "" && caminho != null){
            File file = new File(caminho)
            return file.delete()
        }
        else return false
    }

    def busqueCelula(int planilha, IntRange columnRange, IntRange rowRange, def query) {
        List<Cell> celulasEncontradas = new ArrayList()
        if(query==null)
            return null
        for(i in columnRange){
            for(j in rowRange){
                def cell = getPlanilha(planilha)?.getRow(j)?.getCell(i)
                if(query instanceof String || query instanceof GString){
                    def cellValue = getCellValueFirstPlan(cell) as String
                    if(query.replaceAll(/\s+/, " ")?.replaceAll("\\u00a0"," ")?.equalsIgnoreCase(cellValue?.replaceAll(/\s+/, " ")?.replaceAll("\\u00a0"," ")))
                        celulasEncontradas.push(cell)
                }else if(getCellValueFirstPlan(cell) ==  query){
                    celulasEncontradas.push(cell)
                }
            }
        }
        if(celulasEncontradas.size()>=1)
            return celulasEncontradas
        return null
    }

    Sheet insiraImagem(Sheet planilha, String caminhoImagem, int primeiraLinhaImagem, int primeiraColunaImagem){
        InputStream is = new FileInputStream(caminhoImagem);
        byte[] bytes = IOUtils.toByteArray(is);
        int pictureIdx = planilha.getWorkbook().addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_PNG);
        is.close();
        CreationHelper helper = this.workbook.getCreationHelper();
        // Create the drawing patriarch.  This is the top level container for all shapes.
        Drawing drawing = planilha.createDrawingPatriarch();
        //add a picture shape
        ClientAnchor anchor = helper.createClientAnchor();
        //set top-left corner of the picture,
        //subsequent call of Picture#resize() will operate relative to it
        anchor.setCol1(primeiraColunaImagem);
        anchor.setRow1(primeiraLinhaImagem);
        Picture pict = drawing.createPicture(anchor, pictureIdx);
        //auto-size picture relative to its top-left corner
        pict.resize();
        return planilha
    }
}