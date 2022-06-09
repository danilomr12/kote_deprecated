package br.com.cotecom.util.documents.excel

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.Font

public class Celula {

   Cell cell

    public def getCellValue = { Cell cell ->
        if(!cell || cell.equals(null))
            return null
        else if (cell.getCellType() == Cell.CELL_TYPE_STRING)
            return cell.getStringCellValue()
        else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                if(DateUtil.isCellDateFormatted(cell))
                    return cell.getDateCellValue()
                double value = cell.getNumericCellValue()
                long valorTruncado = value as long
                return value % valorTruncado == 0 ? valorTruncado : value
        }else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
            return cell.getBooleanCellValue()
        else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA)
            return cell.getCellFormula()
        return null
    }

    public def setFont = { Cell cell, Font font ->
        CellStyle cellStyle = cloneCellStyle(cell)
        cellStyle.setFont(font)
        cell.setCellStyle(cellStyle)
    }

    public def setFillColor = {  Cell cell, short colorIndex ->
        CellStyle cellStyle = cloneCellStyle(cell)
        cellStyle.setFillForegroundColor(colorIndex)
        cellStyle.setFillPattern(cellStyle.SOLID_FOREGROUND)
        cell.setCellStyle(cellStyle)
    }

    public def setLocked = {  Cell cell, boolean locked->
        CellStyle cellStyle = cloneCellStyle(cell)
        cellStyle.setLocked(locked)
        cell.setCellStyle(cellStyle)
    }

    def setAligment(Cell cell, short aligmentIndex) {
        CellStyle cellStyle = cloneCellStyle(cell)
        cellStyle.setAlignment(aligmentIndex)
        cell.setCellStyle(cellStyle)
    }

    private CellStyle cloneCellStyle(Cell cell) {
        CellStyle cellStyle = cell.getRow().getSheet().getWorkbook().createCellStyle()
        cellStyle.cloneStyleFrom(cell.getCellStyle())
        return cellStyle
    }

    def setBorder(Cell cell, short borderStyle, short borderColor) {
        CellStyle cellStyle = cloneCellStyle(cell)
        cellStyle.setBorderBottom(borderStyle)
        cellStyle.setBottomBorderColor(borderColor)
        cellStyle.setBorderRight(borderStyle)
        cellStyle.setRightBorderColor(borderColor)
        cellStyle.setBorderTop(borderStyle)
        cellStyle.setTopBorderColor(borderColor)
        cellStyle.setBorderLeft(borderStyle)
        cellStyle.setLeftBorderColor(borderColor)
        cell.setCellStyle(cellStyle)
    }
}