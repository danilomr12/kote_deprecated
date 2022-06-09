package br.com.cotecom.domain.error;

import br.com.cotecom.util.ValidationErrors;

public class ServerError {
    public static final int DEFAULT_ID = 0;
    public static final int UNSUPORTED_OPERATION_ID = 1;

    public int id;
    public String acao;
    public String mensagem;
    public String causa;

    public ServerError(){
        
    }

    public ServerError(String unformatedMessage){
        ValidationErrors.setErrorFromUnformatedMessage(this, unformatedMessage);
    }

}
