package br.com.cotecom.model.services.comprador{

import br.com.cotecom.model.domain.dtos.usuarios.Empresa;
import br.com.cotecom.model.domain.dtos.usuarios.TipoUsuario;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;
import br.com.cotecom.model.utils.ArrayCollections;

import mx.collections.ArrayCollection;

[Bindable]
public class GerenciadorRepresentantes{
    public var representantesComAtendimento:ArrayCollection;

    private static var instance:GerenciadorRepresentantes;

    public function GerenciadorRepresentantes(enforcer:SingletonEnforcer){
        if(!enforcer is SingletonEnforcer)
            throw new Error("use the get instance function");

        this.representantesComAtendimento = new ArrayCollection();
    }

    public function adicioneOuSubstituaRepresentante(representante:Usuario):void{
        adicioneOuSubstituaObjeto(representante, this.representantesComAtendimento);
        representante.supervisor = representante.supervisor;// forcada para dar refresh
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
            alvo.addItemAt(objeto, indexASubstituir);
        }else{
            alvo.addItem(objeto);
        }
    }

    public function getRepresentanteByEmpresa(empresa:Empresa):Usuario{
        for each(var rep:Usuario in this.representantesComAtendimento){
            if(rep.empresa.id == empresa.id)
                return rep
        }
        return null;
    }

    public function getRepresentanteBySupervisor(supervisor:Usuario):Usuario{
        for each(var rep:Usuario in this.representantesComAtendimento){
            if(rep.supervisor && rep.supervisor.id == supervisor.id)
                return rep
        }
        return null;
    }

    public static function getInstance():GerenciadorRepresentantes{
        if(instance == null)
            instance = new GerenciadorRepresentantes((new SingletonEnforcer()));
        return instance;
    }

    public function criarRepresentante():void{
        var novoRepresentante:Usuario = new Usuario(TipoUsuario.REPRESENTANTE);
        this.representantesComAtendimento.addItemAt(novoRepresentante,0);
    }

    public function removaRepresentante(representante:Usuario):void{
        this.representantesComAtendimento.removeItemAt(this.representantesComAtendimento.getItemIndex(
                ArrayCollections.getEqualIdItem(this.representantesComAtendimento, representante)));
    }

    public function getRepresentanteById(representanteId:*):Usuario{
        if(representanteId && representantesComAtendimento){
            for each(var representante:Usuario in representantesComAtendimento){
                if(representanteId == representante.id){
                    return representante;
                }
            }
        }
        return null;
    }

}
}
class SingletonEnforcer{};