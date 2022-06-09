package br.com.cotecom.domain.usuarios

class Requestmap {

	String url
	String configAttribute

    // TODO Mudar estratégia de validação de url
    static mapping = {
        id generator:'identity'
    }
	static constraints = {
		url(blank: false, unique: true)
		configAttribute(blank: false)
	}
}
