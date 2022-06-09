package br.com.cotecom.services.remote

import br.com.cotecom.domain.dto.produto.ProdutoDTO
import br.com.cotecom.domain.item.EmbalagemVenda
import br.com.cotecom.domain.item.ItemCotacao
import br.com.cotecom.domain.item.Produto
import br.com.cotecom.dtos.assembler.ProdutoAssembler
import br.com.cotecom.util.Path.Path
import org.apache.log4j.Logger

public class RemoteProdutoService {

    private static final log = Logger.getLogger(RemoteProdutoService.class)

    boolean transactional = true
    static expose = ["flex-remoting"]
    
    def produtoService
    def fileService
    def usuarioService
    def grailsApplication

    public List<ProdutoDTO> search(String query) {
        return produtoService.search(query).collect {Produto produto ->
            ProdutoAssembler.crieProdutoDTO(produto)
        }
    }

    public List<ProdutoDTO> search(){
        return produtoService.search().collect {Produto produto->
            ProdutoAssembler.crieProdutoDTO(produto)
        }
    }

    List<ProdutoDTO> saveProdutos(List<ProdutoDTO> produtos){
        return produtos.collect {ProdutoDTO produtoDTO ->
            this.saveProduto(produtoDTO)
        }
    }

    def saveProduto(ProdutoDTO produtoDTO){
        Produto produtoExistente = Produto.get(produtoDTO.id)
        if(produtoExistente && ItemCotacao.findByProduto(produtoExistente)){
            Produto produtoNovo = crieNovoProduto(produtoDTO)
            produtoNovo.dataModificacao = new Date()
            produtoNovo = produtoService.save(produtoNovo)
            produtoExistente.dataDelecao = new Date()
            produtoService.save(produtoExistente)
            return ProdutoAssembler.crieProdutoDTO(produtoNovo)
        }
        return ProdutoAssembler.crieProdutoDTO(produtoService.save(ProdutoAssembler.crieProduto(produtoDTO)))

    }

    Produto crieNovoProduto(ProdutoDTO produtoDTO) {
        Produto produto = new Produto(barCode: produtoDTO.barCode, descricao: produtoDTO.descricao,
                embalagem: EmbalagemVenda.setEmbalagem(produtoDTO.embalagem), categoria: produtoDTO.categoria,
                fabricante: produtoDTO.fabricante, marca: produtoDTO.marca, peso: produtoDTO.peso,
                empresaId:  usuarioService.sessionUser.empresa.id)
        return produto
    }

    def remove(ProdutoDTO produtoDTO){
        return produtoService.remove(produtoDTO.id)
    }

    def importePlanilhaProdutos(byte[] bytes){
        File arquivo = fileService.graveArquivo(bytes, new Path().getPathArquivos() + File.separator +
                "planilha_produtos_temporaria.xls");
        def result = produtoService.importePlanilhaProdutos(arquivo.path, usuarioService.sessionUser.empresa)
        arquivo.delete()
        if(result)
            return true
        return false
    }

    String donwloadPlanilhaExemplo(){
        //CH.config.grails.serverURL
        //serverURL is defined manually in config.groovy
        //return getServerRootUrl(grailsApplication.config.grails.serverURL)+"/arquivos/test/planilha_exemplo_importacao.xls"
        return "/arquivos/test/planilha_exemplo_importacao.xls"
    }

    private String getServerRootUrl(String serverURL) {
        String root = ""
        int barrasCout = 0
        serverURL.each {
            if(it=="/")
                barrasCout ++
            if(barrasCout<3)
               root += it
        }
        return root
    }
}