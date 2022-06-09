package br.com.cotecom.domain.usuarios.empresa

import org.apache.log4j.Logger

class Categoria {

    private static final log = Logger.getLogger(Categoria.class)

    static final String PERFUMARIA = "Perfumaria"
    static final String ALIMENTICIOS = "Alimentícios"
    static final String BEBIDAS = "Bebidas"
    static final String HIGIENE_PESSOAL = "Higiene Pessoal"

    String tipo

    static mapping = {
        tipo column: 'categoria'
    }
    static constraints = {
        tipo nullable:false
    }
}