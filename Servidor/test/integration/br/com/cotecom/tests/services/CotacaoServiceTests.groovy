package br.com.cotecom.tests.services

import br.com.cotecom.domain.analise.Analise
import br.com.cotecom.domain.analise.ItemAnalise
import br.com.cotecom.domain.analise.ItemRespostaFact
import br.com.cotecom.domain.dto.cotacao.CotacaoDTO
import br.com.cotecom.domain.dto.cotacao.ItemCotacaoDTO
import br.com.cotecom.domain.dto.tela.TelaCotacaoDTO
import br.com.cotecom.domain.pedido.Pedido
import br.com.cotecom.domain.resposta.EstadoResposta
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.empresa.Cliente
import br.com.cotecom.domain.usuarios.empresa.Endereco
import br.com.cotecom.domain.usuarios.empresa.Fornecedor
import br.com.cotecom.domain.usuarios.empresa.Telefone
import br.com.cotecom.dtos.assembler.CotacaoAssembler
import br.com.cotecom.dtos.assembler.EnderecoAssembler
import br.com.cotecom.services.CotacaoService
import br.com.cotecom.services.remote.RemoteCotacaoService
import br.com.cotecom.util.documents.excel.ExcelExport
import br.com.cotecom.util.documents.excel.ExcelFile
import br.com.cotecom.util.fixtures.CotacaoFixture
import grails.test.GrailsUnitTestCase
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.hibernate.SessionFactory
import br.com.cotecom.domain.cotacao.*
import br.com.cotecom.domain.item.*

public class CotacaoServiceTests extends GrailsUnitTestCase {

    boolean transactional = true
    Produto produto
    Comprador comprador1
    Endereco endereco
    ICotacao iCotacao
    Representante representante1
    Representante representante2
    CotacaoDTO cotacaoDTO
    RemoteCotacaoService remoteCotacaoService
    ItemCotacao itemCotacao
    CotacaoService cotacaoService

    SessionFactory sessionFactory

    void setUp(){
        super.setUp()

        produto = new Produto(descricao:"produto1", categoria:"categoria1")
        produto.save(flush:true)

        Fornecedor fornecedor = new Fornecedor(nomeFantasia:"empresa1", razaoSocial:"empresa1 LTDA", email: "fornecedor1@idra.com.br")
        fornecedor.save(flush:true)

        Cliente cliente = new Cliente(nomeFantasia:"cliente1", razaoSocial:"cliente1 LTDA", email: "cliente1@idra.com.br")
        cliente.save(flush:true)

        comprador1 = new Comprador(nome:"comprador1", password:"nsn32fs8", email:"comprador1@cotecom.com.br",
                empresa: cliente)
        comprador1.save(flush:true)


        representante1 = new Representante(nome: "representante1", password: "n13ffs8",
                email: "representante1@cotecom.com.br", empresa: fornecedor)
        representante1.save(flush:true)
        representante2 = new Representante(nome: "representante2", password: "n23445g",
                email: "representante2@cotecom.com.br", empresa: fornecedor)
        representante2.save(flush:true)

        endereco = new Endereco(logradouro:"rua 1", numero: 1, cidade: "goiania", estado:"GO", bairro: 'centro')
        cotacaoDTO = new CotacaoDTO(titulo: "cotacaoDTO de teste", dataCriacao: new Date(2010,10,10),
                dataEntrega: new Date(2010,10,10),prazoPagamento: "35", compradorId: comprador1.id,
                dataValidade: new Date(2010,10,10), enderecoEntrega: EnderecoAssembler.crieEnderecoDTO(endereco),
                codigoEstadoCotacao: 0)

        itemCotacao = new ItemCotacao(produto: produto, quantidade: 12)
    }

    void tearDown(){
        super.tearDown()
    }

    void testAdicioneItem(){
        iCotacao = CotacaoFactory.crie("cotacao de teste", "mensagem", new Date(2010,10,10),
                new Date(2010,10,10), "28", null, endereco)
        iCotacao.addToItens(itemCotacao)
        assertNotNull iCotacao.itens
        assertTrue iCotacao.itens.any {
            it.equals(itemCotacao)
        }
    }

