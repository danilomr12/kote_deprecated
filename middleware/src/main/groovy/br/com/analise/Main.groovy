package br.com.analise

import org.springframework.context.ApplicationContext
import org.springframework.context.support.GenericXmlApplicationContext

class Main {

	static main(args) {
		ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml")
	}

}
