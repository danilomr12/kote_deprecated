package br.com.cotecom.control.services.handlers
{
import flash.events.Event;

public interface AsyncMessageHandler
	{
		function messageHandler(evt:Event):void;
		function faultHandler(evt:Event):void;
		function channelDisconnectedHandler(evt:Event):void;
		function channelConnectedHandler(evt:Event):void;
		
	}
}