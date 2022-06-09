package br.com.cotecom.domain.usuarios.empresa

import br.com.cotecom.domain.usuarios.Comprador

public class Cliente extends Empresa {

    Comprador comprador

    static constraints = {
        comprador(nullable: true)
    }
    Set<Atendimento> getAtendimentos(){
        return Atendimento.findAllByCliente(this)
    }

    public String toString(){
        super.toString()
    }

    }