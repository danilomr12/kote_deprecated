package br.com.cotecom.control.message
{
import com.notifications.Notification;

public class DefaultMessageHandler implements IMessageHandler
	{
		
		public static const DEFAULT_DURATION:int = 5000;
		public static const DEFAULT_POSITION:String = Notification.NOTIFICATION_POSITION_TOP_CENTER;
		
		public function showMessage(message:Object):void{
			
		}
		public function showTextMessage(title:String, text:String, icon:Class = null):void{
			Notification.show(text, title, DEFAULT_DURATION, DEFAULT_POSITION, icon);
		}
	}
}