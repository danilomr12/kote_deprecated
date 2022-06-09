package br.com.cotecom.tests.codec

import br.com.cotecom.util.UrlDigestCodec
import grails.test.GrailsUnitTestCase

public class SecureCodecTests extends GrailsUnitTestCase{

    void setUp(){
        super.setUp()
    }

    void tearDown(){
        super.tearDown()
    }

    void testSecureCodec() {
        def original = "secrety"
        def encoded = original.encodeAsUrlDigest()
        assertTrue UrlDigestCodec.check(original, encoded)                          
    }
}