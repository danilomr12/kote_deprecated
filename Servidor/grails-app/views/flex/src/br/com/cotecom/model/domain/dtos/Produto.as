package br.com.cotecom.model.domain.dtos{

	[Bindable]
	[RemoteClass(alias="br.com.cotecom.domain.dto.produto.ProdutoDTO")]
	public class Produto{

		public var descricao:String;
		public var categoria:String;
		public var barCode:String;
		public var fabricante:String;
		public var embalagem:String;
        public var qtdMaster:int;
		public var marca:String;
    	public var isDeletado:Boolean;
    	public var peso:Number;
		public var id:*;
        public var version:*;
        public var empresaId:String;
	
		public function Produto(descricao:String = "", categoria:String = "", codigoDeBarras:String = "",
				fabricante:String = "", embalagem:String = "", marca:String = "", peso:Number = 0, id:* = null){
					this.descricao = descricao;
					this.categoria = categoria;
					this.barCode = codigoDeBarras;
					this.fabricante = fabricante;
					this.embalagem = embalagem;
					this.marca = marca;
					this.peso = peso;
					this.id = id;
		}
		
		public function validate():String{
			if( 
					(descricao == "") ||
					(categoria == "") ||
					(barCode == "") ||
					(fabricante == "") ||
					(embalagem == "") ||
					(marca == "") ){
				return "Atributo Vazio"
			}
			
			try {
				Number( this.barCode )
			}
			catch( e:Error ){
				return "O Código de barras deve ser um número"
			}	
			return null
		}
		
		public static function validateField( value:String, field:String = "" ):Boolean{
			if( value == "" ){
				return false
			}else if( field == "codigoDeBarras" ){
				try {
					Number( value )
				}
				catch( e:Error ){
					return false
				}		
			}
			return true
		}
		
		public function equalsId(outroProduto:Produto):Boolean{
			return this.id == outroProduto.id;

		}
		
		public function toString():String {
            return "Cód. barras: " + (this.barCode ? this.barCode : "-") +
                    "\nDescrição: " + this.descricao +
                    "\nEmbalagem: " + (this.embalagem ? this.embalagem : "-")+
                    "\nMarca: "+ (this.marca ? this.marca : "-") +
                    "\nFabricante: " +(this.fabricante ? this.fabricante : "-");
		}

        public function get qtdEmbalagem():int {
            return parseInt(this.embalagem.slice(3, 7));
        }
    }
}