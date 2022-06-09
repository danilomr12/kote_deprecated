package br.com.cotecom.domain.error

class ErrorHandlerFactory {

    static final String DEFAULT_ERROR_HANDLER = "DefaultErrorHandler"

    public static def getErrorHandler = { errorHandlerType ->

        switch (errorHandlerType){
            case DEFAULT_ERROR_HANDLER:
                return new ErrorHandlerImpl()
            default:
                return null
        }

    }

}
