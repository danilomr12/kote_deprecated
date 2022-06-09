package br.com.cotecom.domain.usuarios

import br.com.cotecom.domain.usuarios.empresa.Empresa
import br.com.cotecom.domain.usuarios.empresa.Telefone

class Usuario {

    public static final String REPRESENTANTE = "Representante"
    public static final String SUPERVISOR = "Supervisor"
    public static final String COMPRADOR = "Comprador"
    public static final String ESTOQUISTA = "Estoquista"
    public static final String ADMINISTRADOR = "Administrador"

    protected Usuario() {}

    String nome
    String email
    String password
    boolean habilitado = false
    boolean accountLocked
    boolean accountExpired
    boolean passwordExpired
    boolean isDeletado = false
    boolean newUser = true

    Usuario supervisor
    static transients = ['supervisor', 'tipo']

    Empresa empresa

    static hasMany = [telefones: Telefone]

    static mapping = {
        id generator:'identity'
        telefones column:'usuario_id', cache:true
        email index:'email'
    }
    static constraints = {
        email email:true, unique:true, index:'email_index'
        nome nullable:false, blank:false
        password blank: false, size:5..255, password: true
        empresa nullable:true, cache:true
    }

    Set<Responsabilidade> getResponsabilidades() {
		UsuarioResponsabilidade.findAllByUsuario(this).collect { it.responsabilidade } as Set
	}

    Set<Usuario> getSubordinados() {
        UsuarioSupervisor.findAllBySupervisor(this).collect {it.usuario} as Set
	}

    Usuario getSupervisor() {
		UsuarioSupervisor.findByUsuario(this)?.supervisor
	}

    Boolean addTelefone(Telefone telefone){
        if(!telefone.validate()) return false
        this.addToTelefones(telefone)
        return telefones.contains(telefone)
    }

    Boolean addResponsabilidade(Responsabilidade responsabilidade) {
        if (!this.responsabilidades.contains(responsabilidade)) {
            UsuarioResponsabilidade.create this, responsabilidade
            return true
        }
        return false
    }

    public int hashCode() {
        if (this.id == null)
            return super.hashCode()
        return this.id.hashCode()
    }

    public boolean equals(Object object) {
        if (this.is(object))
            return true
        if ((object == null) || !(object instanceof Usuario))
            return false

        Usuario usuarioToCompare = object as Usuario
        if(this.id.is(usuarioToCompare.id))
            return true
        if((this.id == null) || (usuarioToCompare.id == null))
            return false
        return this.id.equals(object.id)
    }

    public static Usuario queryUserByEmail(String email){
        def comprador = Comprador.findByEmail(email)
        if(comprador)
            return comprador
        def representante = Comprador.findByEmail(email)
        if(representante)
            return representante
        return null
    }

    public String getTipo() {
        if(this.instanceOf(Representante))
            return REPRESENTANTE
        else if (this.instanceOf(Supervisor))
            return SUPERVISOR
        else if (this.instanceOf(Comprador))
            return COMPRADOR
        else if(this.instanceOf(Estoquista))
            return ESTOQUISTA
        else
            return ADMINISTRADOR
    }

    public String toString(){
        return this.id+" - "+this.tipo  + " - " + this.nome
    }
    
}