package br.com.cotecom.services

import br.com.cotecom.domain.item.Produto
import br.com.cotecom.util.documents.excel.ExcelFile
import br.com.cotecom.util.documents.excel.ExcelImport
import org.apache.log4j.Logger
import org.hibernate.classic.Session
import br.com.cotecom.domain.usuarios.empresa.Empresa
import br.com.cotecom.domain.item.ItemCotacao

class ProdutoService {

    private static final log = Logger.getLogger(ProdutoService.class)

    def sessionFactory
    def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
    private static final Integer MAX_RESULTS = 200
    def fileService
    def usuarioService

    def Produto save(Produto produto) {
        produto.save(flush: true)
        if (!produto.hasErrors())
            return produto
        log.debug produto.errors.eachWithIndex {error, e -> log.debug "Erro: ${error}"}
        return null
    }

    def save(List<Produto> produtoList) {
        List<Produto> listaAuxiliar = (List<Produto>) produtoList.clone()
        produtoList.each {Produto produto ->
            if (!this.save(produto))
                listaAuxiliar.remove(produto)
            else
                log.debug produto.errors.eachWithIndex {error, e -> log.debug "Erro: ${error}"}
        }
        if (listaAuxiliar.isEmpty())
            return false
        return listaAuxiliar
    }

    Boolean remove(def id ) {
        if (id == null)
            return false
        Produto produto = Produto.get(id)
        if (produto) {
            def pertenceAAlgumaCotacao = ItemCotacao.findByProduto(produto)
            if(pertenceAAlgumaCotacao)   {
                produto.dataDelecao = new Date()
                return (this.save(produto) != null)
            }else {
                produto.delete()
                return !produto.hasErrors()
            }
        }
        return false
    }

    public List<Produto> search(String query) {
        def expressaoRegularEspacoBranco = /\s*/
        if (query == null || query ==~ expressaoRegularEspacoBranco)
            return search()

        query = prepareQuery(query)
        def produtos
        try{
            produtos = Produto.searchEvery(query)?.sort{it.descricao}
        }catch (Exception e){
            log.error("Usuário: " +  usuarioService.sessionUser + ". Erro ao buscar " + query)
            e.printStackTrace()
        }

        if (produtos.size() == 0)
            return null
        return produtos

    }

    private String prepareQuery(String query) {
        String newQuery = ""
        query.each {
            if (it == "[" || it == "]" || it == "+" || it == "-" || it == "(" || it == "&" || it == "|" ||
                    it == "!" || it == "{" || it == "}" || it == "^" || it == '"' || it == "~" || it == "*" ||
                    it == "?" || it == ":" || it == "\'" || it == "/" ) {
                newQuery += " "
            } else {
                newQuery += it
            }
        }
        newQuery = newQuery.replaceAll(/(\s)+/, "* *")
        if(newQuery.length() > 1) {
            if(newQuery.substring(0, 2) != "* "){
                newQuery = "+*" +newQuery
            }else{
                newQuery = "+" +newQuery
            }
            if(newQuery.substring(newQuery.length() - 2, newQuery.length()) != "* ") {
                newQuery = newQuery + "* "
            }
        }
        newQuery = newQuery + " -possuiDataDelecao: true"
        return newQuery
    }

    public List<Produto> search() {
        def criteria = Produto.createCriteria()
        def listProdutos = criteria.list {
            isNull("dataDelecao")
            /*and {
                isNull("dataModificacao")
            }*/
            maxResults(MAX_RESULTS)
        }
        return listProdutos
    }

    def importePlanilhaProdutos(String caminhoPlanilha, Empresa empresa) {
        List<Produto> produtos = new ExcelImport(new ExcelFile(caminhoPlanilha)).importProdutos(0, empresa);
        produtos = batchInsertProdutos(produtos) as List<Produto>
        Produto.reindex()
        return produtos
    }

    def batchInsertProdutos(List<Produto> produtos) {
        def produtosSalvos = new ArrayList<Produto>()
        produtos.eachWithIndex { Produto produto, int index ->
            produtosSalvos.add(updateOrInsertBook(produto))
            if (index % 100 == 0) cleanUpGorm()
        }
        cleanUpGorm()
        Produto.reindex()
        return produtosSalvos
    }

    void cleanUpGorm() {
        Session session = sessionFactory.currentSession
        session.flush()
        session.clear()
        propertyInstanceMap.get().clear()
    }

    Produto updateOrInsertBook(Produto produtoASalvar) {
        Produto produtoExistente = produtoASalvar.barCode ? Produto.findByBarCode(produtoASalvar.barCode) : null
        if (produtoExistente) {
            produtoExistente.descricao = produtoASalvar.descricao
            produtoExistente.categoria = produtoASalvar.categoria
            produtoExistente.embalagem = produtoASalvar.embalagem
            produtoExistente.fabricante = produtoASalvar.fabricante
            produtoExistente.marca = produtoASalvar.marca
            produtoExistente.peso = produtoASalvar.peso
            produtoExistente.dataDelecao = null;
            produtoExistente.dataModificacao = new Date();
            return produtoExistente.save(flush:true)
        } else {
            return produtoASalvar.save(flush:true)
        }
    }

}