package br.com.cotecom.domain.error

import org.codehaus.groovy.grails.commons.ConfigurationHolder

abstract class ErrorHandler {

    private static def instance;

    public static def getInstance = {
        if(!instance){
            def defaultErrorHandler = ConfigurationHolder.config.errorHandler.defaultErrorHandler
            instance = ErrorHandlerFactory.getErrorHandler(defaultErrorHandler)
        }
        return instance
    }

    abstract def pushError(error)
    abstract def pushAndDispatchError(error)
    abstract def dispatchErrors()
    abstract def cleanErrors()
    abstract def dispatchErrorsAndClean()



}