    void testSalveEAtualizeCotacao(){
        List<ItemCotacaoDTO> itemCotacaoDTOs = new ArrayList()

        TelaCotacaoDTO novaCotacaoDTO = new TelaCotacaoDTO(itensCotacaoDTO: itemCotacaoDTOs, cotacaoDTO: cotacaoDTO)
        novaCotacaoDTO = remoteCotacaoService.saveCotacao(novaCotacaoDTO)

        assertNotNull novaCotacaoDTO.cotacaoDTO

        ICotacao cotacao1 = Cotacao.get(novaCotacaoDTO.cotacaoDTO.id)
        assertNotNull cotacao1
        assertEquals cotacao1.estado, EstadoCotacao.estado.get(EstadoCotacao.RASCUNHO)
        assertEquals EstadoCotacao.estado.get(cotacao1.codigoEstadoCotacao), EstadoCotacao.estado.get(EstadoCotacao.RASCUNHO)

        novaCotacaoDTO.cotacaoDTO.titulo = "cotacao de teste atualizada"
        novaCotacaoDTO.cotacaoDTO.enderecoEntrega.logradouro = "rua atualizada"

        novaCotacaoDTO = remoteCotacaoService.saveCotacao(novaCotacaoDTO)
        assertNotNull novaCotacaoDTO

        assertNotNull novaCotacaoDTO.cotacaoDTO

        ICotacao cotacao2 = Cotacao.get(novaCotacaoDTO.cotacaoDTO.id)
        assertEquals cotacao2.enderecoEntrega.logradouro, "rua atualizada"
    }

    void testCanceleCotacao(){
        List<ItemCotacaoDTO> itemCotacaoDTOs = new ArrayList()

        TelaCotacaoDTO novaCotacaoDTO = new TelaCotacaoDTO(itensCotacaoDTO: itemCotacaoDTOs, cotacaoDTO: cotacaoDTO)
        novaCotacaoDTO = remoteCotacaoService.saveCotacao(novaCotacaoDTO)

        remoteCotacaoService.cancele(novaCotacaoDTO.cotacaoDTO.id)
        ICotacao cotacao1 = Cotacao.get(novaCotacaoDTO.cotacaoDTO.id)
        assertNotNull cotacao1

        assertEquals EstadoCotacao.descricao.get(EstadoCotacao.CANCELADA),
                EstadoCotacao.descricao.get(cotacao1.codigoEstadoCotacao)
        assertEquals EstadoCotacao.estado.get(EstadoCotacao.CANCELADA),
                cotacao1.estado
    }

