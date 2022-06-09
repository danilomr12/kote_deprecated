package br.com.analise.service;

import java.rmi.Remote;
import java.rmi.RemoteException

import br.com.analise.domain.Compra;
import br.com.analise.domain.Empresa
import br.com.analise.domain.Item
import br.com.analise.domain.RespostaCompra;

public interface ICompraService extends Remote {

    public Compra getCompra(String id) throws RemoteException;

    /**
     * Salva a compra para iniciar o processo de análise. É necessário quando o comprador envia a cotação aos
     * seus fornecedores.
     * @param Compra não persistida com itens de compra também não persistidos
     */
    public Compra saveCompra(Compra compra) throws RemoteException;

    /**
     * @param id da cotacao
     * @return id da Compra
     */
    public String getCompraIdByCotacaoId(Long cotacaoId) throws RemoteException;

    /**
     * Atualiza estado da compra
     * @param cotacaoId - ida da cotacação correspondente no sistema
     * @param estadoCotacao - código do estado a ser atualizado
     */
    public void updateEstadoCompraByCotacaoId(Long cotacaoId, Integer estadoCotacao) throws RemoteException;

    /**
     * Atualiza estado de uma resposta dentro da compra, por exemplo ao representante enviar a resposta
     * @param respostaId - id da resposta no sistema Kote
     * @param estadoResposta - novo código de estado para atualizar
     */
    public void updateRespostaCompra(Long respostaId, Integer estadoResposta) throws RemoteException;

    /**
     * Salva respostaCompra não persisitida e as adiciona à compra correspondente
     * @param respostaCompraList
     * @param cotacaoId
     */
    public void saveRespostaCompraAndUpdateCompraByCotacaoId(List<RespostaCompra> respostaCompraList, Long cotacaoId) throws RemoteException;

    /**
     * Atualiza resposatas do item de compra. Necessário quando preço de um produto ou ordem das resposatas
     * de item a ser comprado é alterada, ou quando o status naoComprar de um item é alterado.
     * Quando o comprador está reordenando as respostas dos itens para concluir a análise da melhor compra
     * @param itens já persistidos
     */
    public void updateItens(List<Item> item) throws RemoteException;

    /**
     * Remove respostas dos itens de analise, ex.: comprador cancela resposta, reativa, etc
     * @param cotacaoId - id da cotação
     * @param respostaId - id da resposta
     */
    public void removeRespostasFromItens(Long cotacaoId, Long respostaId) throws RemoteException;

    /**
     * Salva a empresa. Deve ser chamado antes de salvar a compra.
     * @param empresa
     * @return empresa salva
     */
    public Empresa saveEmpresa(Empresa empresa) throws RemoteException;

    /**
     * Busca Compra pelo id da cotação do sistema Kote
     * @param id da cotação correspondente à compra
     * @return Compra correspondente
     */
    public Compra getCompraByCotacaoId(Long id) throws RemoteException;

    /**
     * Busca Empresa pelo id correspondente do sistema Kote
     * @param id da Empresa no Kote
     * @return Empresa correspondente
     */
    public Empresa getEmpresaByEmpresaId(Long id) throws RemoteException;

    /**
     * Ordena itens da compra baseado em menor preço e os persiste
     * @param id da cotação correspondente do sistema cliente
     */
    public void analiseCompra(Long cotacaoId) throws RemoteException;


    /**
     * @param dataValidadeInicio - data de validade da cotação de início do intervalo a ser somado
     * @param dataValidadeFim - data de validade da cotação de fim do intervalo a ser somado
     * @param empresaId - id da empresa relativa ao relatório
     * @return total de compras do intervalo requisitado
     * @throws RemoteException
     */
    public Double getTotalComprasDoPeriodo(Date dataValidadeInicio, Date dataValidadeFim, Long empresaId) throws RemoteException;

    /**
     * @param empresaClienteId - id da empresa que se deseja consultar o histórico de preços do produto
     * @param idProduto - id do produto a ser consultado
     * @return mapa com chave sendo datas em que o produto foi compro e o preço da embalagem do produto em que foi compro neste dia
     * @throws RemoteException
     */
    public Map<Long, Double> gePrecosPagosDeItensDeCotao(List<Long> idsItemCotacao) throws RemoteException;

    /**
     *
     * @param id da Compra
     * @throws RemoteException
     */
    public void deleteCompra(String id) throws RemoteException;
}

