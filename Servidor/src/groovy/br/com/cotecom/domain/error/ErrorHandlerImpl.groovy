package br.com.cotecom.domain.error

/**
 * ErrorHandler Class
 *
 * Para o melhor uso dessa classe, atente-se para as seguintes considerações:
 *
 * 1.  O ErrorHandler é um Singleton, ou seja, existe somente uma instancia do mesmo para toda a aplicacao.
 *     Logo, é necessario usar o método ErrorHandler.getInstance() para obter a referencia unica. Crie entao
 *     um atributo em sua classe para fazer uso do mesmo.
 * 2.
 */

class ErrorHandlerImpl extends ErrorHandler{

    def errorList = new ArrayList<ServerError>()

    def pushError ( error ){
        errorList.add(error)

    }

    def cleanErrors () {
        errorList = new ArrayList<ServerError>()
    }

    def dispatchErrors () {
        return errorList
    }

    def dispatchErrorsAndClean () {
        def errors = dispatchErrors()
        cleanErrors()
        return errors
    }

    def pushAndDispatchError( error ){
        cleanErrors()
        pushError(error)
        return dispatchErrorsAndClean()
    }



}
