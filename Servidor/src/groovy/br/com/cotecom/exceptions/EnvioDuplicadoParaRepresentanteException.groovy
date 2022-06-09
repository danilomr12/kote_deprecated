package br.com.cotecom.exceptions

public class EnvioDuplicadoParaRepresentanteException extends Exception {


    public EnvioDuplicadoParaRepresentanteException(){
        super("Cotaçao já foi enviada para este respresentante")
    }

}