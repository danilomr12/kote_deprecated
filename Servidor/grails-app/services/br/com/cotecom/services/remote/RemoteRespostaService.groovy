package br.com.cotecom.services.remote

import br.com.cotecom.domain.dto.resposta.ItemRespostaDTO
import br.com.cotecom.domain.dto.tela.TelaRespostaRepresentanteDTO
import br.com.cotecom.domain.error.ErrorHandler
import br.com.cotecom.domain.error.ServerError
import br.com.cotecom.domain.item.ItemResposta
import br.com.cotecom.domain.resposta.IResposta
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.domain.resposta.RespostaRespondendo
import br.com.cotecom.dtos.assembler.RespostaAssembler
import br.com.cotecom.util.Path.Path
import org.apache.log4j.Logger
import org.perf4j.log4j.Log4JStopWatch
import org.perf4j.StopWatch
import br.com.cotecom.domain.resposta.EstadoResposta
import br.com.cotecom.domain.dto.resposta.RespostaDTO
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.dto.tela.TelaPedidoDTO
import br.com.cotecom.domain.pedido.Pedido
import br.com.cotecom.dtos.assembler.PedidoAssembler
import br.com.analise.domain.Compra
import br.com.cotecom.domain.resposta.RespostaAguardandoOutrasRespostas
import br.com.cotecom.domain.resposta.RespostaProntaParaAnalise

class RemoteRespostaService {

    // Todo Salvar itens de resposta aos poucos

    private static final log = Logger.getLogger(RemoteRespostaService.class)

    boolean transactional = true

    static expose = ["flex-remoting"]

    def compraService
    def grailsApplication
    def fileService
    def respostaService
    def usuarioService
    ErrorHandler errorHandler = ErrorHandler.getInstance()

    def getResposta(String respostaDigest) {
        StopWatch timer = new Log4JStopWatch();
        IResposta resposta = respostaService.getRespostaByDigest(respostaDigest)
        if (resposta){
            def respostaDTO = RespostaAssembler.crieTelaRespostaDTO(resposta)
            timer.stop("Carregando resposta")
            return respostaDTO
        }
        timer.stop("Carregando resposta")
        return errorHandler?.pushAndDispatchError(
                new ServerError(id: 0,
                        acao: "Carregar Cotação",
                        mensagem: "Não foi possível carregar sua cotação.",
                        causa: "Cotação inexistente."))
    }
    
    def getRespostas(){
        def user = usuarioService.getSessionUser()
        if(!(user instanceof Representante))
            return false
        Representante representante = user as Representante
        List<Resposta> respostas = respostaService.getRespostas(representante)
        def collect = respostas.collect {RespostaAssembler.crieRespostaDTO(it)}

        return collect
    }

    def getRespostaById(Long id) {
        IResposta resposta = respostaService.getRespostaById(id)
        if (resposta)
            return RespostaAssembler.crieTelaRespostaDTO(resposta)

        return errorHandler?.pushAndDispatchError(
                new ServerError(id: 0,
                        acao: "Carregar Cotação",
                        mensagem: "Não foi possível carregar sua cotação.",
                        causa: "Cotação inexistente."))
    }

    def aceiteResposta(Long id) {
        StopWatch timer = new Log4JStopWatch();
        IResposta resposta = Resposta.get(id)
        if (resposta) {
            try {
                def result = respostaService.aceite(resposta)
                timer.stop("Tempo para aceitar resposta")
                return result
            } catch (UnsupportedOperationException e) {
                def serverError = new ServerError(id: 0,
                        acao: "Aceitar Cotação",
                        mensagem: "Não foi possível aceitar a cotação.",
                        causa: "Cotação Já Aceita ou Recusada.")
                return errorHandler?.pushAndDispatchError(serverError)
            }
        }

        def serverError = new ServerError(id: ServerError.UNSUPORTED_OPERATION_ID,
                acao: "Aceitar Cotação",
                mensagem: "Não foi possível aceitar a cotação.",
                causa: "Cotação Inexistente")
        return errorHandler?.pushAndDispatchError(serverError)
    }

    def recuseResposta(Long id) {
        IResposta resposta = Resposta.get(id)
        boolean recusou = false
        if (resposta) {
            try {
                recusou = respostaService.recuse(resposta)
            } catch (UnsupportedOperationException e) {
                def serverError = new ServerError(id:  ServerError.UNSUPORTED_OPERATION_ID,
                        acao: "Recusar Cotação",
                        mensagem: "Não foi possível recusar a cotação.",
                        causa: "Cotação Já Aceita ou Recusada.")
                return errorHandler?.pushAndDispatchError(serverError)
            }
        }

        if (recusou)
            return RespostaAssembler.crieRespostaDTO(Resposta.get(id))

        return errorHandler?.pushAndDispatchError(
                new ServerError(id: 0,
                        acao: "Recusar Cotação",
                        mensagem: "Não foi possível recusar sua cotação.",
                        causa: "Cotação Inexistente"))
    }