    void testAdicioneSalveItensCotacaoDTO(){
        iCotacao = CotacaoFactory.crie("cotacao de teste", "mensagem", new Date(2010,10,10),
                new Date(2010,10,10), "28", null, endereco)
        CotacaoDTO cotacaoDTO = CotacaoAssembler.crieCotacaoDTO(iCotacao)
        ItemCotacaoDTO itemCotacaoDTO = new ItemCotacaoDTO(cotacaoId: cotacaoDTO.id, quantidade: 12,
                produtoId: produto.id)
        List<ItemCotacaoDTO> itemCotacaoDTOs = new ArrayList()
        TelaCotacaoDTO  novaCompletaDTO = new TelaCotacaoDTO(itensCotacaoDTO: itemCotacaoDTOs, cotacaoDTO: cotacaoDTO)
        novaCompletaDTO = remoteCotacaoService.saveCotacao(novaCompletaDTO)
        assertNotNull novaCompletaDTO

        ICotacao cotacao1 = iCotacao.get(novaCompletaDTO.cotacaoDTO.id)
        assertNotNull cotacao1
        assertEquals cotacao1.titulo, cotacaoDTO.titulo
        assertNull cotacao1.itens
        assertNotNull novaCompletaDTO.cotacaoDTO

        novaCompletaDTO.itensCotacaoDTO.push(itemCotacaoDTO)
        novaCompletaDTO.itensCotacaoDTO.push(new ItemCotacaoDTO(quantidade:12, produtoId: produto.id,
                cotacaoId: cotacao1.id))
        novaCompletaDTO.itensCotacaoDTO.push(new ItemCotacaoDTO(quantidade:12, produtoId: produto.id,
                cotacaoId: cotacao1.id))
        novaCompletaDTO = remoteCotacaoService.saveCotacao(novaCompletaDTO)
        assertNotNull novaCompletaDTO.cotacaoDTO
        assertEquals 3, novaCompletaDTO.itensCotacaoDTO.size()

        cotacao1 = iCotacao.get(novaCompletaDTO.cotacaoDTO.id)
        assertEquals cotacao1.itens.size(), 3

        novaCompletaDTO.itensCotacaoDTO.each {ItemCotacaoDTO it->
            it.quantidade = 10
            it.saved = false
        }
        novaCompletaDTO = remoteCotacaoService.saveCotacao(novaCompletaDTO)
        cotacao1 = iCotacao.get(novaCompletaDTO.cotacaoDTO.id)
        cotacao1.itens.each {ItemCotacao it->
            assertEquals it.quantidade, 10

        }
        assertEquals 3, cotacao1.itens.size()

        simulateNewRequest()

        remoteCotacaoService.removaItemCotacao(novaCompletaDTO.itensCotacaoDTO.get(0))
        simulateNewRequest()
        remoteCotacaoService.removaItemCotacao(novaCompletaDTO.itensCotacaoDTO.get(1))
        simulateNewRequest()


        novaCompletaDTO.itensCotacaoDTO.removeAll{it.id != 3}
        def itemRestante = novaCompletaDTO.itensCotacaoDTO.get(0)

        novaCompletaDTO = remoteCotacaoService.saveCotacao(novaCompletaDTO)
        assertEquals novaCompletaDTO.itensCotacaoDTO.size(),1
        cotacao1 = iCotacao.get(novaCompletaDTO.cotacaoDTO.id)

        assertTrue cotacao1.itens.any {
            it.id == itemRestante.id
        }

        assertEquals cotacao1.itens.size(),1

    }

    private def simulateNewRequest() {
        sessionFactory.getCurrentSession().flush()
        sessionFactory.getCurrentSession().clear()
    }

    void testEnvieCotacao(){

        iCotacao = CotacaoFactory.crie("cotacao de teste", "mensagem", new Date(2010,10,10), new Date(2010,10,10),
                "25", null, endereco)
        itemCotacao.cotacao = iCotacao

        CotacaoDTO cotacaoDTO = CotacaoAssembler.crieCotacaoDTO(iCotacao)
        TelaCotacaoDTO novaCotacaoDTO = new TelaCotacaoDTO(cotacaoDTO: cotacaoDTO, representantesId: new ArrayList(),
                itensCotacaoDTO: [CotacaoAssembler.crieItemCotacaoDTO(itemCotacao)])
        novaCotacaoDTO.representantesId.push(representante1.id)
        novaCotacaoDTO.representantesId.push(representante2.id)

        simulateNewRequest() // Após criação DTO começar com uma sessão 'limpa'

        assertNotNull "Falha ao enviar cotacao ${iCotacao.titulo}", remoteCotacaoService.envieCotacao(novaCotacaoDTO)

        Cotacao cotacaoEnviada = CotacaoAssembler.crieCotacao(novaCotacaoDTO.cotacaoDTO)

        assertEquals EstadoCotacao.descricao.get(EstadoCotacao.AGUARDANDO_RESPOSTAS), EstadoCotacao.descricao.get(cotacaoEnviada.codigoEstadoCotacao)
        assertEquals EstadoCotacao.estado.get(EstadoCotacao.AGUARDANDO_RESPOSTAS), cotacaoEnviada.estado

        assertEquals 2 , cotacaoEnviada.respostas.size()
        cotacaoEnviada.respostas.each {
            assertNotNull Resposta.get(it.id)
        }
    }

    void testGetAnalisePersistida() {
        def cotacaoRespondida = CotacaoFixture.crieCotacaoRespondida().save(flush:true)
        cotacaoService.analisar(cotacaoRespondida.id).save(flush:true)
        def analisePersistida = cotacaoService.getAnalise(cotacaoRespondida.id).save(flush:true)
        assertNotNull "Analise nao foi criada", analisePersistida
        assertNotNull "Analise deveria ter sido persistida", analisePersistida.id
    }

