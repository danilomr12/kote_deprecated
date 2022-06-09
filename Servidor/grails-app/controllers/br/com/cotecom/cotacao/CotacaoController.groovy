package br.com.cotecom.cotacao

import br.com.cotecom.util.Path.Path
import br.com.cotecom.domain.pedido.Pedido

class CotacaoController {

    def cotacaoService
    def compraService
    def index = { }

    def downloadExcelAnalise = {
        if(params.id){
            File file = new File(new Path().getPathArquivosExcelAnalises() + File.separator + params.id +".xls")
            response.contentType = "application/vnd.ms-excel"
            def dataCotacao = compraService.getCompra(params.id)?.dataCriacao
            response.setHeader("Content-disposition", "attachment;filename=\"Análise Cotação ${formateDataBrazil(dataCotacao)}.xls\"");
            response.setHeader("Content-Length", "${file.size()}")
            response.outputStream << file.readBytes()
            response.outputStream.flush()
            response.outputStream.close()
            file.delete()
        }else{
            render "Arquivo não encontrado!"
        }
    }

    private String formateDataBrazil(Date data) {
        return String.format('%td-%<tm-%<tY', data)
    }

    def exportePedidoExcel = {
        if(params.id){
            def excel = cotacaoService.exportePedidoExcel(params.id as Long)
            if(excel){
                File file = new File(excel)
                Pedido pedido = Pedido.read(params.id)
                String nomeFantasiaCliente = pedido.resposta.cotacao.empresa.nomeFantasia
                String fileName = "Ordem de compra " + nomeFantasiaCliente + " " + String.format('%td-%<tm-%<tY', new Date()) + ".xls"
                response.contentType = "application/octet-stream"
                response.setHeader("Content-disposition", "attachment; filename = ${fileName}");
                response.setHeader("Content-Length", "${file.size()}")
                response.outputStream << file.readBytes()
                file.delete()
            }else{
                render "Não foi possivel gerar o pedido"

            }
        }else{
            render "Pedido não encontrado!"
        }
    }
}
