package br.com.cotecom.tests.fixtures

import br.com.cotecom.domain.item.ItemResposta
import grails.test.GrailsUnitTestCase

public class BuildTestDataTests extends GrailsUnitTestCase {

    def grailsApplication 

    void testBuildAllDomains() {
        def successful = true
        grailsApplication.domainClasses.each { domainClass ->
            println "Test of ${domainClass.name}.build()"
            try {
                def domainObject = domainClass.clazz.build()
                assertNotNull domainObject."${domainClass.identifier.name}"
                println "********** SUCCESSFUL BUILD OF $domainClass"
            } catch (Exception e) {
                println "********** FAILED BUILD OF $domainClass"
                successful = false
            }
        }
        assert successful
    }

    void testBuildItemResposta() {
        def itemResposta = ItemResposta.build()
        assertNotNull itemResposta
    }

}