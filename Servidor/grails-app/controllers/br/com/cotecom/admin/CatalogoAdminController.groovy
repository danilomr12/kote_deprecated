package br.com.cotecom.admin

import br.com.cotecom.domain.item.Produto

class CatalogoAdminController {

    def index = {}

    def reindexCatalogo = {
        Produto.reindex()
        flash.message = "Catálogo reindexado"
        redirect(action: "index")
    }



}
