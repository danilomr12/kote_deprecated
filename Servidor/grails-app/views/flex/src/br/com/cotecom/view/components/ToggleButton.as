package br.com.cotecom.view.components
{
import flash.events.Event;

import mx.controls.Button;

[Style(name="toggledColor", type="uint", format="Color", inherit="no")]
	[Style(name="toggledTextRollOverColor", type="uint", format="Color", inherit="no")]
	[Style(name="toggledTextSelectedColor", type="uint", format="Color", inherit="no")]
	
	public class ToggleButton extends Button{
		private var defaultColor:uint;
		private var defaultRoll:uint;
		private var defaultSelected:uint;
		private var toggledColor:uint;
		private var toggledRoll:uint;
		private var toggledSelected:uint;
		
		public function ToggleButton(){
			super();
			addEventListener('initialize',init);
			this.buttonMode = true;
		}
		
		private function init(e:Event):void{
			defaultColor=getStyle('color');
			defaultRoll=getStyle('textRollOverColor');
			defaultSelected=getStyle('textSelectedColor');
			toggledColor=getStyle('toggledColor');
			toggledRoll=getStyle('toggledTextRollOverColor');
			toggledSelected=getStyle('toggledTextSelectedColor');
					
			setStyle('textAlign',"left");
			setStyle('color',"#EAEAEA");
			setStyle('highlightAlphas', [0.0, 0.0]);
			setStyle('borderStyle','none');
			setStyle('fillAlphas', [0,0]);
			setStyle('textRollOverColor',defaultRoll);
			setStyle('textSelectedColor',defaultSelected);
		}
		
		override public function set selected(value:Boolean):void{
			if(value){
				setStyle('color',toggledColor);
				setStyle('textRollOverColor',toggledRoll);
				setStyle('textSelectedColor',toggledSelected);
				setStyle('fillAlphas',[0.6, 0.4]);
				super.selected = true;
			}  
			else if(super.selected&&!value){  
//				setStyle('color',defaultColor);
				setStyle('textRollOverColor','#FFF566');
				setStyle('textSelectedColor',defaultSelected);
				setStyle('textAlign',"left");
				setStyle('textColor',"#FFF566");
				setStyle('highlightAlphas', [0.0, 0.0]);
				setStyle('fillAlphas', [0,0]);
				
				super.selected = false;
			}
		}
	}
}// ActionScript file