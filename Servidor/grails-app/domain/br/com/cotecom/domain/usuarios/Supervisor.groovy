package br.com.cotecom.domain.usuarios

class Supervisor extends Usuario{

    Boolean addRepresentante(Representante representante){
        if(!representante.empresa || !this.empresa || this.empresa == representante.empresa){
            UsuarioSupervisor.create(representante, this, true)
            return true
        }
        return false
    }

    public String toString(){
        super.toString()
    }

}