package br.com.cotecom.dtos.assembler

import br.com.cotecom.domain.dto.produto.ProdutoDTO
import br.com.cotecom.domain.item.EmbalagemVenda
import br.com.cotecom.domain.item.Produto
import br.com.cotecom.domain.item.TipoEmbalagem

public static class ProdutoAssembler {

    static ProdutoDTO crieProdutoDTO(Produto produto){
        if(!produto)
            return null
        return new ProdutoDTO(descricao: produto.descricao?.toUpperCase(), categoria: produto.categoria?.toUpperCase(), barCode: produto.barCode?.toUpperCase(),
            embalagem: produto.embalagem?.toString()?.toUpperCase(), peso: produto.peso, id: produto.id, marca: produto.marca?.toUpperCase(),
                fabricante: produto.fabricante?.toUpperCase(), version: produto.version, qtdMaster: produto.qtdMaster, empresaId: produto.empresaId)
    }

    static Produto crieProduto(ProdutoDTO produtoDTO){
        Produto produto
        if(!produtoDTO)
            return null
        if(produtoDTO.id == null){
            produto = new Produto(embalagem: new EmbalagemVenda(tipoEmbalagemDeVenda: new TipoEmbalagem(),
                    tipoEmbalagemUnidade: new TipoEmbalagem())) }
        else {
            produto = Produto.get(produtoDTO.id)
        }
        produto.barCode = produtoDTO.barCode
        produto.descricao = produtoDTO.descricao
        produto.categoria = produtoDTO.categoria
        produto.embalagem = EmbalagemVenda.setEmbalagem(produtoDTO.embalagem)
        produto.qtdMaster = produtoDTO.qtdMaster
        produto.fabricante = produtoDTO.fabricante
        produto.marca = produtoDTO.marca
        produto.peso = produtoDTO.peso
        produto.empresaId = produtoDTO.empresaId
        return produto
    }
}