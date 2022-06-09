package br.com.cotecom.services.remote

import br.com.cotecom.domain.cotacao.Cotacao
import br.com.cotecom.domain.cotacao.ICotacao
import br.com.cotecom.domain.dto.analise.AnaliseCotacaoDTO
import br.com.cotecom.domain.dto.cotacao.CotacaoDTO
import br.com.cotecom.domain.dto.cotacao.ItemCotacaoDTO
import br.com.cotecom.domain.dto.tela.TelaCotacaoDTO
import br.com.cotecom.domain.error.ErrorHandler
import br.com.cotecom.domain.error.ServerError
import br.com.cotecom.domain.item.ItemCotacao
import br.com.cotecom.domain.pedido.Pedido
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.empresa.Endereco
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.perf4j.StopWatch
import org.perf4j.log4j.Log4JStopWatch
import br.com.cotecom.dtos.assembler.*

import br.com.cotecom.domain.dto.analise.ItemAnaliseCotacaoDTO

public class RemoteCotacaoService {

    private static final log = Logger.getLogger(RemoteCotacaoService.class)

    boolean transactional = true
    static expose = ["flex-remoting"]
    def sessionFactory

    def cotacaoService
    def fileService
    def config = ConfigurationHolder.config
    def usuarioService
    ErrorHandler errorHandler = ErrorHandler.getInstance()
    def backgroundService
    def compraService

    def getCotacao(Long id) {
        ICotacao cotacao = cotacaoService.getCotacao(id)
        return CotacaoAssembler.crieTelaCotacaoDTO(cotacao)
    }

    def saveCotacao(TelaCotacaoDTO novaCotacaoDTO) {
        Cotacao cotacao = CotacaoAssembler.crieCotacao(novaCotacaoDTO.cotacaoDTO)

        novaCotacaoDTO.itensCotacaoDTO?.each { ItemCotacaoDTO itemCotacaoDTO ->
            if (itemCotacaoDTO.id != null && !itemCotacaoDTO.saved)
                atualizeItemCotacao(itemCotacaoDTO)
            if (itemCotacaoDTO.id == null) {
                ItemCotacao item = CotacaoAssembler.crieItemCotacao(itemCotacaoDTO)
                item.cotacao = cotacao
                cotacao.addItemCotacao item
                item.save(flush:true)
            }
        }
        cotacao = cotacaoService.salve(cotacao)

        if (cotacao != null) {
            novaCotacaoDTO.itensCotacaoDTO = CotacaoAssembler.crieItensCotacaoDTO(cotacao.itens)
            novaCotacaoDTO.itensCotacaoDTO.each {it.saved = true}
            novaCotacaoDTO.cotacaoDTO = CotacaoAssembler.crieCotacaoDTO(cotacao)
            return novaCotacaoDTO
        }
        return null
    }

    private void atualizeItemCotacao(ItemCotacaoDTO itemCotacaoDTO) {
        ItemCotacao itemParaAtualizar = ItemCotacao.get(itemCotacaoDTO.id)
        itemParaAtualizar.quantidade = itemCotacaoDTO.quantidade;
    }

    public ItemCotacaoDTO saveItemCotacao(ItemCotacaoDTO itemCotacaoDTO) {
        ItemCotacao itemParaAtualizar = ItemCotacao.get(itemCotacaoDTO.id)
        itemParaAtualizar.quantidade = itemCotacaoDTO.quantidade;
        if(itemParaAtualizar.save()){
            itemCotacaoDTO.saved = true
            return itemCotacaoDTO
        }
        return itemCotacaoDTO
    }

    boolean cancele(Long cotacaoId) {
        if (cotacaoService.cancele(cotacaoId))
            return true
        return false
    }

    def finalizeCotacao(Long cotacaoId) {
        cotacaoService.finalizeCotacao(Cotacao.get(cotacaoId))
    }

    Boolean reenvieEmailCotacao(Long respostaId) {
        return cotacaoService.reenvieEmailCotacao(respostaId)
    }

