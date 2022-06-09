package br.com.cotecom.control.message
{
	public class MessageHandlerFactory
	{
		public static const DEFAULT:String = "DefaultMessageHandler";
		
		public static function createMessageHandler(type:String):IMessageHandler{
			
			switch (type){
				case MessageHandlerFactory.DEFAULT:
					return new DefaultMessageHandler();
					break;
				default:
					return createMessageHandler( DEFAULT );
					break;
			}
			trace("ErrorHandler couldn't be created");
		}

	}
}