    void testAnaliseCotacao() {
        def cotacaoRespondida = CotacaoFixture.crieCotacaoRespondida()
        def analisePersistida = cotacaoService.analisar(cotacaoRespondida.id)
        assertNotNull "Analise nao foi criada", analisePersistida
        assertNotNull "Analise deveria ter sido persistida", analisePersistida.id
        assertTrue "Estado da cotacao deveria ter sido alterado para CotacaoEmAnalise",
                analisePersistida.cotacao.estado instanceof CotacaoEmAnalise
    }

    void testAnaliseCotacaoAguardandoRespostas() {
        Cotacao cotacaoAguardandoRespostas =
        CotacaoFixture.crieCotacaoAguardandoRespostasComDuasRespostasConcluidasEUmaNova()

        def analisePersistida = cotacaoService.analisar(cotacaoAguardandoRespostas.id)
        assertNotNull "Analise nao foi criada", analisePersistida
        assertNotNull "Analise deveria ter sido persistida", analisePersistida.id
        assertTrue "Estado da cotacao deveria ter sido alterado para CotacaoEmAnalise",
                analisePersistida.cotacao.estado instanceof CotacaoEmAnalise
        assertEquals analisePersistida.cotacao.respostas.asList().get(2).codigoEstado, EstadoResposta.CANCELADA
    }

