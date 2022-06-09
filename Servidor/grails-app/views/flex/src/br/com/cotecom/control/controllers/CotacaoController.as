package br.com.cotecom.control.controllers {
import br.com.cotecom.control.commands.cotacao.SaveItemCotacaoCommand;
import br.com.cotecom.control.commands.resposta.AtualizeRespostasCommand;
import br.com.cotecom.control.commands.cotacao.SalveAnaliseCotacaoCommand;
import br.com.cotecom.control.commands.cotacao.AdicioneItemCotacaoCommand;
import br.com.cotecom.control.commands.cotacao.AdicioneProdutoACotacaoCommand;
import br.com.cotecom.control.commands.cotacao.AnaliseCotacaoCommand;
import br.com.cotecom.control.commands.cotacao.CrieNovaCotacaoRascunhoConmmand;
import br.com.cotecom.control.commands.cotacao.EnviaCotacaoParaFornecedorCommand;
import br.com.cotecom.control.commands.cotacao.FinalizeCotacaoCommand;
import br.com.cotecom.control.commands.cotacao.GeraEEnviaPedidosCommand;
import br.com.cotecom.control.commands.cotacao.LoadAnaliseCotacaoCommand;
import br.com.cotecom.control.commands.cotacao.LoadCotacaoCommand;
import br.com.cotecom.control.commands.cotacao.ReenvieEmailCotacacaoCommand;
import br.com.cotecom.control.commands.cotacao.RemovaItemCotacaoCommand;
import br.com.cotecom.control.commands.cotacao.RemoveCotacaoCommand;
import br.com.cotecom.control.commands.cotacao.SaveCotacaoCommand;
import br.com.cotecom.control.commands.cotacao.SendCotacaoCommand;
import br.com.cotecom.control.commands.cotacao.VerificaNovasRespostasCommand;
import br.com.cotecom.control.commands.resposta.CanceleRespostaCommand;
import br.com.cotecom.control.commands.resposta.LoadRespostaCommand;
import br.com.cotecom.control.commands.resposta.RessusciteRespostaCommand;
import br.com.cotecom.control.commands.resposta.SalveItensRespostaCommand;
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.events.RemoveEvent;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.events.aplicacao.ReenvieEmailCotacacaoEvent;
import br.com.cotecom.control.events.cotacao.AdicioneItemCotacaoEvent;
import br.com.cotecom.control.events.cotacao.AdicioneProdutoACotacaoEvent;
import br.com.cotecom.control.events.cotacao.AtualizeRespostasEvent;
import br.com.cotecom.control.events.cotacao.CotacaoEvent;
import br.com.cotecom.control.events.cotacao.CrieNovaCotacaoRascunhoEvent;
import br.com.cotecom.control.events.cotacao.EnviaCotacaoParaFornecedorEvent;
import br.com.cotecom.control.events.cotacao.GeraPedidosEvent;
import br.com.cotecom.control.events.cotacao.RemovaItemCotacaoEvent;
import br.com.cotecom.control.events.cotacao.SendCotacaoEvent;
import br.com.cotecom.control.events.cotacao.VerificaNovasRespostasEvent;
import br.com.cotecom.control.events.resposta.RespostaEvent;

import com.adobe.cairngorm.control.FrontController;

public class CotacaoController extends FrontController {

    public function CotacaoController() {
        initialiseCommands();
    }

    private function initialiseCommands():void {
        addCommand(SaveEvent.COTACAO, SaveCotacaoCommand);
        addCommand(LoadEvent.COTACAO, LoadCotacaoCommand);
        addCommand(SendCotacaoEvent.NAME, SendCotacaoCommand);
        addCommand(RemoveEvent.COTACAO, RemoveCotacaoCommand);
        addCommand(LoadEvent.ANALISE_COTACAO, LoadAnaliseCotacaoCommand);
        addCommand(CotacaoEvent.ANALISE_COTACAO, AnaliseCotacaoCommand);
        addCommand(SaveEvent.ANALISE_COTACAO, SalveAnaliseCotacaoCommand);
        addCommand(AdicioneProdutoACotacaoEvent.EVENT_NAME, AdicioneProdutoACotacaoCommand);
        addCommand(EnviaCotacaoParaFornecedorEvent.EVENT_NAME, EnviaCotacaoParaFornecedorCommand);
        addCommand(AtualizeRespostasEvent.EVENT_NAME, AtualizeRespostasCommand);
        addCommand(GeraPedidosEvent.GERAR_PEDIDOS, GeraEEnviaPedidosCommand);
        addCommand(LoadEvent.RESPOSTA, LoadRespostaCommand);
        addCommand(SaveEvent.RESPOSTA, SalveItensRespostaCommand);
        addCommand(ReenvieEmailCotacacaoEvent.EVENT_NAME, ReenvieEmailCotacacaoCommand);
        addCommand(VerificaNovasRespostasEvent.EVENT_NAME, VerificaNovasRespostasCommand);
        addCommand(CrieNovaCotacaoRascunhoEvent.EVENT_NAME, CrieNovaCotacaoRascunhoConmmand);
        addCommand(AdicioneItemCotacaoEvent.EVENT_NAME, AdicioneItemCotacaoCommand);
        addCommand(RemovaItemCotacaoEvent.EVENT_NAME, RemovaItemCotacaoCommand);
        addCommand(SaveEvent.ITEM_COTACAO, SaveItemCotacaoCommand);
        addCommand(CotacaoEvent.FINALIZE_COTACAO, FinalizeCotacaoCommand);
        addCommand(RespostaEvent.CANCELAR, CanceleRespostaCommand);
        addCommand(RespostaEvent.RESSUSCITAR, RessusciteRespostaCommand);
    }
}
}
