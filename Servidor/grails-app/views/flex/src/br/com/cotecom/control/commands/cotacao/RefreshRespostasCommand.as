package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

import mx.collections.ArrayCollection;

public class RefreshRespostasCommand implements ICommand {

    public function RefreshRespostasCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var temp:ArrayCollection = GerenciadorCotacao.getInstance().cotacaoSelecionada.analise.itensAnaliseCotacao;
        GerenciadorCotacao.getInstance().cotacaoSelecionada.analise.itensAnaliseCotacao = null;
        GerenciadorCotacao.getInstance().cotacaoSelecionada.analise.itensAnaliseCotacao = temp;
    }
}
}