    /*void testExporteAnaliseCotacaoExcel(){
        def cotacaoRespondida = CotacaoFixture.crieCotacaoRespondida()
        def analisePersistida = cotacaoService.analisar(cotacaoRespondida.id)
        cotacaoService.gerePedidosPrimeiraOrdem(cotacaoRespondida.id)
        Cotacao cotacao = Cotacao.get(analisePersistida.cotacao.id)

        String caminhoArquivoExcel = cotacaoService.exporteAnaliseExcel(cotacao.id)
        assertNotNull caminhoArquivoExcel

        ExcelFile excelFile = new ExcelFile(caminhoArquivoExcel)
        assertNotNull excelFile

        Sheet planilha1 = excelFile.getPlanilha(0)
        assertNotNull planilha1

        int indicePrimeiraLinhaPlanilha = 1

        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                cotacao.titulo + " - " + formateData(cotacao.dataCriacao)
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                "Prazo de pagamento - " + cotacao.prazoPagamento + " dias"
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                "Data de entrega - " + formateData(cotacao.dataEntrega)
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                "Validade da cotacao - " + formateData(cotacao.dataValidade)
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                "Endereço de entrega - " + formateEnderecoPrimeiraCelula(cotacao)
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,1,indicePrimeiraLinhaPlanilha),
                fomateEnderecoSegundaCelula(cotacao)
        indicePrimeiraLinhaPlanilha++

        indicePrimeiraLinhaPlanilha += cotacao.respostas.size()+4

        Iterator rowIterator = planilha1.rowIterator()

        while(rowIterator.hasNext()){
            Row linhaAnaliseCotacao = (Row)rowIterator.next()
            if(linhaAnaliseCotacao.rowNum >= indicePrimeiraLinhaPlanilha &&
                    linhaAnaliseCotacao.rowNum < indicePrimeiraLinhaPlanilha+cotacao.itens.size()){

                int itemCotacaoIdx = linhaAnaliseCotacao.rowNum -indicePrimeiraLinhaPlanilha
                ItemAnalise itemAnalise = cotacao.analise.itens.asList().get(itemCotacaoIdx)
                ItemCotacao itemCotacao = cotacao.itens.asList().get(itemCotacaoIdx)

                assertEquals itemCotacao.qtdPedida,
                        excelFile.getCellValue(0, 0, linhaAnaliseCotacao.rowNum)
                assertEquals itemCotacao.produto.descricao,
                        excelFile.getCellValue(0, 1, linhaAnaliseCotacao.rowNum)
                assertEquals itemCotacao.produto.embalagem.toString().toUpperCase(),
                        excelFile.getCellValue(0, 2, linhaAnaliseCotacao.rowNum)

                cotacao.respostasConcluidas.eachWithIndex {Resposta resposta, int i->
                    Cell celula = linhaAnaliseCotacao.getCell(3+i)
                    def valor = excelFile.getCellValueFirstPlan(celula)

                    assertTrue celula.getCellStyle().dataFormat == excelFile.workbook.createDataFormat().getFormat("#,##0.00")
                    assertTrue """Alinhamento da celula de indice ${celula.getRow().rowNum} x ${celula.getColumnIndex()}
                            esperado era ${CellStyle.ALIGN_CENTER} mas eh ${celula.getCellStyle().alignment}"""  ,
                            celula.getCellStyle().alignment == CellStyle.ALIGN_CENTER

                    List<ItemRespostaFact> itensRespostaOrdenados = itemAnalise.itensRespostaFact
                    assertTrue "linha: ${celula.getRow().rowNum}, celula: ${celula.getColumnIndex()}",
                            valor != null

                    ItemResposta itemResposta = resposta.itens.asList().get(itemCotacaoIdx)
                    Float preco = itemResposta.preco.embalagem

                    if(preco == null || preco == 0.toFloat()){
                        assertTrue "linha: ${celula.getRow().rowNum}, celula: ${celula.getColumnIndex()}",
                                valor == "-"
                    }else{
                        assertEquals preco, valor

                        if(resposta.id == itensRespostaOrdenados.get(0).resposta.id){
                            assertEquals "linha: ${celula.getRow().rowNum}, celula: ${celula.getColumnIndex()}",
                                    celula.getCellStyle().getFillForegroundColor(), new ExcelExport().COR_MENOR_PRECO
                        }else if(resposta.id == itensRespostaOrdenados.get(0).resposta.id){
                            assertEquals "linha: ${celula.getRow().rowNum}, celula: ${celula.getColumnIndex()}",
                                    celula.getCellStyle().getFillForegroundColor(), new ExcelExport().COR_SEGUNDO_MENOR_PRECO
                        }
                    }
                }

            }
        }
        //testando planilhas de pedidos
        cotacao.respostas.eachWithIndex {Resposta resposta, int index ->
            Pedido pedido = resposta.pedidos.asList().get(0)
            Sheet planPedido
            if(resposta.representante.empresa){
                planPedido = excelFile.getPlanilha("Pedido ${resposta.representante?.empresa?.nomeFantasia}")
            }else{
                planPedido = excelFile.getPlanilha("Pedido ${resposta.representante.nome}")
                if(!planPedido)
                    planPedido = excelFile.getPlanilha("Pedido ${resposta.representante.email}")                 
            }
            assertNotNull planPedido

            def numPlanilhaPedido = excelFile.workbook.getSheetIndex(planPedido.getSheetName())
            //todo: testar estilo das celulas
            assertEquals excelFile.getCellValue(numPlanilhaPedido,0,0), "${resposta.representante?.empresa?.nomeFantasia} / ${resposta.representante.nome}"
            assertEquals excelFile.getCellValue(numPlanilhaPedido,0,1), "${resposta.representante.email} / ${formateTelefones(resposta.representante.telefones)}"
            assertEquals excelFile.getCellValue(numPlanilhaPedido,0,2), "Ordem de compra:"
            assertEquals excelFile.getCellValue(numPlanilhaPedido,2,2), pedido.id.toString()
            assertEquals excelFile.getCellValue(numPlanilhaPedido,0,3), "Mix total:"
            assertEquals excelFile.getCellValue(numPlanilhaPedido,2,3), pedido.itens.size()
            assertEquals excelFile.getCellValue(numPlanilhaPedido,0,4), "Valor total:"
            assertEquals excelFile.getCellValue(numPlanilhaPedido,2,4), pedido.valorTotalPedido()
            assertEquals planPedido.getRow(4).getCell(2).getCellStyle().dataFormat,
                    excelFile.workbook.createDataFormat().getFormat("R\$#,##0.00")

            Iterator linhasIterator = planPedido.rowIterator()
            int indiceUltimaLinhaCabecalho = 5
            while(linhasIterator.hasNext()){
                Row linhaPedido = (Row)linhasIterator.next()
                if(linhaPedido.rowNum > indiceUltimaLinhaCabecalho &&
                        linhaPedido.rowNum <= indiceUltimaLinhaCabecalho+pedido.itens.size()){
                    int itemPedidoIdx = linhaPedido.rowNum-indiceUltimaLinhaCabecalho-1
                    ItemPedido itemPedido = pedido.itens.asList().get(itemPedidoIdx)

                    assertEquals itemPedido.itemRespostaFact.itemAnalise.qtdPedida,
                            excelFile.getCellValue(numPlanilhaPedido, 0, linhaPedido.rowNum)
                    assertEquals itemPedido.itemRespostaFact.itemAnalise.descricao,
                            excelFile.getCellValue(numPlanilhaPedido, 1, linhaPedido.rowNum)
                    assertEquals itemPedido.itemRespostaFact.itemAnalise.embalagem,
                            excelFile.getCellValue(numPlanilhaPedido, 2, linhaPedido.rowNum)
                    assertEquals itemPedido.itemRespostaFact.calculePrecoUnitario(),
                            excelFile.getCellValue(numPlanilhaPedido, 3, linhaPedido.rowNum)
                    assertEquals linhaPedido.getCell(3).getCellStyle().dataFormat,
                            excelFile.workbook.createDataFormat().getFormat("#,##0.00")
                    assertEquals linhaPedido.getCell(3).getCellStyle().alignment,
                            CellStyle.ALIGN_CENTER
                }
                if(linhaPedido.rowNum == indiceUltimaLinhaCabecalho + pedido.itens.size() + 1){
                    assertEquals excelFile.getCellValue(numPlanilhaPedido, 0, linhaPedido.rowNum), "Total pedido -->"
                    assertEquals excelFile.getCellValue(numPlanilhaPedido, 5, linhaPedido.rowNum), pedido.valorTotalPedido()
                    assertEquals linhaPedido.getCell(5).getCellStyle().dataFormat,
                            excelFile.workbook.createDataFormat().getFormat("R\$#,##0.00")
                }
            }
        }

        assertTrue excelFile.delete()
    }*/

