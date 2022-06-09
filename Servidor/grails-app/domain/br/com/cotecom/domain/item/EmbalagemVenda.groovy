package br.com.cotecom.domain.item

import org.compass.annotations.Searchable
import org.apache.log4j.Logger
import org.compass.annotations.SearchableProperty
import org.compass.annotations.SearchableComponent

@Searchable(root = false)
public class EmbalagemVenda {

    private static final log = Logger.getLogger(EmbalagemVenda.class)


    @SearchableComponent(prefix = 'embVenda')
    TipoEmbalagem tipoEmbalagemDeVenda
    @SearchableComponent(prefix = 'embUnidade')
    TipoEmbalagem tipoEmbalagemUnidade
    static embedded = ['tipoEmbalagemDeVenda','tipoEmbalagemUnidade']


    @SearchableProperty
    Integer qtdeDeUnidadesNaEmbalagemDeVenda

    Produto produto
    static mapping = {

  tipoEmbalagemUnidade column:'embalagem_unidade'
        tipoEmbalagemDeVenda column:'embalagem_venda'

  qtdeDeUnidadesNaEmbalagemDeVenda column:'qtd_embalagem'
    }
    static constraints = {

tipoEmbalagemDeVenda(nullable:false)
        tipoEmbalagemUnidade(nullable:false)

qtdeDeUnidadesNaEmbalagemDeVenda(min:1)
    }

    public static EmbalagemVenda setEmbalagem(String embalagem) {
        //Expressao regular em groovy para validar o formato da embalagem -> CX/0012/UN
        def p = "[a-zA-Z]{2}/[0-9][0-9]?[0-9]?[0-9]?/[a-zA-Z]{2}"
        if(embalagem==null||embalagem==""||embalagem=="null"||!(embalagem==~p)){
            return null
        }

        EmbalagemVenda newEmbalagem = new EmbalagemVenda(tipoEmbalagemUnidade: new TipoEmbalagem(),
                tipoEmbalagemDeVenda: new TipoEmbalagem())
        int indicePrimeiraBarraNaString = 0 ,indiceSegundaBarraNaString = 0

        indicePrimeiraBarraNaString = getIndicePrimeiraBarra(embalagem)
        indiceSegundaBarraNaString = getIndiceSegundaBarraNaString(indicePrimeiraBarraNaString, embalagem)

        newEmbalagem.tipoEmbalagemDeVenda.descricao = embalagem.getAt(0..indicePrimeiraBarraNaString-1)

        newEmbalagem.qtdeDeUnidadesNaEmbalagemDeVenda = embalagem.getAt(indicePrimeiraBarraNaString + 1
                ..indiceSegundaBarraNaString-1).toInteger()

        newEmbalagem.tipoEmbalagemUnidade.descricao = embalagem.getAt(indiceSegundaBarraNaString +
                1..embalagem.length()-1)


        return newEmbalagem

    }

    private static int getIndiceSegundaBarraNaString(int indicePrimeiraBarraNaString, String embalagem) {
        int indiceSegundaBarraNaString = 0

        while (!embalagem.substring(indicePrimeiraBarraNaString + 1).getAt(indiceSegundaBarraNaString).equals("/")) {
            indiceSegundaBarraNaString++
        }

        indiceSegundaBarraNaString = indicePrimeiraBarraNaString + indiceSegundaBarraNaString + 1
        return indiceSegundaBarraNaString
    }

    private static int getIndicePrimeiraBarra(String embalagem) {
        int indicePrimeiraBarraNaString = 0
        while (!embalagem.getAt(indicePrimeiraBarraNaString).equals("/")) {
            indicePrimeiraBarraNaString++
        }
        return indicePrimeiraBarraNaString
    }

    public String toString(){
        String resultado
        if(this.tipoEmbalagemDeVenda.descricao == null &&
                qtdDaEmbalagemToString() == null &&
                this.tipoEmbalagemUnidade.descricao == null){
            resultado = "CX/0001/UN"
        }else{
            resultado = this.tipoEmbalagemDeVenda.descricao+"/"+qtdDaEmbalagemToString()+"/"+this.tipoEmbalagemUnidade.descricao
        }
        return resultado
    }

    private String qtdDaEmbalagemToString(){
        String qtd = this.qtdeDeUnidadesNaEmbalagemDeVenda.toString()
        String zerosAEsquerda = ""
        int i=0
        int numeroDeZeros = 4-qtd.length()
        (numeroDeZeros).times {
            zerosAEsquerda += "0"
            i++
        }
        return zerosAEsquerda.concat(qtd)
    }
}