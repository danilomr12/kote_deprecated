package br.com.cotecom.model.utils{

import mx.collections.ArrayCollection;

public class ArrayCollections {
		
		public static function equals( ar1:ArrayCollection, ar2:ArrayCollection ):Boolean{
			if(ar1.length == ar2.length){
				for each( var item:Object in ar1 ){
					if( !ar2.contains( item ) ) return false;
				}
				return true;
			}
			return false;
		}
		public static function equalsCustom( ar1:ArrayCollection, ar2:ArrayCollection ):Boolean{
			if(ar1.length == ar2.length){
				for each( var item:Object in ar1 ){
					if( !contains(ar2, item) ) return false;
				}
				return true;
			}
			return false;
		}
		
		public static function equalsId( ar1:ArrayCollection, ar2:ArrayCollection ):Boolean{
			if(ar1.length == ar2.length){
				for each( var item:Object in ar1 ){
					if( !containsId(ar2, item) ) return false;
				}
				return true;
			}
			return false;
		}
		
		public static function contains(array:ArrayCollection, itemToSearch:Object):Boolean{
			return getEqualItem(array, itemToSearch);
		}
		
		public static function containsId(array:ArrayCollection, itemToSearch:Object):Boolean{
			return getEqualIdItem(array, itemToSearch);
		}
		
		public static function getEqualItem(array:ArrayCollection, itemToSearch:Object):Object{
			for each( var item:Object in array ){
				if( item.equals(itemToSearch) ){
					return item;
				}
			}
			return null;
		}
		
		public static function getEqualIdItem(array:ArrayCollection, itemToSearch:Object):Object{
			for each( var item:Object in array ){
				if( item.equalsId(itemToSearch) ){
					return item;
				}
			}
			return null;
		}
		
		public static function toXml(array:ArrayCollection, rootName:String = "childrem"):XML{
			var result:XML = new XML( "<"+rootName+"></"+rootName+">" );
			
			for each( var item:Object in array ){
				result[rootName] += item.toXml();
			}
			
			return result;
		}
		
		public static function cloneAll(array:ArrayCollection):ArrayCollection{
			var clone:ArrayCollection = new ArrayCollection();
			for each(var item:* in array){
				clone.addItem(item.clone())
			}
			return clone;
		}
	}
}