    def envieResposta(TelaRespostaRepresentanteDTO respostaDTO) {
        StopWatch timer = new Log4JStopWatch();
        Exception exception
        IResposta resposta = Resposta.get(respostaDTO?.resposta?.id)
        if (resposta) {
            try {
                if(resposta.estado instanceof RespostaRespondendo) this.salveItemResposta(respostaDTO.itensResposta)
                respostaService.envie(resposta)
            } catch (Exception e) {
                exception = e
                log.debug e
            }finally {
                def read = Resposta.read(resposta.id)
                if(!exception && (read.estado instanceof RespostaAguardandoOutrasRespostas ||
                        read.estado instanceof RespostaProntaParaAnalise )){
                    def respostaRetornoDTO = RespostaAssembler.crieRespostaDTO(Resposta.get(resposta.id))
                    timer.stop("Enviando resposta")
                    return respostaRetornoDTO
                }else{
                    Resposta.get(resposta.id).mudeEstadoPara(EstadoResposta.RESPONDENDO)
                    compraService.updateRespostaCompra(resposta.id, EstadoResposta.RESPONDENDO)
                    if (exception != null && exception instanceof UnsupportedOperationException){
                        return errorHandler?.pushAndDispatchError(
                                new ServerError(id: ServerError.UNSUPORTED_OPERATION_ID,
                                        acao: "Enviar resposta",
                                        mensagem: "Não foi possível enviar sua resposta. Salve sua resposta e atualize a página e verifique se ela já foi finalizada.",
                                        causa: "Cotação Já Respondida ou Recusada."))
                    }else{
                        return errorHandler?.pushAndDispatchError(
                                new ServerError(id: ServerError.UNSUPORTED_OPERATION_ID,
                                        acao: "Enviar resposta",
                                        mensagem: "Erro ao enviar sua resposta. Tente novamente, se o problema persistir contate o administrador",
                                        causa: ""))
                    }

                }
            }
        }else{
            return errorHandler?.pushAndDispatchError(
                    new ServerError(id: ServerError.UNSUPORTED_OPERATION_ID,
                            acao: "Enviar resposta",
                            mensagem: "Erro ao enviar sua resposta.",
                            causa: "Resposta não encontrada."))
        }
    }

    def salveItemResposta(List<ItemRespostaDTO> itensRespostaDTO) {
        boolean  todosSalvos = true
        for(int respIndex=0; respIndex < itensRespostaDTO.size(); respIndex++){
            ItemRespostaDTO itemRespostaDTO = itensRespostaDTO.get(respIndex)
            ItemResposta itemResposta = RespostaAssembler.crieItemResposta(itemRespostaDTO)
            boolean itemSalvo = respostaService.salveItem(itemResposta)
            if (todosSalvos) todosSalvos = itemSalvo
        }
        if(todosSalvos)
            return new Date()
        return todosSalvos
    }

    String downloadFormularioCotacaoParaRespostaOffLine(Long respostaId) {
        respostaService.criePlanilhaCotacaoParaPreenchimentoOffLine(Resposta.get(respostaId))
        //todo:verificar se essa url sempre funcionará independente do context root*/
        /*String serverURL = grailsApplication.config.grails.serverURL
        return getServerRootUrl(serverURL) + "/resposta/downloadExcelCotacao/$respostaId"*/
        return "/resposta/downloadExcelCotacao/$respostaId"
    }

    private String getServerRootUrl(String serverURL) {
        String root = ""
        int barrasCout = 0
        serverURL.each {
            if(it=="/")
                barrasCout ++
            if(barrasCout<3)
                root += it
        }
        return root
    }

    def importePlanilhaResposta(byte[] bytes, Long respostaId, boolean finalizar) {
        StopWatch timer = new Log4JStopWatch();
        File arquivo = fileService.graveArquivo(bytes, new Path().getPathArquivosExcelRespostas() + File.separator +
                "planilha_resposta_${respostaId}.xls");
        def resposta = respostaService.importePlanilhaResposta(Resposta.get(respostaId), arquivo.path, finalizar)
        arquivo.delete()
        if (resposta instanceof Resposta){
            def dtoTelaResposta = RespostaAssembler.crieTelaRespostaDTO(resposta)
            timer.stop("Importação Planilha Com Respostas")
            return dtoTelaResposta
        }else if(resposta instanceof List && resposta.get(0) instanceof ServerError)
            return  resposta
        timer.stop("Importação Planilha Com Respostas")
        return false
    }

    RespostaDTO ressusciteResposta(long respostaId){
        RespostaAssembler.crieRespostaDTO(
                respostaService.ressusciteResposta(Resposta.get(respostaId))
        )
    }

    boolean canceleResposta(long respostaId){
        return respostaService.canceleResposta(Resposta.get(respostaId))
    }

    TelaPedidoDTO getPedidoByid(Long pedidoId){
        def get = Pedido.get(pedidoId)
        Compra compra = compraService.getCompraByCotacaoId(get.resposta.cotacao.id)
        PedidoAssembler.crieTelaPedidoDTO(get, compra)
    }

    def faturePedido(Long pedidoId){
        Pedido pedido = Pedido.get(pedidoId)
        pedido.faturado = true
        pedido.save()
        pedido.resposta.faturePedido()
    }

    String downloadPedido(def idPedido){
        def pedido = Pedido.read(idPedido)
        if(pedido) {
            def string = "/pedido/exportePedidoExcel?digest=${pedido.pedidoUrlDigest.encodeAsURL()}"
            return string
        }else{
            return null
        }
    }
}