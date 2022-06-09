package br.com.cotecom.control.services.handlers
{
import br.com.cotecom.view.components.login.LoginWindow;

import flash.display.DisplayObject;
import flash.events.TimerEvent;
import flash.utils.Timer;

import mx.core.Application;
import mx.managers.PopUpManager;

public class ServerCommunicationHandler
{
    public var timer:Timer;

    //Tempo de delay igual a 30 minutos de inatividade
    public static const DELAY:Number = 1800000;

    public function ServerCommunicationHandler() {
        timer = new Timer(DELAY, 1);
        timer.addEventListener(TimerEvent.TIMER, timerDispatch);
        timer.start();
    }

    public function receiveCommunication():void {
        resetCommunication();
    }

    public function resetCommunication():void{
        timer.reset();
        timer.start();
    }

    public function timerDispatch(event:Object):void{
        if(timer.running){
            var login:LoginWindow = new LoginWindow();
            PopUpManager.addPopUp(login, Application.application as DisplayObject, true);
            PopUpManager.centerPopUp(login);
            timer.stop();
        }
    }
}
}