package br.com.cotecom.domain.usuarios

class Responsabilidade {

	String descricao
	String responsabilidade

    static final String ROLE_COMPRADOR = "ROLE_COMPRADOR"
    static final String ROLE_REPRESENTANTE = "ROLE_REPRESENTANTE"
    static final String ROLE_ESTOQUISTA = "ROLE_ESTOQUISTA"
    static final String ROLE_ADMIN = "ROLE_ADMIN"


    static mapping = {
        id generator:'identity'
    }
	static constraints = {
		responsabilidade blank: false, unique: true
		descricao nullable:false, blank:false
	}
}

