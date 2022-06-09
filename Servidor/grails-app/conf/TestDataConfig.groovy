import br.com.cotecom.domain.cotacao.EstadoCotacao
import br.com.cotecom.domain.item.TipoEmbalagem
import br.com.cotecom.domain.resposta.EstadoResposta

testDataConfig {
    sampleData {
        'br.com.cotecom.domain.cotacao.Cotacao' {
            codigoEstadoCotacao = EstadoCotacao.RASCUNHO
            estado = EstadoCotacao.estado.get(EstadoCotacao.RASCUNHO)
        }

        'br.com.cotecom.domain.usuarios.Supervisor'{
            def i = 1
            email = {->"supervisor${i++}@domain.com"}
        }

        'br.com.cotecom.domain.usuarios.Administrador'{
            def i = 1
            email = {->"administrador${i++}@domain.com"}
        }

        'br.com.cotecom.domain.usuarios.Comprador'{
            def i = 1
            email = {->"comprador${i++}@domain.com"}
        }

        'br.com.cotecom.domain.usuarios.Representante'{
            def i = 1
            email = {->"representante${i++}@domain.com"}
        }

        'br.com.cotecom.domain.usuarios.Cliente'{
            def i = 1
            email = {->"cliente${i++}@domain.com"}
        }
        
        'br.com.cotecom.domain.usuarios.empresa.Fornecedor'{
            def i = 1
            email = {->"fornecedor${i++}@domain.com"}
        }

        'br.com.cotecom.domain.usuarios.empresa.CoteCom'{
            def i = 1
            email = {->"cotecom${i++}@domain.com"}
        }

        'br.com.cotecom.domain.resposta.Resposta' {
            codigoEstado = EstadoResposta.NOVA_COTACAO
            estado = EstadoResposta.estado.get(EstadoCotacao.RASCUNHO)
            itens = new ArrayList()
        }

        'br.com.cotecom.domain.usuarios.Responsabilidade' {
            def i = 1
            responsabilidade = {-> "Responsabilidade ${i++}"}
        }

        'br.com.cotecom.domain.item.EmbalagemVenda' {
            tipoEmbalagemUnidade = new TipoEmbalagem(descricao:TipoEmbalagem.UN)
            tipoEmbalagemDeVenda   = new TipoEmbalagem(descricao:TipoEmbalagem.VD)
        }
    
    }
}

environments {
    development {
        testDataConfig {

            sampleData {

                'br.com.cotecom.domain.usuarios.Representante'{
                    def i = 1
                    email = {->"representante${i++}@domain.com"}
                    nome = {->"Representante ${i++}"}
                }

                'br.com.cotecom.domain.usuarios.Comprador'{
                    def i = 1
                    email = {->"comprador${i++}@domain.com"}
                    nome = {->"Comprador ${i++}"}
                }


            }

        }
    }
}