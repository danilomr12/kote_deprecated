package br.com.cotecom.util

import cr.co.arquetipos.password.PasswordTools

public class UrlDigestCodec {

    static encode = { str ->
        return PasswordTools.saltPasswordBase64(str)

    }

    static Boolean check(String texto, String hash){
        return PasswordTools.checkDigestBase64(texto, hash)
    }

}