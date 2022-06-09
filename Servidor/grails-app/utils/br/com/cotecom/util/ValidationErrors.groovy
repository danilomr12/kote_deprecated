package br.com.cotecom.util

class ValidationErrors {

    public static def setErrorFromUnformatedMessage(error, originalMessage){
        error.mensagem = getMessage(originalMessage)
        error.id = getCode(originalMessage).toInteger()
    }

    private static def getMessage = { String originalMessage -> originalMessage.replaceFirst(/([0-9]+)-/, "")}

    private static def getCode = { String originalMessage ->
        String code = ""
        (originalMessage =~ /[0-9]+/).eachWithIndex{fullMatch, int index ->
            if(index == 0)
                code = "${fullMatch}"
        }
        return (code == "") ? 0 : code.toInteger()
    }

}
