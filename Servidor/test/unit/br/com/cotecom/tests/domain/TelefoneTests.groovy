package br.com.cotecom.tests.domain

import br.com.cotecom.domain.usuarios.empresa.Telefone

public class TelefoneTests extends GroovyTestCase{

    void testBuildTelefoneByNumero(){
        def numero1 = "62-99341234"
        def numero2 = "63-9934123"
        def numeroErrado = "939341234"

        assertNull Telefone.buildFrom(numeroErrado)
        assertEquals '62', Telefone.buildFrom(numero1).ddd
        assertEquals '63', Telefone.buildFrom(numero2).ddd
        assertEquals '99341234', Telefone.buildFrom(numero1).numero
        assertEquals '9934123', Telefone.buildFrom(numero2).numero
    }


}