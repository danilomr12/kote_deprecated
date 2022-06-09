package br.com.cotecom.control.events.usuario
{
import com.adobe.cairngorm.control.CairngormEvent;

public class LoginEvent extends CairngormEvent
{
    public static const EVENT_NAME:String = "login";

    public var user:String;
    public var pass:String;
    public var popup:Object;

    public function LoginEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
    {
        super(type, bubbles, cancelable);
    }
}
}