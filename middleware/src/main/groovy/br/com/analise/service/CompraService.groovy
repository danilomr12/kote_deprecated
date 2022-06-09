package br.com.analise.service

import com.sun.corba.se.spi.ior.ObjectId;
import org.perf4j.aop.Profiled;
import org.springframework.beans.factory.annotation.Autowired
/*import org.springframework.cache.annotation.Cacheable;*/
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator

import br.com.analise.domain.Compra;
import br.com.analise.domain.Empresa
import br.com.analise.domain.Item;
import br.com.analise.repository.CompraRepository;
import br.com.analise.repository.EmpresaRepository;
import br.com.analise.repository.ItemRepository
import br.com.analise.domain.RespostaCompra
import br.com.analise.repository.RespostaCompraRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Update

@Repository
@Transactional
public class CompraService implements ICompraService{

    @Autowired CompraRepository compraRepository;
    @Autowired ItemRepository itemRepository;
    @Autowired EmpresaRepository empresaRepository;
    @Autowired RespostaCompraRepository respostaCompraRepository;
    @Autowired MongoTemplate mongoTemplate

    @Autowired
    private Validator validator;

    @Profiled
    public Compra saveCompra(Compra compra){
        if(validate(compra, "compra")){
            compra.itens = itemRepository.save(compra.itens)
            return compraRepository.save(compra)
        }
        return null
    }

    @Profiled
    public void saveRespostaCompraAndUpdateCompraByCotacaoId(List<RespostaCompra> respostaCompraList, Long cotacaoId){
        respostaCompraList = respostaCompraRepository.save(respostaCompraList)
        updateRespostaCompraByCotacaoId(respostaCompraList, cotacaoId)
    }

    @Profiled
    public Empresa saveEmpresa(Empresa empresa){
        return empresaRepository.save(empresa);
    }

    @Profiled
    /*@Cacheable("compras")*/
    public Compra getCompra(String id) {
        return compraRepository.findOne(id);
    }

    @Profiled
    public void deleteCompra(String id) {
        def compra = compraRepository.findOne(id);
        if(compra){
            itemRepository.delete(compra.itens)
            respostaCompraRepository.delete(compra.respostasCompra)
            compraRepository.delete(compra)
        }

    }

    @Profiled
        public Compra getCompraByCotacaoId(Long id) {
        return compraRepository.findByIdCotacao(id);
    }

    public String getCompraIdByCotacaoId(Long id){
        return compraRepository.findByIdCotacao(id).id
    }

    @Profiled
    public void updateItens(List<Item> itens){
        itens.collect { Item item ->
            def update = new Update()
            update.set("respostas", item.respostas)
            update.set("naoComprar", item.naoComprar)
            update.set("quantidade", item.quantidade)
            mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(item.id)),
                    update, Item.class )
        }
    }

    @Profiled
    public void removeRespostasFromItens(Long cotacaoId, Long respostaId){
        Compra compra = getCompraByCotacaoId(cotacaoId)
        for( Item item in compra.itens){
            item?.respostas?.removeAll {it?.respostaCompra?.idResposta == respostaId }
        }
        updateItens(compra.itens)
    }

    @Profiled
    public void updateEstadoCompraByCotacaoId(Long cotacaoId, Integer estadoCotacao){
        mongoTemplate.updateFirst(Query.query(Criteria.where("idCotacao").is(cotacaoId)),
                Update.update("estado", estadoCotacao), Compra.class)
    }

    @Profiled
    public void updateRespostaCompraByCotacaoId(List<RespostaCompra> respostasCompra, Long cotacaoId){
        Compra compra = compraRepository.findByIdCotacao(cotacaoId)
        if(compra.respostasCompra == null || compra.respostasCompra?.size()<=0)
            compra.respostasCompra = new ArrayList<RespostaCompra>()
         compra.respostasCompra.addAll(respostasCompra)
        compraRepository.save(compra)
        /* problema com update de campos DBRef
        mongoTemplate.updateFirst(Query.query(Criteria.where("idCotacao").is(cotacaoId)),
                Update.update("respostasCompra", respostasCompra), Compra.class)*/
    }

    @Profiled
    public void updateRespostaCompra(Long respostaId, Integer estadoResposta){
        mongoTemplate.updateFirst(Query.query(Criteria.where("idResposta").is(respostaId)),
                new Update().set("estadoResposta", estadoResposta), RespostaCompra.class)
    }

    @Profiled
    public Empresa getEmpresaByEmpresaId(Long id){
        return empresaRepository.findByIdEmpresa(id)
    }

    @Profiled
    public void analiseCompra(Long cotacaoId){
        Compra compra = compraRepository.findByIdCotacao(cotacaoId)

        compra.itens.each {Item item ->
            item.naoComprar = false
            item?.respostas?.sort {br.com.analise.domain.Resposta first, br.com.analise.domain.Resposta sec ->
                if (first.preco == null || first.preco == 0)
                    return 1
                if (sec.preco == null || sec.preco == 0)
                    return -1
                return first.preco <=> sec.preco
            }
        }
        itemRepository.save(compra.itens)
    }

    public Double getTotalComprasDoPeriodo(Date dataValidadeInicio, Date dataValidadeFim, Long empresaId){
        def compras = mongoTemplate.find(Query.query(Criteria.where("empresaId").is(empresaId).and("dataCriacao").gte(dataValidadeInicio).lte(dataValidadeFim)), Compra.class)
        Double totalGeral = 0
        compras.each{Compra compra ->
            Double totalDaCompra = 0
            compra.itens.each {
                if(!it.naoComprar) {
                       if(it?.respostas && it?.respostas?.size()>=1)
                        totalDaCompra += it?.respostas?.get(0)?.preco *it?.quantidade
                }
            }
            def update = new Update();
            update.set("total", totalDaCompra);
            mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(compra.id)), update, Compra.class );
            totalGeral += totalDaCompra;
        }
        return totalGeral
    }

    public Map<Long, Double> gePrecosPagosDeItensDeCotao(List<Long> idsItemCotacao){
        Map<Long, Double> historicoPrecos = [:]
        def itens = mongoTemplate.find(Query.query(Criteria.where("idItemCompra").in(idsItemCotacao)),Item.class)
        itens.each {Item item->
            historicoPrecos.put(item.idItemCompra, item.respostas.get(0).preco)
        }
        historicoPrecos
    }

    private Boolean validate(Object domainClass, String objectName) {
        Errors validationErrors = new BeanPropertyBindingResult(domainClass, objectName)
        validator.validate(domainClass, validationErrors)
        if(validationErrors.hasErrors()){
            print(validationErrors.allErrors)
            return false
        }
        return true
    }
}
