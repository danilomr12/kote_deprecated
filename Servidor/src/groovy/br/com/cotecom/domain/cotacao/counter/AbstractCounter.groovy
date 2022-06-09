package br.com.cotecom.domain.cotacao.counter

import br.com.cotecom.domain.cotacao.ICotacao

public abstract class AbstractCounter {
    
    ICotacao cotacao

    Integer recebido
    Integer faltando
}