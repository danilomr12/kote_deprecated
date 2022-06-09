package br.com.cotecom.pedido

import br.com.cotecom.util.Path.Path
import br.com.cotecom.domain.pedido.Pedido
import br.com.cotecom.util.documents.excel.ExcelExport

class PedidoController {

    def cotacaoService

    def index = { }

    def exportePedidoExcel = {
        Pedido pedido = Pedido.findByPedidoUrlDigest(params.digest)
        if(pedido){
            def excel = cotacaoService.exportePedidoExcel(pedido?.id)
            if(excel){
                File file = new File(excel)
                String nomeFantasiaCliente = pedido.resposta.cotacao.empresa.nomeFantasia
                def dataCotacao = pedido.resposta.cotacao.dataCriacao
                String fileName = "Ordem de compra " + nomeFantasiaCliente + " " + String.format('%td-%<tm-%<tY', dataCotacao) + ".xls"
                response.contentType = "application/vnd.ms-excel"
                response.setHeader("Content-disposition", "attachment;\"filename = ${fileName}\"");
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
