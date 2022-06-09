package br.com.cotecom.resposta

import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.util.Path.Path
import br.com.cotecom.domain.cotacao.Cotacao

class RespostaController {

    def index = {
        render view:'index', params:params
    }

    def downloadExcelCotacao = {
        if(params.id){
            File file = new File(new Path().getPathArquivosExcelRespostas() + File.separator + params.id +".xls")
            def cotacao = Resposta.read(params.id)?.cotacao
            def cliente = cotacao?.empresa?.nomeFantasia
            response.contentType = "application/vnd.ms-excel"
            response.setHeader("Content-disposition", "attachment;filename=\"Cotação ${cliente} - ${formateDataBrazil(cotacao.dataCriacao)}.xls\"");
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
}
