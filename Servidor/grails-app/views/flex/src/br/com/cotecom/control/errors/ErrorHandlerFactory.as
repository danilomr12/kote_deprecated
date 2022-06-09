package br.com.cotecom.control.errors
{
public class ErrorHandlerFactory
	{
		
		public static const DEFAULT:String = "DefaultErrorHandler";
		
		public static function createErrorHandler(type:String):IErrorHandler{
			
			switch (type){
				case ErrorHandlerFactory.DEFAULT:
					return new DefaultErrorHandler();
					break;
				default:
					return createErrorHandler( DEFAULT );
					break;
			}
			trace("ErrorHandler couldn't be created");
		}

	}
}