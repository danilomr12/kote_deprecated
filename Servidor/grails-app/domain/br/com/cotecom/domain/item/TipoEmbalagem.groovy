package br.com.cotecom.domain.item

import org.compass.annotations.Searchable
import org.compass.annotations.SearchableProperty

@Searchable(root = false)
public class TipoEmbalagem {

    static final String CX = "CX"
    static final String PT = "PT"
    static final String UN = "UN"
    static final String DP = "DP"
    static final String LT = "LT"
    static final String GA = "GA"
    static final String VD = "VD"
    static final String PO = "PO"
    static final String SC = "SC"
    static final String FD = "FD"
    static final String KG = "KG"

    @SearchableProperty
    String descricao

    String toString(){
        return descricao
    }

    boolean equals(TipoEmbalagem outroTipoEmbalagem){
        if(this.descricao.equalsIgnoreCase(outroTipoEmbalagem.descricao))
            return true
        return false
    }

}