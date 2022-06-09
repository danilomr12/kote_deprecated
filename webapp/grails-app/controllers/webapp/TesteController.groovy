package webapp

import org.springframework.util.StopWatch

class TesteController {
	
	def compraService
	
    def index() { 
		println "Obtendo Analise"
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("getAnalise")
			def boo = compraService.getCompra("4f6f34fd84aef42ea9418161")
		stopWatch.stop();
		println "Analise obtida em " + stopWatch.getLastTaskTimeMillis() + "ms"
		println boo.toString()
	}
}
