package br.com.analise.service

import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import br.com.analise.domain.Compra;
import br.com.analise.domain.Empresa;
import br.com.analise.domain.Item;
import br.com.analise.domain.Resposta;

@Service
@Transactional
public class BootstrapService {

	@PostConstruct
	private void bootstrap() {
	}
	
	private void get() {	
		/*println("Obtendo a análise...")
		Compra compra = mongoTemplate.findOne(new Query(Criteria.where("idCompra").is("1")), Compra.class);
		analiseRepository.findAll()
		println("Analise obtida")*/
	}

	private void save() {
		ArrayList<String> telefones = new ArrayList<String>();
		telefones.add("39451446");
		telefones.add("2872670");

		Empresa fornecedor = new Empresa();
		fornecedor.nomeFantasia = "Empresa Atacado XYZ"
		fornecedor.nomeResponsavel = "Fornecedor";
		fornecedor.email = "fornecedor@grupojatoba.com";
		fornecedor.telefones = telefones;
		mongoTemplate.save(fornecedor);

		Empresa comprador = new Empresa();
		comprador.nomeFantasia = "Empresa Compras XYZ";
		comprador.nomeResponsavel = "Comprador";
		comprador.email = "comprador@grupojatoba.com";
		comprador.telefones = telefones;
		mongoTemplate.save(comprador);


		ArrayList<Item> itens = new ArrayList<Item>();
		for (int i = 0; i < 1000; i++){
			ArrayList<Resposta> respostas = new ArrayList<Resposta>();
			for (int j = 0; j < 12; j++) {
				Resposta resposta = new Resposta();
				resposta.preco = i*j;
				resposta.observacao = "obs"+j;

				resposta.idItemResposta = j;
				//mongoOperation.save(resposta);
				respostas.add(resposta);
			}
			Item itemCompra = new Item();
			itemCompra.categoria = "categoria";
			itemCompra.codigoBarras = "12481658"+i;
			itemCompra.descricao = "descricao";
			itemCompra.embalagem = "embalagem";
			itemCompra.fabricante = "fabricante";
			itemCompra.marca = "marca";
			itemCompra.peso = "300g";
			itemCompra.idItemCompra = 1;
			itemCompra.quantidade = 10;
			itemCompra.respostas = respostas;
			mongoTemplate.save(itemCompra);
			itens.add(itemCompra);
		}

		Compra compra = new Compra();
		compra.titulo = "Compra"
		compra.mensagem = "Mensagem da compra";
		compra.endereco = "Endereço de compra";
		compra.dataEntrega = new Date();
		compra.prazoPagamento = "30/60/90";
		compra.compradorId = new Random().nextInt(100)
		compra.itens = itens;
		compra.idCotacao = 1;
		/*compra.respostasCompra = 10.collect {
            RespostaCompra respostaCompra = new RespostaCompra()
            respostaCompra.idResposta = it;
            respostaCompra.estadoResposta = 0;
            respostaCompra.idRepresentante = new Random().nextInt(100);
            respostaCompra.nomeRepresentante = "rep" + it
            respostaCompra.nomeFantasiaEmpresa = "empresa" + it
            compraService.saveRespostaCompra(respostaCompra)
        }*/
		compraService.saveCompra(compra)
	}
}
