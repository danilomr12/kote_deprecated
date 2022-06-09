package br.com.cotecom.control.delegates {
import br.com.cotecom.control.responders.usuario.SaveCompradorResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.usuarios.Empresa;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;

import mx.rpc.AsyncToken;
import mx.rpc.IResponder;
import mx.rpc.remoting.RemoteObject;

public class UsuarioDelegate {

    public const SERVICE_FACADE_NAME:String = "remoteUsuarioService";

    private var remoteObject:RemoteObject;

    public var model:Session = Session.getInstance();

    public function UsuarioDelegate() {
        this.remoteObject = new RemoteObject(SERVICE_FACADE_NAME);
    }

    public function loadUsuarioLogado(responder:IResponder):void {
        setAsyncToken(this.remoteObject.getUsuarioLogado(), responder);
    }

    public function loadRepresentantes(responder:IResponder):void {
        setAsyncToken(this.remoteObject.getRepresentantesByComprador(), responder);
    }

    public function loadEmpresasFornecedores(responder:IResponder):void {
        setAsyncToken(this.remoteObject.getFornecedores(), responder);
    }

    public function loadSupervisores(responder:IResponder):void {
        setAsyncToken(this.remoteObject.getSupervisores(), responder);
    }

    public function searchRepresentantesSemAtendimento(responder:IResponder, query:String):void {
        setAsyncToken(this.remoteObject.busqueNovosRepresentantes(query), responder);
    }

    public function crieAtedimento(responder:IResponder, representante:Usuario):void {
        setAsyncToken(AsyncToken(this.remoteObject.crieAtendimento(representante)), responder);
    }

    public function saveRepresentante(responder:IResponder, representante:Usuario):void {
        setAsyncToken(this.remoteObject.saveRepresentante(representante), responder);
    }

    public function saveFornecedor(responder:IResponder, empresa:Empresa):void {
        setAsyncToken(this.remoteObject.saveEmpresa(empresa), responder);
    }

    public function saveCliente(responder:IResponder, empresa:Empresa):void {
        setAsyncToken(this.remoteObject.saveEmpresa(empresa), responder);
    }

    public function saveSupervisor(responder:IResponder, supervisor:Usuario):void {
        setAsyncToken(AsyncToken(this.remoteObject.saveSupervisor(supervisor)), responder);
    }

    public function updateCadastroComprador(responder:SaveCompradorResponder, comprador:Usuario):void {
        setAsyncToken(this.remoteObject.saveComprador(comprador), responder);
    }

    public function removeAtedimento(responder:IResponder, representanteToRemove:Usuario):void {
        setAsyncToken(this.remoteObject.removeAtendimento(representanteToRemove), responder)
    }

    private function setAsyncToken(object:AsyncToken, responder:IResponder):void {
        object.addResponder(responder);
    }
}
}