    private String getServerRootUrl(String serverURL) {
        String root = ""
        int barrasCout = 0
        serverURL.each {
            if (it == "/")
                barrasCout++
            if (barrasCout < 3)
                root += it
        }
        return root
    }

    Boolean possuiNovasRespostas() {
        def comprador = usuarioService.sessionUser as Comprador
        log.debug("comprador logado:" + comprador)
        List<Cotacao> cotacoes = Cotacao.createCriteria().list {
            'in'('empresa', comprador?.clientes)
            order("dataCriacao", "desc")
        }
        for (cotacao in cotacoes) {
            for (resposta in cotacao.respostas) {
                if (!resposta.lida) {
                    return true
                }
            }
        }
        return false
    }

    Boolean possuiNovasRespostasDaCotacao(Long cotacaoId) {
        def cotacao = Cotacao.get(cotacaoId)
        log.debug("cotação:" + cotacao + " do comprador : "+ usuarioService.sessionUser)
        for (resposta in cotacao.respostas) {
            if (!resposta.lida) {
                return true
            }
        }
        return false
    }

    CotacaoDTO crieNovaCotacaoRascunho() {
        def cotacaoDTO = new CotacaoDTO()
        cotacaoDTO.dataCriacao = new Date()
        cotacaoDTO.dataEntrega = new Date()
        def user = usuarioService.getSessionUser()
        cotacaoDTO.empresaId = user?.empresa?.id
        def cotacao = CotacaoAssembler.crieCotacao(cotacaoDTO)
        log.debug("Usuário criando cotação: " + user)
        if(user?.empresa?.endereco){
            cotacao.enderecoEntrega = new Endereco()
            cotacao.enderecoEntrega?.properties = user?.empresa?.endereco?.properties
        }
        return CotacaoAssembler.crieCotacaoDTO(cotacaoService.salve(cotacao))
    }

    def adicioneItemCotacao(List<ItemCotacaoDTO> itensCotacaoNovos) {
        if(itensCotacaoNovos && itensCotacaoNovos.size()>0){
            def itemCotacaoDTO = itensCotacaoNovos?.get(0)
            Cotacao cotacao
            if (itemCotacaoDTO) {
                cotacao = Cotacao.get(itemCotacaoDTO.cotacaoId)
            } else {
                return null
            }
            if (cotacao.itens.size() <= 3000) {
                CotacaoAssembler.crieItensCotacaoDTO(
                        cotacaoService.adicioneItensACotacao(
                                CotacaoAssembler.crieItensCotacao(itensCotacaoNovos), cotacao
                        )
                )
            } else {
                def serverError = new ServerError(id: ServerError.UNSUPORTED_OPERATION_ID,
                        acao: "Adicionar Itens a Cotação",
                        mensagem: "O máximo de itens de uma cotação suportado pelo sistema é de 1500",
                        causa: "Numero máximo de itens da cotação excedido")
                return errorHandler?.pushAndDispatchError(serverError)
            }
        }else{
            def serverError = new ServerError(id: ServerError.UNSUPORTED_OPERATION_ID,
                    acao: "Adicionar Itens a Cotação",
                    mensagem: "Você não selecionou nenhum item para adicionar a cotação",
                    causa: "Nenhum item selecionado")
            return errorHandler?.pushAndDispatchError(serverError)
        }

    }

    Long removaItemCotacao(ItemCotacaoDTO itemCotacaoDTO) {
        def itemCotacaoRemovido = cotacaoService.removaItemDaCotacao(CotacaoAssembler.crieItemCotacao(itemCotacaoDTO))
        if (itemCotacaoRemovido)
            return itemCotacaoDTO.id
        return null
    }

    //TODO: Metodos que deverão ser alterados para contemplar nova arquitetura de análise

