package br.com.cotecom.domain.usuarios.empresa

import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.Supervisor
import br.com.cotecom.domain.usuarios.Usuario

public class Fornecedor extends Empresa{

    List<Usuario> getSupervisores(){
        Supervisor.findAllByEmpresa(this)
    }

    List<Usuario> getRepresentantes(){
        Representante.findAllByEmpresa(this)
    }

    static constraints = {
        email(email: true,unique: true, nullable: true, blank: true)
        endereco(nullable: true)
        usuarios(nullable: true, size: 0..10)
        categorias(nullable: true, size: 0..10)
    }

    public String toString(){
        super.toString()
    }

}