package br.com.cotecom.model{
import br.com.cotecom.control.errors.IErrorHandler;
import br.com.cotecom.control.message.IMessageHandler;
import br.com.cotecom.control.services.CaixaDeEntradaService;
import br.com.cotecom.control.services.handlers.ServerCommunicationHandler;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;
import br.com.cotecom.model.services.comprador.EstadoExibicao;
import br.com.cotecom.model.utils.QueryString;

import com.adobe.cairngorm.model.IModelLocator;

import mx.core.Application;

[Bindable]
public class Session implements IModelLocator{

    private var _user:Usuario;

    public var clipBoard:Object;
    public var errorConsole:String;
    public var statusMessage:String;
    public var applicationServerRootUrl:String;
    public var viewState:EstadoExibicao;
    public static const COMPRADOR:int = 0;
    public static const REPRESENTANTE:int = 1;
    public static const ESTOQUISTA:int = 2;

    public var errorHandler:IErrorHandler;
    public var messageHandler:IMessageHandler;
    public var communicationHandler:ServerCommunicationHandler;
    public var caixaDeEntradaService:CaixaDeEntradaService;

    private var _applicationType:int = -1;
    private static var instance:Session;

    public function Session(enforcer:SingletonEnforcer){
        if(!enforcer is SingletonEnforcer)
            throw new Error("use the get instance function");
        this.viewState = new EstadoExibicao();
        this.applicationServerRootUrl = new QueryString().url;
    }

    public function get applicationType():int{
        var application:Object = Application.application;
        if(_applicationType == -1){
            if(application is CompradorMain){
                this._applicationType = COMPRADOR;
            }else if(application is EstoquistaMain){
                this._applicationType = ESTOQUISTA;
            }else{
                this._applicationType = REPRESENTANTE;
            }
        }
        return this._applicationType
    }

    public function set user(usuario:Usuario):void{
        this._user = usuario;
    }

    public function get user():Usuario{
        return this._user;
    }

    public static function getInstance():Session{
        if(instance == null)
            instance = new Session(new SingletonEnforcer());
        return instance;
    }

}
}
class SingletonEnforcer{};
