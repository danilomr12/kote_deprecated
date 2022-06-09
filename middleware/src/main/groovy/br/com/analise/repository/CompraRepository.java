package br.com.analise.repository;

import br.com.analise.domain.Compra;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface CompraRepository extends MongoRepository<Compra, String> {

    Compra findByIdCotacao(Long cotacaoId);

    List<Compra> findByEmpresaIdAndDataValidadeBetween(Date inicio, Date fim, Long empresaId);

    List<Compra> findByDataValidadeBetween(Date inicio, Date fim);

}
