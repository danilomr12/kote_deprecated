package br.com.cotecom.model.tests {

import br.com.cotecom.model.utils.ArrayCollections;

import flexunit.framework.TestCase;

import mx.collections.ArrayCollection;

public class ArrayCollectionsTest extends TestCase{
		
		public function testArrayCollectionEquals():void {
			
			var ac1:ArrayCollection
			var ac2:ArrayCollection
			
			var i:int = 43
			var a:String = "aaa"
			var b:String = "bbb"
			var obj:Object = {shape: "quadrado", size: 300}
			
			ac1 = new ArrayCollection([i, a, obj])
			ac2 = new ArrayCollection([i, a, obj])
			assertTrue("arrays com os mesmos itens na mesma ordem deveriam ser 'iguais'", ArrayCollections.equals(ac1, ac2))
			
			ac2 = new ArrayCollection([a, i, obj])
			assertTrue("arrays com os mesmos itens fora de ordem deveriam ser 'iguais'", ArrayCollections.equals(ac1, ac2))
			
			ac2 = new ArrayCollection([a, b, obj])
			assertFalse("arrays com dois itens iguais e um diferente não deveriam ser 'iguais'", 
					ArrayCollections.equals(ac1, ac2))
			
			ac2 = new ArrayCollection([a, b, i, obj])
			assertFalse("arrays com os mesmos itens fora exeto um exedente não deveriam ser 'iguais'", 
					ArrayCollections.equals(ac1, ac2))	
		}
		
		public function testGetEqualItem():void{
			var itens:ArrayCollection = new ArrayCollection();
			for (var i:int = 0; i < 10; i++){
				var itemi:MockObject = new MockObject("nome"+i, i, false);
				itens.addItem(itemi);
			}
			
			var itemToSearch:MockObject = new MockObject("nome3", 3, false);
			var itemFound:MockObject = ArrayCollections.getEqualItem(itens, itemToSearch) as MockObject;
			assertTrue("Item ["+itemToSearch+"] deveria ser igual ao item ["+itemFound+"]", 
					itemToSearch.equals(itemFound));
					
			itemToSearch = new MockObject("nome35", 26, false);
			itemFound = ArrayCollections.getEqualItem(itens, itemToSearch) as MockObject;
			assertFalse("Item ["+itemToSearch+"] não deveria ser encontrado no ArrayCollection ["+itens+"]", 
					itemToSearch.equals(itemFound));
		}
		
		public function testGetEqualIdItem():void{
			var itens:ArrayCollection = new ArrayCollection();
			for (var i:int = 0; i < 10; i++){
				var itemi:MockObject = new MockObject("nome"+i, i, false);
				itens.addItem(itemi);
			}
			
			var itemToSearch:MockObject = new MockObject("nome3", 3, false);
			var itemFound:MockObject = ArrayCollections.getEqualIdItem(itens, itemToSearch) as MockObject;
			assertTrue("Item ["+itemToSearch+"] deveria ser igual ao item ["+itemFound+"]", 
					itemToSearch.equals(itemFound));			
			assertTrue("Item ["+itemToSearch+"] deveria ser igual ao item ["+itemFound+"]", 
					itemToSearch.equalsId(itemFound));
					
			itemToSearch = new MockObject("nome4", 3, true);
			itemFound = ArrayCollections.getEqualIdItem(itens, itemToSearch) as MockObject;
			assertTrue("Item ["+itemToSearch+"] deveria ter o id igual ao id do item ["+itemFound+"]", 
					itemToSearch.equalsId(itemFound));
			assertFalse("Item ["+itemToSearch+"] deveria ser diferente ao item ["+itemFound+"]", 
					itemToSearch.equals(itemFound));
					
			itemToSearch = new MockObject("nome35", 26, false);
			itemFound = ArrayCollections.getEqualIdItem(itens, itemToSearch) as MockObject;
			assertFalse("Item ["+itemToSearch+"] não deveria ser encontrado no ArrayCollection ["+itens+"]", 
					itemToSearch.equals(itemFound));
			assertFalse("Item ["+itemToSearch+"] não deveria ser encontrado no ArrayCollection ["+itens+"]", 
					itemToSearch.equalsId(itemFound));
		}
	}
}
	
class MockObject{
	public var string:String;
	public var numero:Number;
	public var boolean:Boolean;
	
	public function MockObject(string:String, numero:Number, boolean:Boolean){
		this.string = string;
		this.numero = numero;
		this.boolean = boolean;
	}
	
	public function equals(outro:MockObject):Boolean{
		if(outro
		&&(this.string == outro.string)
		&&(this.numero == outro.numero)
		&&(this.boolean == outro.boolean)){
			return true;
		}
		return false;
	}
	
	public function equalsId(outro:MockObject):Boolean{
		if(outro
		&&(this.numero == outro.numero)){
			return true;
		}
		return false;
	}
}