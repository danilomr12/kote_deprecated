package com.flexstore.model.utils
{
import flash.events.IEventDispatcher;

import mx.events.StyleEvent;
import mx.styles.StyleManager;
import mx.utils.StringUtil;

public class ThemeLoader
	{
		static  public const   THEME_BLUE          : String            = 'blue';
		static  public const   THEME_BIEGE         : String            = 'beige';

		static  private const  	PATH_THEMES         : String			= "/assets/css/{0}.swf";
		
		private var handler:Function = null;
		private var currentTheme:String = "";
		
		public function ThemeLoader(caller:Function) {
			this.handler = caller;		
		}

		public function toggleTheme():void {
			loadTheme( this.currentTheme == THEME_BIEGE ? THEME_BLUE : THEME_BIEGE );
		}
        public function loadTheme(name:String = ''):void {        	
        	if ( this.handler != null) {
	        	this.currentTheme = (name=="") ? THEME_BIEGE : name;  

	        	var pathToStyles   : String           = StringUtil.substitute(PATH_THEMES,[this.currentTheme]);
	            var dispatcher     : IEventDispatcher = StyleManager.loadStyleDeclarations(pathToStyles);
	            
	            dispatcher.addEventListener(StyleEvent.COMPLETE, this.handler);
        	}
        }
	}
}