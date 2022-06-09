package br.com.cotecom

class IndexController {

    def index = {
        redirect(controller: "login")
       // render(view: "../index")
    }
}
