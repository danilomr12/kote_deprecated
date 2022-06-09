package br.com.cotecom.tests.fixtures

import br.com.cotecom.domain.usuarios.Administrador
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.Supervisor
import grails.test.GrailsUnitTestCase

public class UsuarioFixtureTests extends GrailsUnitTestCase {

    void testUsuariosBuild(){
        assertNotNull Administrador.build()
        assertNotNull Comprador.build()
        assertNotNull Representante.build()
        assertNotNull Supervisor.build()
    }

}