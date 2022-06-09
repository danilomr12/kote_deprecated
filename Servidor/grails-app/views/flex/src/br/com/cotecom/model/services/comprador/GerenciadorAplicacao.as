package br.com.cotecom.model.services.comprador
{
import br.com.cotecom.model.Session;
import br.com.cotecom.view.screens.BaseCaixaDeEntrada;

import flash.net.URLRequest;
import flash.net.navigateToURL;

import mx.core.Application;

import mx.utils.URLUtil;

public class GerenciadorAplicacao{

    public static const NOVA_COTACAO:int = 0;
    public static const CAIXA_DE_ENTRADA:int = 1;
    public static const COTACAO_RASCUNHO:int = 2;
    public static const COTACAO_AGUARDANDO_RESPOSTAS:int = 3;
    public static const RESPOSTA:int = 4;
    public static const PEDIDO:int = 5;
    public static const CATALOGO:int = 6;
    public static const FORNECEDORES:int = 7;
    private var session:Session = Session.getInstance();

    public function showView(tela_const:int):void{
        switch (tela_const){
            case NOVA_COTACAO:
                session.viewState.applicationViewStack.selectedIndex = 0;
                break;
            case CAIXA_DE_ENTRADA:
                session.viewState.applicationViewStack.selectedIndex = 1;
                var baseCaixaEtd:BaseCaixaDeEntrada = session.viewState.applicationViewStack.selectedChild as BaseCaixaDeEntrada;
                baseCaixaEtd.selectedIndex = 0;
                session.caixaDeEntradaService.getAllItens();
                break;
            case CATALOGO:
                session.viewState.applicationViewStack.selectedIndex = 2;
                break;
            case FORNECEDORES:
                session.viewState.applicationViewStack.selectedIndex = 3;
                break;
            case COTACAO_RASCUNHO:
                session.viewState.applicationViewStack.selectedIndex = 1;
                var baseCxEntrada:BaseCaixaDeEntrada = session.viewState.applicationViewStack.selectedChild as BaseCaixaDeEntrada;
                baseCxEntrada.selectedIndex = 1;
                break;
            case COTACAO_AGUARDANDO_RESPOSTAS:
                session.viewState.applicationViewStack.selectedIndex = 1;
                var baseCxEntrada2:BaseCaixaDeEntrada = session.viewState.applicationViewStack.selectedChild as BaseCaixaDeEntrada;
                baseCxEntrada2.selectedIndex = 2;
                break;
            case RESPOSTA:
                session.viewState.applicationViewStack.selectedIndex = 1;
                var baseCxEtda:BaseCaixaDeEntrada = session.viewState.applicationViewStack.selectedChild as BaseCaixaDeEntrada;
                baseCxEtda.selectedIndex = 3;
                break;
            case PEDIDO:
                session.viewState.applicationViewStack.selectedIndex = 1;
                var bCxEntrada:BaseCaixaDeEntrada = session.viewState.applicationViewStack.selectedChild as BaseCaixaDeEntrada;
                bCxEntrada.selectedIndex = 4;
                break;
            default:
                session.viewState.applicationViewStack.selectedIndex = 1;
                break;
        }
    }

    private static var instance:GerenciadorAplicacao;

    public function GerenciadorAplicacao(enforcer:SingletonEnforcer){
        if(!enforcer is SingletonEnforcer)
            throw new Error("use the get instance function");
    }

    public static function getInstance():GerenciadorAplicacao{
        if(instance == null)
            instance = new GerenciadorAplicacao(new SingletonEnforcer());
        return instance;
    }

    public function logout():void {
        var fullUrl:String = "http://"+URLUtil.getServerNameWithPort(Application.application.loaderInfo.url);
        navigateToURL(new URLRequest(fullUrl + "/logout"), "_self")
    }

    public function switchUserBack():void {
        var fullUrl:String = "http://"+URLUtil.getServerNameWithPort(Application.application.loaderInfo.url);
        navigateToURL(new URLRequest(fullUrl + "/j_spring_security_exit_user"), "_self")
    }

    private function getRootAppUrl():String {
        var string:String = session.applicationServerRootUrl;
        var root:String = "";
        var barrasCount:int = 0;
        for (var a:int = 0; a < string.length; a++) {
            if (string.charAt(a) == "/")
                barrasCount++;
            if (barrasCount < 3)
                root += string.charAt(a);
        }
        return root;
    }

    public function mudeSenha():void {
        var fullUrl:String = "http://"+URLUtil.getServerNameWithPort(Application.application.loaderInfo.url);
        navigateToURL(new URLRequest(fullUrl + "/login/changePass"), "_self")
    }
}
}
class SingletonEnforcer{};