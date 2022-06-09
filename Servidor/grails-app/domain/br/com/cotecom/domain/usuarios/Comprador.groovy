package br.com.cotecom.domain.usuarios

import br.com.cotecom.domain.usuarios.empresa.Cliente

public class Comprador extends Usuario implements Serializable {

    static hasMany = [clientes: Cliente]

    static mappedBy = [clientes: 'comprador']

    static mapping = {
       clientes column: 'comprador_empresa_id'
    }

    public String toString(){
        super.toString()
    }
}