    /*void testExporteAnaliseCotacaoExcelDeCotacaoComRespostaCancelada(){
        def cotacaoRespondida = CotacaoFixture.crieCotacaoAguardandoRespostasComDuasRespostasConcluidasEUmaNova()
        def analisePersistida = cotacaoService.analisar(cotacaoRespondida.id)
        cotacaoService.gerePedidosPrimeiraOrdem(cotacaoRespondida.id)
        Cotacao cotacao = Cotacao.get(analisePersistida.cotacao.id)

        String caminhoArquivoExcel = cotacaoService.exporteAnaliseExcel(cotacao.id)
        assertNotNull caminhoArquivoExcel

        ExcelFile excelFile = new ExcelFile(caminhoArquivoExcel)
        assertNotNull excelFile

        Sheet planilha1 = excelFile.getPlanilha(0)
        assertNotNull planilha1

        int indicePrimeiraLinhaPlanilha = 1

        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                cotacao.titulo + " - " + formateData(cotacao.dataCriacao)
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                "Prazo de pagamento - " + cotacao.prazoPagamento + " dias"
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                "Data de entrega - " + formateData(cotacao.dataEntrega)
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                "Validade da cotacao - " + formateData(cotacao.dataValidade)
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,0,indicePrimeiraLinhaPlanilha),
                "Endereço de entrega - " + formateEnderecoPrimeiraCelula(cotacao)
        indicePrimeiraLinhaPlanilha++
        assertEquals excelFile.getCellValue(0,1,indicePrimeiraLinhaPlanilha),
                fomateEnderecoSegundaCelula(cotacao)
        indicePrimeiraLinhaPlanilha++

        indicePrimeiraLinhaPlanilha += cotacao.respostas.size()+4

        Iterator rowIterator = planilha1.rowIterator()

        while(rowIterator.hasNext()){
            Row linhaAnaliseCotacao = (Row)rowIterator.next()
            if(linhaAnaliseCotacao.rowNum >= indicePrimeiraLinhaPlanilha &&
                    linhaAnaliseCotacao.rowNum < indicePrimeiraLinhaPlanilha+cotacao.itens.size()){

                int itemCotacaoIdx = linhaAnaliseCotacao.rowNum -indicePrimeiraLinhaPlanilha
                ItemAnalise itemAnalise = cotacao.analise.itens.asList().get(itemCotacaoIdx)
                ItemCotacao itemCotacao = cotacao.itens.asList().get(itemCotacaoIdx)

                assertEquals itemCotacao.qtdPedida,
                        excelFile.getCellValue(0, 0, linhaAnaliseCotacao.rowNum)
                assertEquals itemCotacao.produto.descricao,
                        excelFile.getCellValue(0, 1, linhaAnaliseCotacao.rowNum)
                assertEquals itemCotacao.produto.embalagem.toString().toUpperCase(),
                        excelFile.getCellValue(0, 2, linhaAnaliseCotacao.rowNum)

                cotacao.respostasConcluidas.eachWithIndex {Resposta resposta, int i->
                    Cell celula = linhaAnaliseCotacao.getCell(3+i)
                    def valor = excelFile.getCellValueFirstPlan(celula)

                    assertTrue celula.getCellStyle().dataFormat == excelFile.workbook.createDataFormat().getFormat("#,##0.00")
                    assertTrue """Alinhamento da celula de indice ${celula.getRow().rowNum} x ${celula.getColumnIndex()}
                            esperado era ${CellStyle.ALIGN_CENTER} mas eh ${celula.getCellStyle().alignment}"""  ,
                            celula.getCellStyle().alignment == CellStyle.ALIGN_CENTER

                    List<ItemRespostaFact> itensRespostaOrdenados = itemAnalise.itensRespostaFact
                    assertTrue "linha: ${celula.getRow().rowNum}, celula: ${celula.getColumnIndex()}",
                            valor != null

                    ItemResposta itemResposta = resposta.itens.asList().get(itemCotacaoIdx)
                    Preco preco = new Preco(embalagem: itemResposta.preco.embalagem)

                    if(preco == null || preco == 0.toFloat()){
                        assertTrue "linha: ${celula.getRow().rowNum}, celula: ${celula.getColumnIndex()}",
                                valor == "-"
                    }else{
                        assertEquals preco.embalagem, valor

                        if(resposta.id == itensRespostaOrdenados.get(0).respostaId){
                            assertEquals "linha: ${celula.getRow().rowNum}, celula: ${celula.getColumnIndex()}",
                                    celula.getCellStyle().getFillForegroundColor(), new ExcelExport().COR_MENOR_PRECO
                        } else if(resposta.id == itensRespostaOrdenados.get(1).respostaId){
                            assertEquals "linha: ${celula.getRow().rowNum}, celula: ${celula.getColumnIndex()}",
                                    celula.getCellStyle().getFillForegroundColor(), new ExcelExport().COR_SEGUNDO_MENOR_PRECO
                        }
                    }
                }

            }
        }
        //testando planilhas de pedidos
        cotacao.respostas.findAll {it.codigoEstado == EstadoResposta.PEDIDO_FATURADO }.eachWithIndex {Resposta resposta, int index ->
            Pedido pedido = resposta.pedidos.asList().get(0)
            Sheet planPedido
            if(resposta.representante.empresa){
                planPedido = excelFile.getPlanilha("Pedido ${resposta.representante?.empresa?.nomeFantasia}")
            }else{
                planPedido = excelFile.getPlanilha("Pedido ${resposta.representante.nome}")
                if(!planPedido)
                    planPedido = excelFile.getPlanilha("Pedido ${resposta.representante.email}")
            }
            assertNotNull planPedido

            def numPlanilhaPedido = excelFile.workbook.getSheetIndex(planPedido.getSheetName())
            //todo: testar estilo das celulas
            assertEquals excelFile.getCellValue(numPlanilhaPedido,0,0), "${resposta.representante?.empresa?.nomeFantasia} / ${resposta.representante.nome}"
            assertEquals excelFile.getCellValue(numPlanilhaPedido,0,1), "${resposta.representante.email} / ${formateTelefones(resposta.representante.telefones)}"
            assertEquals excelFile.getCellValue(numPlanilhaPedido,0,2), "Ordem de compra:"
            assertEquals excelFile.getCellValue(numPlanilhaPedido,2,2), pedido.id.toString()
            assertEquals excelFile.getCellValue(numPlanilhaPedido,0,3), "Mix total:"
            assertEquals excelFile.getCellValue(numPlanilhaPedido,2,3), pedido.itens.size()
            assertEquals excelFile.getCellValue(numPlanilhaPedido,0,4), "Valor total:"
            assertEquals excelFile.getCellValue(numPlanilhaPedido,2,4), pedido.valorTotalPedido()
            assertEquals planPedido.getRow(4).getCell(2).getCellStyle().dataFormat,
                    excelFile.workbook.createDataFormat().getFormat("R\$#,##0.00")

            Iterator linhasIterator = planPedido.rowIterator()
            int indiceUltimaLinhaCabecalho = 5
            while(linhasIterator.hasNext()){
                Row linhaPedido = (Row)linhasIterator.next()
                if(linhaPedido.rowNum > indiceUltimaLinhaCabecalho &&
                        linhaPedido.rowNum <= indiceUltimaLinhaCabecalho+pedido.itens.size()){
                    int itemPedidoIdx = linhaPedido.rowNum-indiceUltimaLinhaCabecalho-1
                    ItemPedido itemPedido = pedido.itens.asList().get(itemPedidoIdx)

                    assertEquals itemPedido.itemRespostaFact.itemAnalise.qtdPedida,
                            excelFile.getCellValue(numPlanilhaPedido, 0, linhaPedido.rowNum)
                    assertEquals itemPedido.itemRespostaFact.itemAnalise.descricao,
                            excelFile.getCellValue(numPlanilhaPedido, 1, linhaPedido.rowNum)
                    assertEquals itemPedido.itemRespostaFact.itemAnalise.embalagem,
                            excelFile.getCellValue(numPlanilhaPedido, 2, linhaPedido.rowNum)
                    assertEquals itemPedido.itemRespostaFact.calculePrecoUnitario(),
                            excelFile.getCellValue(numPlanilhaPedido, 3, linhaPedido.rowNum)
                    assertEquals linhaPedido.getCell(3).getCellStyle().dataFormat,
                            excelFile.workbook.createDataFormat().getFormat("#,##0.00")
                    assertEquals linhaPedido.getCell(3).getCellStyle().alignment,
                            CellStyle.ALIGN_CENTER
                }
                if(linhaPedido.rowNum == indiceUltimaLinhaCabecalho+pedido.itens.size() + 1){
                    assertEquals excelFile.getCellValue(numPlanilhaPedido, 0, linhaPedido.rowNum), "Total pedido -->"
                    assertEquals excelFile.getCellValue(numPlanilhaPedido, 5, linhaPedido.rowNum), pedido.valorTotalPedido()
                    assertEquals linhaPedido.getCell(5).getCellStyle().dataFormat,
                            excelFile.workbook.createDataFormat().getFormat("R\$#,##0.00")
                }
            }
        }


        assertTrue excelFile.delete()
    }*/

