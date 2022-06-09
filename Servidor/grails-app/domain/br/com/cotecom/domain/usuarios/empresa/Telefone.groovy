package br.com.cotecom.domain.usuarios.empresa

import org.apache.commons.lang.builder.HashCodeBuilder

public class Telefone {

    static final int CELULAR = 0
    static final int FAX = 1
    static final int COMERCIAL = 2
    static final int RESIDENCIAL = 3

    static final Map tipos = [0: "Celular", 1: "Fax", 2: "Fixo", 3: "Residencial"]
    
    int tipo
    String ddd
    String numero

     static mapping = {
        id generator:'identity'
    }
    static constraints = {
        ddd(nullable: false, blank: false)
        numero(nullable: false, blank: false)
        tipo(inList:[0,1,2,3])
    }

    public getTipo(int tipo) {
        return tipos[tipo]
    }

    public int hashCode() {
        def builder = new HashCodeBuilder()
		if (ddd) builder.append(this.ddd)
		if (numero) builder.append(this.numero)
        if (tipo) builder.append(this.tipo)
		builder.toHashCode()
    }

    public boolean equals(Object object) {
        if (this.is(object))
            return true
        if ((object == null) || !(object instanceof Telefone))
            return false

        Telefone telefoneToCompare = object as Telefone
        if(this.id.is(telefoneToCompare.id))
            return true
        if((this.id == null) || (telefoneToCompare.id == null))
            return false
        return this.id.equals(object.id)
    }

    public String toString(){
        if(this.numero == null && this.ddd == null)
            return ""
        return "($ddd)${numero}"
    }
                                       
    static public Telefone buildFrom(String numero){
        if((numero ==~ /[0-9]{2}-[0-9]{8}/) || (numero ==~ /[0-9]{2}-[0-9]{7}/))
            return new Telefone(ddd:numero.substring(0,2), numero:numero.substring(3))
        return null
    }

}
