package br.com.cotecom.control.message
{
	public interface IMessageHandler
	{
		function showMessage(message:Object) : void;
		function showTextMessage(title:String, text:String, icon:Class = null) : void;
	}
}