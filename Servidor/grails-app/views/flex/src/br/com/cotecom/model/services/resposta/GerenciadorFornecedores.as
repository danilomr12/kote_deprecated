package br.com.cotecom.model.services.resposta{

import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.model.domain.dtos.usuarios.Empresa;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;

import mx.collections.ArrayCollection;

[Bindable]
public class GerenciadorFornecedores{

    public var representante:Usuario;
    public var empresas:ArrayCollection;
    public var supervisores:ArrayCollection;

    private static var instance:GerenciadorFornecedores;

    public function GerenciadorFornecedores(enforcer:SingletonEnforcer){
        if(!enforcer is SingletonEnforcer)
            throw new Error("use the get instance function");
        this.empresas = new ArrayCollection();
        this.supervisores = new ArrayCollection();
    }

    public function loadInformacoes():void{
        new LoadEvent(LoadEvent.EMPRESA_FORNECEDOR).dispatch();
        new LoadEvent(LoadEvent.SUPERVISOR).dispatch();
    }

    public function adicioneOuSubstituaFornecedor(fornecedor:Empresa):void{
        if(fornecedor)
            adicioneOuSubstituaObjeto(fornecedor, this.empresas);
    }

    public function adicioneOuSubstituaSupervisor(supervisor:Usuario):void{
        if(supervisor)
            adicioneOuSubstituaObjeto(supervisor, this.supervisores);
    }

    private function adicioneOuSubstituaObjeto(objeto:*, alvo:ArrayCollection):void{
        var indexASubstituir:int = -1;
        for(var i:int = 0; i < alvo.length; i++){
            if(alvo.getItemAt(i) && alvo.getItemAt(i).id == objeto.id){
                indexASubstituir = i;
            }
        }
        if(indexASubstituir >= 0){
            alvo.removeItemAt(indexASubstituir);
            alvo.addItemAt(objeto ,indexASubstituir);
        }else{
            alvo.addItem(objeto);
        }
    }

    public static function getInstance():GerenciadorFornecedores{
        if(instance == null)
            instance = new GerenciadorFornecedores(new SingletonEnforcer());
        return instance;
    }

    public function getEmpresaById(empresaId:*):Empresa{
        if(empresaId && empresas){
            for each(var empresa:Empresa in empresas){
                if(empresaId == empresa.id){
                    return empresa;
                }
            }
        }
        return null;
    }

    public function getSupervisorById(supervisorId:*):Usuario{
        if(supervisorId && supervisores){
            for each(var supervisor:Usuario in supervisores){
                if(supervisorId == supervisor.id){
                    return supervisor;
                }
            }
        }
        return null;
    }
}
}

class SingletonEnforcer{}