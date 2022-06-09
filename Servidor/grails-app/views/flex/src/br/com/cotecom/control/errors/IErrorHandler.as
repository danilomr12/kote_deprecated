package br.com.cotecom.control.errors
{
	public interface IErrorHandler
	{
		function showError(objectToError: Object = null) : void;		
	}
}