    /**
     * Testado - OK
     */
    def envieCotacao(TelaCotacaoDTO novaCotacaoDTO) {
        novaCotacaoDTO = saveCotacao(novaCotacaoDTO)
        ICotacao cotacao = CotacaoAssembler.crieCotacao(novaCotacaoDTO.cotacaoDTO)
        List<Representante> representantesAEnviar = UsuarioAssembler.crieRepresentantesFromIds(novaCotacaoDTO.representantesId)
        if (cotacaoService.envieCotacao(cotacao, representantesAEnviar))
            return true
        return false
    }

    /**
     * Testado - OK
     */
    AnaliseCotacaoDTO getAnaliseCotacao(Long id) {
        StopWatch timer = new Log4JStopWatch()
        def cotacao = Cotacao.get(id)
        cotacao.respostas.each {def resposta ->
            if (!resposta.lida) {
                resposta.lida = true
                resposta.save()
            }
        }
        def analiseDTO = AnaliseAssembler.crieAnaliseCotacaoDTO(compraService.getCompraByCotacaoId(id))
        timer.stop("Obtendo a análise")
        return analiseDTO
    }

    /**
     * testado Ok
     */
    AnaliseCotacaoDTO atualizeRespostas(Long idCotacao, int qtddRespostasConcluidas) {
        if (cotacaoService.possuiNovaResposta(idCotacao, qtddRespostasConcluidas))
            return getAnaliseCotacao(idCotacao)
        else return null
    }

    /**
     *Testado ok
     *
     */
    def saveItensAnaliseCotacao(List<ItemAnaliseCotacaoDTO> itensAnaliseCotacao){
        Boolean ok = true
        try{
            compraService.updateItens(AnaliseAssembler.crieItensAAtualizar(itensAnaliseCotacao))
        }catch (Exception e){
            e.printStackTrace()
            ok = false;
        }
        if(ok)
            return itensAnaliseCotacao.collect {
                it.saved = true
                return it
            }
        return ok;
    }

    /**
     * Testado ok
     */
    AnaliseCotacaoDTO analiseCotacao(Long id) {
        cotacaoService.analisar(id)
        return AnaliseAssembler.crieAnaliseCotacaoDTO(compraService.getCompraByCotacaoId(id))
    }

    /**
     * Implementado e não testado
     *
     */
    Boolean envieCotacaoParaRepresentante(def idCotacao, def idRepresentante) {
        def representante = Representante.get(idRepresentante)
        return cotacaoService.envieCotacao(Cotacao.get(idCotacao), [representante])
    }

    /**
     *
     *Usado em estado em analise
     */
    List gerePedidos(Map mapCotacaoIdEItensAnaliseNaoSalvos){
        def analiseId = mapCotacaoIdEItensAnaliseNaoSalvos.cotacaoId
        def itensAnaliseNaoSalvos = mapCotacaoIdEItensAnaliseNaoSalvos.itensAnalise
        def itensSalvos = saveItensAnaliseCotacao(itensAnaliseNaoSalvos)
        if(itensSalvos instanceof Boolean && !itensSalvos){
            return null
        }
        List<Pedido> pedidos = cotacaoService.gerePedidosPrimeiraOrdem(analiseId)
        return PedidoAssembler.criePedidosDTO(pedidos)
    }

    /**
     * Implementado e não testado
     */
    String downloadPlanilhaAnaliseCotacao(Long cotacaoId) {
        cotacaoService.exporteAnaliseExcel(cotacaoId)
        def compraId = Cotacao.read(cotacaoId).compraId
        //todo:verificar se essa url sempre funcionará independente do context root*/
        /*String serverURL = config.grails.serverURL
        return getServerRootUrl(serverURL) + "/cotacao/downloadExcelAnalise/$compraId"*/
        return "/cotacao/downloadExcelAnalise/$compraId"
    }

    String donwloadPlanilhaCriacaoCotacaoRascunhoExemplo(){
        return "/arquivos/test/planilha_exemplo_criacao_cotacao_rascunho.xls"
    }

}