package br.com.cotecom.services

import br.com.cotecom.domain.item.ItemResposta
import br.com.cotecom.domain.resposta.IResposta
import br.com.cotecom.domain.resposta.Resposta
import org.apache.log4j.Logger
import org.hsqldb.lib.StopWatch
import org.perf4j.log4j.Log4JStopWatch
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.dto.resposta.RespostaDTO

class RespostaService {

    private static final log = Logger.getLogger(RespostaService.class)

    boolean transactional = true
    def fileService

    //legado, provavelmente não será mais usado
    def getRespostaByDigest(String respostaDigest) {
        if(!respostaDigest)
            return null
        return Resposta.getRespostaForDigest(respostaDigest)
    }

    def getRespostaById(Long id){
        if(!id) return null
        return Resposta.get(id)
    }

    def salve(IResposta resposta) {
        if(resposta.salve())
            return true
        return false
    }

    def aceite(IResposta resposta) {
        resposta.aceite()
        if(resposta.save())
            return true
        return false
    }
    
    def recuse(IResposta resposta) {
        resposta.recuse()
        if(resposta.save())
            return true
        return false
    }

    def envie(IResposta resposta) {
        this.salve(resposta)
        def enviou = resposta.envie()
        if(enviou){
            return true
        }
        return false
    }

    def salveItem(ItemResposta itemResposta){
        if(itemResposta.save())
            return true
        return false
    }

    String criePlanilhaCotacaoParaPreenchimentoOffLine(Resposta resposta) {
        String pathArquivo = resposta.criePlanilhaParaPreenchimentoOffLine()
        if(fileService.convertFile(pathArquivo, "xls"))
            return pathArquivo
        return null
    }

    def importePlanilhaResposta(Resposta resposta, String pathPlanilha, boolean finalizar) {
        def result = resposta.importePlanilhaResposta(resposta, pathPlanilha)
        if(!(result instanceof Resposta))
            return result
        if(finalizar)
            resposta.envie()
        return Resposta.get(resposta.id)
    }

    IResposta ressusciteResposta(Resposta resposta) {
        resposta.ressuscite()
    }

    boolean canceleResposta(Resposta resposta) {
        boolean cancele = resposta.cancele()
        return cancele
    }

    List<Resposta> getRespostas(Representante representante) {
        return Resposta.findAllByRepresentante(representante,[sort:'dataSalva', order: 'desc'])
    }
}