    private String formateEnderecoPrimeiraCelula(ICotacao cotacao) {
        String enderero = ""
        if(cotacao.enderecoEntrega.logradouro)
            enderero += cotacao.enderecoEntrega.logradouro
        if(cotacao.enderecoEntrega.complemento)
            enderero += ", " + cotacao.enderecoEntrega.complemento
        return enderero
    }

    private String fomateEnderecoSegundaCelula(ICotacao cotacao) {
        Endereco endereco1 = cotacao.enderecoEntrega
        String endereco = ""
        if(endereco1.cep){
            endereco += "cep:" + cotacao.enderecoEntrega.cep
        }
        if(endereco1.bairro){
            if(endereco1.cep)
                endereco1 += ", "
            endereco += "Setor " + cotacao.enderecoEntrega.bairro
        }
        if(endereco1.cidade){
            if(endereco1.bairro || endereco1.cep)
                endereco += " ,"
            endereco += cotacao.enderecoEntrega.cidade
        }
        if(endereco1.estado && endereco1.cidade){
            endereco += " - "
            endereco += cotacao.enderecoEntrega.estado
        }
        return endereco
    }

    private String formateTelefones(List telefones) {
        String result = ""
        telefones.eachWithIndex {Telefone telefone, int index ->
            result += "${telefone.toString()}"
            if(index<telefones.size()-1)
                result += " / "
        }
        return result
    }

    private String formateData(Date data) {
        return String.format('%td/%<tm/%<tY', data)
    }

}