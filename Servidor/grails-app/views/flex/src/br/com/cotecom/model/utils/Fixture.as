package br.com.cotecom.model.utils
{
import mx.collections.ArrayCollection;

public class Fixture
	{
		
		public function Fixture()
		{
		}

		public function createAnaliseCotacaoDummy():ArrayCollection
		{
			return new ArrayCollection([
				{qtddProduto="5", descricaoProduto="Produto A", opcaoPrimRepresentante="primProd", opcaoSegRepresentante="segProd", difProdutos="0.4"},
			]);	
			
			
		}

	}
}