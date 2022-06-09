package br.com.cotecom.control.controllers
{
import br.com.cotecom.control.commands.fornecedores.CrieAtendimentoCommand;
import br.com.cotecom.control.commands.fornecedores.LoadEmpresaFornecedorCommand;
import br.com.cotecom.control.commands.fornecedores.LoadRepresentanteCommand;
import br.com.cotecom.control.commands.fornecedores.LoadSupervisorCommand;
import br.com.cotecom.control.commands.fornecedores.RemoveAtendimentoCommand;
import br.com.cotecom.control.commands.fornecedores.SaveEmpresaCommand;
import br.com.cotecom.control.commands.fornecedores.SaveRepresentanteCommand;
import br.com.cotecom.control.commands.fornecedores.SaveSupervisorCommand;
import br.com.cotecom.control.commands.fornecedores.SearchRepresentanteCommand;
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.events.RemoveEvent;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.events.SearchEvent;
import br.com.cotecom.control.events.fornecedores.CrieAtendimentoEvent;

import com.adobe.cairngorm.control.FrontController;

public class FornecedoresController extends FrontController
{
    public function FornecedoresController(){
        initialiseCommands();
    }

    private function initialiseCommands() : void{
        addCommand(CrieAtendimentoEvent.EVENT_NAME, CrieAtendimentoCommand);
        addCommand(SaveEvent.REPRESENTANTE, SaveRepresentanteCommand);
        addCommand(LoadEvent.REPRESENTANTES, LoadRepresentanteCommand);
        addCommand(RemoveEvent.ATENDIMENTO, RemoveAtendimentoCommand);
        addCommand(SearchEvent.REPRESENTANTE_SEM_ATENDIMENTO, SearchRepresentanteCommand);
        addCommand(SaveEvent.SUPERVISOR, SaveSupervisorCommand);
        addCommand(LoadEvent.SUPERVISOR, LoadSupervisorCommand);
        addCommand(SaveEvent.EMPRESA_FORNECEDOR, SaveEmpresaCommand);
        addCommand(LoadEvent.EMPRESA_FORNECEDOR, LoadEmpresaFornecedorCommand);
    }
}
}