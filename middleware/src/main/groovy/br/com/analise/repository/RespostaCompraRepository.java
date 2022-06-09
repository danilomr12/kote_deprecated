package br.com.analise.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.analise.domain.RespostaCompra;

public interface RespostaCompraRepository extends MongoRepository<RespostaCompra, String> {

}
