package br.com.analise.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.analise.domain.Empresa;

public interface EmpresaRepository extends MongoRepository<Empresa, String> {

    Empresa findByIdEmpresa(Long idEmpresa);
}
