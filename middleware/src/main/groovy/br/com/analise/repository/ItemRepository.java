package br.com.analise.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.analise.domain.Item;

public interface ItemRepository extends MongoRepository<Item, String> {

}
