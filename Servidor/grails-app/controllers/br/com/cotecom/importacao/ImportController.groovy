package br.com.cotecom.importacao

import br.com.cotecom.domain.usuarios.empresa.Cliente
import br.com.cotecom.util.Path.Path

class ImportController {

    def remoteProdutoService
    def produtoService
    def usuarioService
    def index = {}

    static Map allowedMethods = [upload: 'POST']
    
    def enviar = {
        def downloadedfile = request.getFile('arquivo');
        String caminhoPlanilha =  new Path().getPathArquivos() + File.separator + 'listaProdutos.xls'
        downloadedfile.transferTo(new File(caminhoPlanilha))
        def cliente = Cliente.read(params?.empresa)

        if(produtoService.importePlanilhaProdutos(caminhoPlanilha, cliente))
            flash.message = "Produtos importados com sucesso!"
        redirect(controller: "catalogoAdmin")
    }

    def downloadPlanilhaExemplo = {
        redirect(url:remoteProdutoService.donwloadPlanilhaExemplo())
    }
}
