package br.com.cotecom.test

import br.com.cotecom.domain.cotacao.Cotacao
import br.com.cotecom.domain.item.EmbalagemVenda
import br.com.cotecom.domain.item.Produto
import br.com.cotecom.domain.item.TipoEmbalagem
import br.com.cotecom.domain.usuarios.Comprador

import br.com.cotecom.domain.usuarios.Responsabilidade
import br.com.cotecom.domain.usuarios.Supervisor
import br.com.cotecom.domain.usuarios.empresa.Atendimento
import br.com.cotecom.domain.usuarios.empresa.Cliente
import br.com.cotecom.domain.usuarios.empresa.Empresa
import br.com.cotecom.util.Path.Path
import br.com.cotecom.util.fixtures.CotacaoFixture
import br.com.cotecom.util.fixtures.EmpresaFixture
import br.com.cotecom.util.fixtures.UsuarioFixture
import grails.converters.JSON
import org.apache.log4j.Logger

import org.hibernate.classic.Session

import br.com.cotecom.util.documents.excel.ExcelFile

import br.com.cotecom.domain.usuarios.empresa.Endereco

import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.cotacao.ICotacao
import br.com.cotecom.domain.usuarios.empresa.Fornecedor
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.domain.usuarios.Usuario
import br.com.cotecom.services.CotacaoService
import br.com.analise.domain.Compra
import br.com.cotecom.domain.resposta.EstadoResposta
import grails.plugins.springsecurity.Secured
import com.amazonaws.services.simpledb.model.Item
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.ss.usermodel.Cell
import br.com.cotecom.domain.item.ItemResposta
import br.com.cotecom.domain.resposta.RespostaRespondendo
import br.com.analise.domain.Item
import br.com.cotecom.domain.pedido.Pedido
import java.text.DecimalFormat
import java.text.Normalizer

class TestController {

    def sessionFactory
    def cotacaoService
    def usuarioService
    def produtoService
    def mailService
    def grailsTemplateEngineService
    def notifierService
    def dataSource
    def springSecurityService
    def compraService
    def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
    private static final log = Logger.getLogger(TestController.class)

    static allowedMethods = [importeProdutosDeTeste:'GET',
            crieCotacaoRespondidaComQuatroRepresentantes : 'GET',
            crieRepresentantes : 'GET',
            envieEmailDeTeste: 'GET',
            crieBaseProdutosDeTeste: 'GET'
    ]

    def index = {
        render (view:"index")
    }

    def hql = {
        def session = sessionFactory.getCurrentSession()

        def query = session.createQuery("select representante.nome from Resposta as resposta inner join resposta.representante as representante  where resposta.id = 2786")
        def results = query.list()

        def representante = Resposta.executeQuery("select representante.nome from Resposta as resposta inner join resposta.representante as representante where resposta.id = 2786", [cache:true])?.get(0)

        render representante
    }

    def crieRepresentantes = {
        crieRepresentantesComFornecedor()
        flash.message = "Mock de representantes criado!"
        render view:"/text/index"
    }

    def crie10Representantes = {
        if(crieRepresentantes()){
            flash.message = "Mock de representantes criado!"
        } else {flash.message = "MoccrieRepresentantesk não criado!"}
        render view:"/text/index"
    }

    def crieCotacaoEnviadaParaQuatroRepresentantes = {
        Comprador comprador = Usuario.findByEmail("demo@cotecom.com.br") as Comprador
        adicioneAtendimentoAoComprador(comprador, crieRepresentantesComFornecedor())

        def cot = CotacaoFixture.crieCotacao(comprador)
        CotacaoFixture.crieItensCotacao(cot, 50).each {cot.addToItens(it)}
        def representantes = usuarioService.getRepresentantesByComprador(comprador)
        def reps
        if(representantes.size()>=3)
            reps = representantes.subList(0,3)
        cot.envie(reps)
        cot.comprador = comprador
        if(cot.save(flush:true)){
            flash.message = "Mock de cotação criado!"
            render view:"/test/index"
        }
//        def cotacaoAnalisada = CotacaoFixture.crieCotacaoAnalisada()
//        cotacaoAnalisada.comprador = usuarioService.getSessionUser()
//        cotacaoAnalisada.salve()

    }

    def preenchaRespostasCotacao = {
        def cotacao = Cotacao.get(params.id)
        if(Cotacao){
            CotacaoFixture.preencheRespostas(cotacao)
            flash.message = "Cotação respondida com sucesso!"
            render view:"/test/index"
        }else{
            flash.message = "Cotacao Não existente"
            render view:"/test/index"
        }
    }

    def importeProdutosDeTeste = {
        produtoService.importePlanilhaProdutos(new Path().getPathArquivosDeTeste()+File.separator +
                'lista_jc_2009.xls', Cliente.findByEmail("empresademo@kote.com.br"))
        flash.message = "Produtos importados"
        render view:"/test/index"
    }

    def retorneCotacao = {
        def a = Cotacao.executeQuery("""select cotacao from Cotacao as cotacao
            inner join cotacao.respostas as resposta where resposta.id = ${this.id}""").get(0)
        return a
    }

    def envieEmailDeTeste = {
        def a = null
        def render2 = grailsTemplateEngineService.renderView("/mail/novoPedido", [comprador: a])
        def mailId = sesMail {
            to "danilo@kote.com.br"
            subject "test"
            html render2
        }

        flash.message = mailId.toString()
        render view:"/test/index"
    }

    private Map crieRepresentantesComFornecedor() {
        def fornecedor1 = EmpresaFixture.crieFornecedorCompletoComEnderecoETelefone()
        def fornecedor2 = EmpresaFixture.crieFornecedorCompletoComEnderecoETelefone()
        def fornecedor3 = EmpresaFixture.crieFornecedorCompletoComEnderecoETelefone()
        def fornecedor4 = EmpresaFixture.crieFornecedorCompletoComEnderecoETelefone()
        fornecedor1.nomeFantasia = "Jc distribuição"
        fornecedor1.razaoSocial = "Jc distr. Log. Expt de prod. ind. S/A"
        fornecedor3.nomeFantasia = "Armazém Goiás"
        fornecedor3.razaoSocial = "Rede Brasil Distribuição e Logística Ltda"
        fornecedor4.nomeFantasia = "Lider distribuição"
        fornecedor4.razaoSocial = "Terra atacado distribuidor LTDA"
        fornecedor1.save(flush:true)
        fornecedor2.save(flush:true)
        fornecedor3.save(flush:true)
        fornecedor4.save(flush:true)
        Supervisor supervisor1 = UsuarioFixture.crieSupervisorSemEmpresa()
        supervisor1.nome = "Iure Gumimarães"
        supervisor1.save(flush:true)
        fornecedor1.addUsuario supervisor1
        Supervisor supervisor2 = UsuarioFixture.crieSupervisorSemEmpresa()
        supervisor2.nome = "Ricardo Gobbo"
        supervisor2.save(flush:true)
        fornecedor2.addUsuario supervisor2
        Supervisor supervisor3 = UsuarioFixture.crieSupervisorSemEmpresa()
        supervisor3.nome = "Alberto Paulo"
        supervisor3.save(flush:true)
        fornecedor3.addUsuario supervisor3
        Supervisor supervisor4 = UsuarioFixture.crieSupervisorSemEmpresa().save(flush:true)
        fornecedor4.addUsuario supervisor4

        def representante1 = UsuarioFixture.crieRepresentanteSemEmpresa().save(flush:true)
        def representante2 = UsuarioFixture.crieRepresentanteSemEmpresa()
        def representante3 = UsuarioFixture.crieRepresentanteSemEmpresa()
        def representante4 = UsuarioFixture.crieRepresentanteSemEmpresa()
        representante1.nome = "Cristiano Santos"
        representante1.save(flush:true)
        representante2.nome = "Maurício Oliveira"
        representante2.save(flush:true)
        representante3.nome = "João Paulo"
        representante3.save(flush:true)
        representante4.nome = "Fábio Freitas"
        representante4.save(flush:true)

        supervisor1.addRepresentante representante1
        supervisor1.addRepresentante representante1
        supervisor2.addRepresentante representante2
        supervisor3.addRepresentante representante3
        supervisor4.addRepresentante representante4

        return [representante1: representante1, representante2: representante2, representante3: representante3,
                representante4: representante4]
    }

    private Atendimento adicioneAtendimentoAoComprador(Comprador comprador, Map representantes) {
        Atendimento atendimento1 = new Atendimento(cliente: comprador.empresa, representante: representantes.representante1,
                fornecedor: representantes.representante1.empresa)
        atendimento1.save(flush:true)
        Atendimento atendimento2 = new Atendimento(cliente: comprador.empresa, representante: representantes.representante2,
                fornecedor: representantes.representante2.empresa)
        atendimento2.save(flush:true)
        Atendimento atendimento3 = new Atendimento(cliente: comprador.empresa, representante: representantes.representante3,
                fornecedor: representantes.representante3.empresa)
        atendimento3.save(flush:true)
        Atendimento atendimento4 = new Atendimento(cliente: comprador.empresa as Cliente,
                representante: representantes.representante4, fornecedor: representantes.representante4.empresa)
        atendimento4.save(flush:true)
    }

    /*def crieBaseProdutosDeTeste = {
        String sqlScript = new File(new Path().getPathArquivosDeTeste() + File.separator + "public_produto.sql" ).getText("UTF-8")
        def sql = Sql.newInstance(dataSource)
        List newSqlScriptAndNextId = createNewScript(sql, sqlScript)
        sql.execute(newSqlScriptAndNextId.getAt(0))
        sql.executeUpdate("update hibernate_sequences set next_val = ? where sequence_name = 'default' ", [newSqlScriptAndNextId.getAt(1)])
        Produto.reindex()
        flash.message = "Produtos importados"
        redirect (view: "test/index")
    }*/

    private List createNewScript(sql, String sqlScript) {
        String newScript = ""
        def nextId = sql.firstRow("select next_val from hibernate_sequences").next_val
        def firstNextId = nextId
        int lineNum = 1;

        sqlScript.eachLine {String line ->
            if (lineNum.mod(3) == 2) {
                line = line.replaceFirst(/VALUES \([0-9]*,/, "VALUES (${nextId},")
                nextId++
            }
            newScript += line + "\n"
            lineNum++
        }
        if(firstNextId != sql.firstRow("select next_val from hibernate_sequences").next_val)
            return createNewScript(sql, sqlScript)
        return [newScript, nextId]
    }

    def cleanUpGorm = {
        cleanUpGorm()
    }

    void cleanUpGorm() {
        Session session = sessionFactory.currentSession
        session.flush()
        session.clear()
        propertyInstanceMap.get().clear()
    }

    def insertProduto = {
        def produto = new Produto(barCode: "78912323", descricao: "teste asd", embalagem: new EmbalagemVenda(tipoEmbalagemDeVenda:
                new TipoEmbalagem(descricao: "cx"), tipoEmbalagemUnidade: new TipoEmbalagem(descricao: "un"),
                qtdeDeUnidadesNaEmbalagemDeVenda: 12))
        produto.save(flush:true)
        flash.message = "Produto criado: ${produto}"
        render view:"/test/index"
    }

    def testDownloadFile = {
        File file = new File(new Path().getPathArquivosDeTeste()+File.separator+"planilha_exemplo_importacao.xls")
        response.contentType = "application/octet-stream"
        response.setHeader("Content-disposition", "attachment; filename=" + file.name + ".xls");
        response.setHeader("Content-Length", "${file.size()}")
        response.outputStream << file.readBytes()
    }

    def crieCotacaoRascunhoAPartirDeExcel = {
        def downloadedfile = request.getFile('planilha');
        String caminhoPlanilha =  new Path().getPathArquivos() + File.separator + 'cotacao.xls'
        downloadedfile.transferTo(new File(caminhoPlanilha))
        Cliente empresa = Cliente.get(params.empresaId as Long)
        ExcelFile excelFile = new ExcelFile(caminhoPlanilha)
        def resultado = cotacaoService.crieCotacaRascunhoAPartirDeExcel(excelFile, empresa, 1)
        if(resultado?.get(0) instanceof Cotacao) {
            flash.message = "Cotação ${resultado?.get(0)?.id} criada com sucesso!\n , ${(resultado?.get(0) as Cotacao)?.itens?.size()} itens adicionados"
        } else {
            flash.messsage = "Erro ao criar cotação!"
        }
        render view:"/test/index"
    }

    def crieCotacaoAPartirDeExcelEEnvieA10Reps = {
        def downloadedfile = request.getFile('planilha');
        String caminhoPlanilha =  new Path().getPathArquivos() + File.separator + 'cotacao.xls'
        downloadedfile.transferTo(new File(caminhoPlanilha))
        ExcelFile excelFile = new ExcelFile(caminhoPlanilha)
        Comprador comprador = Comprador.get(params.compradorId as Long)
        def resultado = cotacaoService.crieCotacaRascunhoAPartirDeExcel(excelFile, comprador, 1)
        ICotacao cotacaoSalva = resultado?.get(0) as Cotacao

        List<Representante> representantes = crieRepresentantes()

        if(envieCotacao(cotacaoService, cotacaoSalva as Cotacao, representantes) instanceof Cotacao){
            flash.message = "Cotação criada e enviada com sucesso! \n ${resultado?.get(1)}"
        } else {
            flash.messsage = "Erro ao criar cotação!"
        }
        render view:"/test/index"
    }

    private Comprador crieCompradorLuizmar() {
        Endereco endereco = new Endereco(logradouro: "Rua senador moraes filho", numero: "292", bairro: "campinas", cidade: "Goiânia", estado: "GO", cep: "74515010").save(flush:true)
        Cliente cliente = Cliente.findByEmail("supjatoba@gmail.com")
        if (!cliente)
            cliente = new Cliente(nomeFantasia: "Supermercado e Agropecuária Jatobá", email: "supjatoba@gmail.com")
        cliente.addEndereco endereco
        cliente.save(flush:true)
        Comprador comprador1 = Comprador.findByEmail("luizmar@grupojatoba.com")
        if (!comprador1)
            comprador1 = new Comprador(empresa: cliente, nome: "Luizmar Theodoro", email: "luizmar@grupojatoba.com",
                    password: springSecurityService.encodePassword("123456"), habilitado: true).save(flush:true)
        comprador1.addResponsabilidade(Responsabilidade.findByResponsabilidade("ROLE_COMPRADOR"))
        comprador1.save(flush:true)
        return comprador1
    }

    private List<Representante> crieRepresentantes() {
        List<Representante> representantes = new ArrayList()
        Representante representante = Representante.findByNome("Emerson")
        if (!representante) {
            representantes.add(new Representante(nome: "Emerson", email: "emerson@empresaaaaa.com", password: "senhaTemp", empresa: new Fornecedor(nomeFantasia: "Rio vermelho", email: "riovermelhosadslksadlahdljahsd@rv.com").save(flush:true)).save(flush:true))
        } else {
            representantes.add(representante)
        }
        Representante representante1 = Representante.findByNome("Cristiano")
        if (!representante1) {
            representantes.add(new Representante(nome: "Cristiano", email: "cristiano_mega@empresaaaaa.com", password: "senhaTemp",
                    empresa: new Fornecedor(nomeFantasia: "Megafort", email: "megafortlksadlahdljahsd@mega.com").save(flush:true)).save(flush:true))
        } else {
            representantes.add(representante1)
        }
        Representante representante2 = Representante.findByNome("Gregório")
        if (!representante2) {
            representantes.add(new Representante(nome: "Gregório", email: "goias@empresaaaaa.com",password: "senhaTemp",
                    empresa: new Fornecedor(nomeFantasia: "Armazém Goiás", email: "armazemgoiaslksadl@real.com").save(flush:true)).save(flush:true))
        } else {
            representantes.add(representante2)
        }
        Representante representante3 = Representante.findByNome("Fernando Tomaz")
        if (!representante3) {
            representantes.add(new Representante(nome: "Fernando Tomaz", email: "realasds@empresaaaaa.com", password: "senhaTemp",
                    empresa: new Fornecedor(nomeFantasia: "Real distribuidora", email: "reallksadlahdljahsd@real.com").save(flush:true)).save(flush:true))
        } else {
            representantes.add(representante3)
        }
        Representante representante4 = Representante.findByNome("Euclides")
        if (!representante4) {
            representantes.add(new Representante(nome: "Euclides", email: "cedroER@empresaaaaa.com", password: "senhaTemp",
                    empresa: new Fornecedor(nomeFantasia: "Cedro distribuição", email: "cedroasdksadlahdljahsd@cedro.com").save(flush:true)).save(flush:true))
        } else {
            representantes.add(representante4)
        }
        Representante representante5 = Representante.findByNome("Manin")
        if (!representante5) {
            representantes.add(new Representante(nome: "Manin", email: "manimRT@empresaaaaa.com", password: "senhaTemp",
                    empresa: new Fornecedor(nomeFantasia: "Pérola distribuição", email: "perolalksadl@peroladsad.com").save(flush:true)).save(flush:true))
        } else {
            representantes.add(representante5)
        }
        Representante representante6 = Representante.findByNome("Lorival")
        if (!representante6) {
            representantes.add(new Representante(nome: "Lorival", email: "lorival_maristela@empresaaaaa.com",password: "senhaTemp",
                    empresa: new Fornecedor(nomeFantasia: "Maristela", email: "ibiaksadlahdljahsd@ibia.com").save(flush:true)).save(flush:true))
        } else {
            representantes.add(representante6)
        }
        Representante representante7 = Representante.findByNome("Cintia")
        if (!representante7) {
            representantes.add(new Representante(nome: "Cintia", email: "masterER@empresaaaaa.com", password: "senhaTemp",
                    empresa: new Fornecedor(nomeFantasia: "Master distribuição", email: "masterksadlah@master.com").save(flush:true)).save(flush:true))
        } else {
            representantes.add(representante7)
        }
        Representante representante8 = Representante.findByNome("Emerson Gynsol")
        if (!representante8) {
            representantes.add(new Representante(nome: "Emerson Gynsol", email: "gynsolE@empresaaaaa.com", password: "senhaTemp",
                    empresa: new Fornecedor(nomeFantasia: "Gynsol", email: "gynsol@gynsolmasdas.com.ca").save(flush:true)).save(flush:true))
        } else {
            representantes.add(representante8)
        }
        Representante representante9 = Representante.findByNome("Rafael Jc")
        if (!representante9) {
            representantes.add(new Representante(nome: "Rafael Jc", email: "RafaelJC@empresaaaaa.com", password: "senhaTemp",
                    empresa: new Fornecedor(nomeFantasia: "JC distribuição", email: "JCksadsd@jcdistribuicao.com").save(flush:true)).save(flush:true))
        } else {
            representantes.add(representante9)
        }
        return representantes
    }

    private def envieCotacao(CotacaoService cotacaoService, Cotacao cotacaoSalva, List<Representante> representantes) {
        cotacaoService.envieCotacao(cotacaoSalva, representantes)
    }

    def loadCompradorApp = {
        redirect(controller:'app', action: 'comprador')
    }

    def loadRepresentanteApp = {
        if(params.digest){
            redirect(controller:'app', action: 'representante', params:[q: params.digest])
        }else if(params.respostaId){
            String digest = Resposta.read(params.respostaId).respostaUrlDigest
            redirect(controller:'app', action: 'representante', params:[q: digest])
        }else{
            flash.message = "Pedido não encontrado"
            render view:"/test/index"
        }
    }

    def enviePedidoARepresentante ={
        if(params.pedidoId){
            redirect(controller: 'pedido', action:
                    'exportePedidoExcel', params: [id: params.pedidoId])
        }else{
            flash.message = "Pedido não encontrado"
            render view:"/test/index"
        }

    }

    def testGselect = {
        def avariavel = params
        print avariavel
    }

    def testeFind  = {
        def p = Produto.findByDescricao("REFRESCO PRO MIX FAZ 10LT SAL FRUTA 400GR")
        print(p)
    }


    def analiseCompra = {
        Compra compra = compraService.getCompra("4fcfc7ba84aeba1ec0f80531")
        compra.itens?.each {println(it.id)}
        render 'ok'
    }

    @Secured(['ROLE_ADMIN'])
    def respondaCotacao = {
        def cotacao = Cotacao.get(params.cotacaoId)
        cotacao.respostas.each {def resposta->
            if(resposta.codigoEstado == EstadoResposta.NOVA_COTACAO){
                resposta.aceite()
                resposta.itens.each {def item->
                    item.preco.embalagem = new Random().nextFloat()*10
                }
                resposta.save()
            }
            if(resposta.codigoEstado == EstadoResposta.RESPONDENDO)
                resposta.envie()
        }
    }

    /**
     * analise de retorno de investimento
     */

    def crieCotacaoAPartirDeAnaliseExcelEnvieAosRepresentantes = {
        // 1 criar cotação com produtos e quantidades da cotação
        // 2 enviar aos representante pelos nomes de cabeçalho
        def downloadedfile = request.getFile('planilha');
        String caminhoPlanilha =  new Path().getPathArquivos() + File.separator + 'analise.xls'
        downloadedfile.transferTo(new File(caminhoPlanilha))
        Cliente empresa = Cliente.get(params.empresaId as Long)
        ExcelFile excelFile = new ExcelFile(caminhoPlanilha)
        int linha = 0
        while(excelFile.getCellValue(0,0,linha) != "Qtd"){
            linha ++
        }
        linha++
        def resultado = cotacaoService.crieCotacaRascunhoAPartirDeExcel(excelFile, empresa, linha)
        if(resultado?.get(0) instanceof Cotacao) {
            envieCotacaoAosRepresentantesDaPlanilha(resultado?.get(0)?.id, caminhoPlanilha)
        }

        render view:"/test/index"
    }

    def envieCotacaoAosRepresentantesDaPlanilha(Long idCotacao, String caminhoPlanilha) {
        ExcelFile excelFile = new ExcelFile(caminhoPlanilha)
        int linha = 8
        def representantes = []
        while(excelFile.getCellValue(0,0,linha) != "Melhor Preco"){
            def rep = excelFile.getCellValue(0, 0, linha).split("/")?.getAt(0) as String
            rep = Normalizer.normalize(rep, Normalizer.Form.NFD);
            rep = rep.replaceAll("[^\\p{ASCII}]", "");
            def representante = Representante.findByNome(rep)
            if (!representante){
                representante = new Representante(nome: rep, email: rep.replaceAll(" ", "")+"@kote.com.br",
                        password: springSecurityService.encodePassword("dmrdmr")).save(flush: true)
                representante.addResponsabilidade(Responsabilidade.findByResponsabilidade("ROLE_REPRESENTANTE"))
                if(representante.hasErrors()){
                    print representante.errors
                }else{
                    representantes.add(representante)
                }
            }else{
                representantes.add(representante)
            }
            linha ++
        }
        def cotacao = Cotacao.get(idCotacao)
        cotacao.envie(representantes)
    }

    def respondaCotacaoAPartirDaAnalise = {
        def cotacaoId = params.cotacaoId
        def downloadedfile = request.getFile('planilha');
        String caminhoPlanilha =  new Path().getPathArquivos() + File.separator + 'cotacao.xls'
        downloadedfile.transferTo(new File(caminhoPlanilha))
        respondaCotacaoComRespostasDoExcelDeAnalise(cotacaoId, caminhoPlanilha)
    }

    def respondaCotacaoComRespostasDoExcelDeAnalise(def idCotacao, String caminhoPlanilha) {
        // 1 para cara coluna de preço encontrar o representante correspondente e responder todos
        def cotacao = Cotacao.get(idCotacao)
        cotacao.respostas.each {
            importeRespostasDePlanilhaAnalise(caminhoPlanilha, it)
        }
        // 2 enviar a resposta
    }

    def importeRespostasDePlanilhaAnalise(def caminhoPlanilha, Resposta resposta){
        ExcelFile  excelFile = new ExcelFile(caminhoPlanilha)
        def numRespostas = resposta.cotacao.respostas.size()
        def linhasDeCabecalho = 10
        Cell cellRepresentante = excelFile.busqueCelula(0,3..(3+numRespostas),(linhasDeCabecalho +numRespostas)..(linhasDeCabecalho+numRespostas),resposta.representante.nome)?.getAt(0)
        def indiceColuna = cellRepresentante.getColumnIndex()
        def numPrimeiroProduto = linhasDeCabecalho + 1 + numRespostas
        if(resposta.codigoEstado == EstadoResposta.NOVA_COTACAO)
            resposta.aceite()
        if (resposta.estado instanceof RespostaRespondendo){
            resposta.itens.each {
                def descricao = it.itemCotacao.produto.descricao.replaceAll(/\s+/, " ").replaceAll("\\u00a0"," ")
                def linha = excelFile.busqueCelula(0,1..1,numPrimeiroProduto..numPrimeiroProduto+resposta.itens.size(),
                        descricao)?.getAt(0)?.getRow()?.rowNum
                def preco
                if(linha == null){
                    print(descricao + "\n")
                }else{
                    preco = excelFile.getCellValue(0,indiceColuna, linha)
                }
                if (preco!= null && preco != "-"){
                    it.preco.embalagem = preco as Double
                    it.save()
                }
            }
            resposta.save()
            resposta.envie()
        }
    }

    def analiseTodosCasosDeCompras = {

    }

    def calculeEconomiaMediaDaCotacao = {
        def cotacaoId = params.idCotacao
        if(!cotacaoId){
            render "Cotação não encontrada"
            return
        }
        def compra = compraService.getCompraByCotacaoId(cotacaoId as int)
        def totalEconomia = 0
        def percentualEconomia = 0
        def numItensParaMedia = 0
        compra.itens.each {Item item->
            if(item?.respostas?.size()>1 && !item?.naoComprar ){
                def segundoPreco = item.respostas.get(1).preco
                def primeiroPreco = item.respostas.get(0).preco
                totalEconomia += (segundoPreco - primeiroPreco)*item.quantidade
                percentualEconomia += (segundoPreco/primeiroPreco)*100 -100
                numItensParaMedia ++
            }
        }

        def mediaEconomia = totalEconomia/numItensParaMedia
        percentualEconomia = percentualEconomia/numItensParaMedia

       render "Economia total: $totalEconomia <br/> media de economia: $mediaEconomia <br/> percentual economia: $percentualEconomia"
    }

    def calculeEcomomiaAoAdicionarReps = {
        def cotacaoId = params.idCotacao as int
        if(!cotacaoId){
            render "Cotação não encontrada"
            return
        }
        def compra = compraService.getCompraByCotacaoId(cotacaoId)
        List qtdDeProdutosComXRespostas
        int totalEconomia
        BigDecimal percentualEconomia
        int totalCompra
        BigDecimal mediaEconomia
        BigDecimal economiaPercentualTotal
        (totalEconomia, mediaEconomia, percentualEconomia, totalCompra, economiaPercentualTotal, qtdDeProdutosComXRespostas) = calculeEconomia(compra)

        def result = """Economia total: R\$ $totalEconomia <br/> media de economia:R\$ $mediaEconomia <br/>
                    percentual economia por item: $percentualEconomia <br/> total da compra:R\$ $totalCompra<br/>
                    percentual de economia global: $economiaPercentualTotal % <br/><br/>"""
        qtdDeProdutosComXRespostas.eachWithIndex {def item, def index->
            result = result +  "$item produtos com $index opções de compra, ${((item == 0 ? 1 : item)/compra.itens.size())*100}% da compra<br/>"
        }
        render result
    }

    private List calculeEconomia(Compra compra) {
        def respostaCompra = compra.respostasCompra.get(0)
        respostaCompra.emailRepresentante
        def produtosComApenasUmaEmpresa = 0
        List qtdDeProdutosComXRespostas = []
        def totalEconomia = 0
        def percentualEconomia = 0
        def numItensParaMedia = 0
        def totalCompra = 0
        def mediaEconomia = 0
        def economiaPercentualTotal = 0

        (compra.respostasCompra.size()+1).times {
            qtdDeProdutosComXRespostas.add(0)
        }
        compra.itens.each {
            if (it?.respostas?.size() > 1 && !it?.naoComprar) {
                def segundoPreco = it.respostas.get(1).preco
                def primeiroPreco = it.respostas.get(0).preco
                totalEconomia += (segundoPreco - primeiroPreco) * it.quantidade
                percentualEconomia += (segundoPreco / primeiroPreco) * 100 - 100
                numItensParaMedia++
            }

            if (it.respostas == null || it.respostas.size() == 0) {
                qtdDeProdutosComXRespostas.putAt(0, qtdDeProdutosComXRespostas.get(0) + 1)
            } else {
                totalCompra += it.respostas.get(0).preco * it.quantidade
                if (it.respostas.size()>=9)
                    print it.respostas
                qtdDeProdutosComXRespostas.putAt(it.respostas.size(), qtdDeProdutosComXRespostas.get(it.respostas.size()) + 1)
            }
        }

        if(numItensParaMedia > 1){
            mediaEconomia = totalEconomia / numItensParaMedia
            percentualEconomia = percentualEconomia / numItensParaMedia
        }
        economiaPercentualTotal = (totalEconomia / totalCompra) * 100
        [totalEconomia, mediaEconomia, percentualEconomia, totalCompra, economiaPercentualTotal, qtdDeProdutosComXRespostas]
    }

    def calculeFaltasEEconomiaAoAdicionarFornecedores = {
        def result = "Cálculo feito dando prioridade aos fornecedores com maior mix de produtos:"
        def cotacaoId = params.idCotacao as int
        if(!cotacaoId){
            render "Cotação não encontrada"
            return
        }
        def compra = compraService.getCompraByCotacaoId(cotacaoId)
        def mixProdutos = [:]
        compra.respostasCompra.each {
            mixProdutos.put(it.idResposta, 0)
        }
        compra.itens.each {
            it.respostas.each {
                if(it.preco != null  && it.preco > 0)
                    mixProdutos.put(it.respostaCompra.idResposta, mixProdutos.get(it.respostaCompra.idResposta)+1)
            }
        }
        //todo: para cada resposta da lista mix produtos, fazer calculos e remove-lo, fazer novamente, até restar apenas o rep com maior mix

        def iterations = mixProdutos.size()

        iterations.times {
            def faltas = 0
            def totalCompra = 0
            /*compra.itens.each {
                if(it.respostas == null || it.respostas.size()<=0){
                    faltas ++
                }else{
                    totalCompra += it.respostas.get(0).preco*it.quantidade
                }
            }*/

            def totalEconomia
            def mediaEconomia
            def percentualEconomia
            def economiaPercentualTotal
            def qtdDeProdutosComXRespostas
            (totalEconomia, mediaEconomia, percentualEconomia, totalCompra, economiaPercentualTotal, qtdDeProdutosComXRespostas) = calculeEconomia(compra)
            result += "<br/> Total da compra com ${mixProdutos.size()} fornecedores: R\$ ${new DecimalFormat("#.##").format(totalCompra)}"
            faltas = qtdDeProdutosComXRespostas.get(0)
            result+= "<br/> Total de faltas ${faltas}"
            result += "<br/> Total Economia: R\$ $totalEconomia"
            result += "<br/> Média de Economia: R\$ $mediaEconomia"
            result += "<br/> Percentual de Economia: $percentualEconomia %"
            result += "<br/> Percentual de Economia global: $economiaPercentualTotal %"
            result += "<br/> Total Compra: $totalEconomia"
            def idRespostaMinMixProdutos = mixProdutos.min {it.value}.key
            compra.itens.each {def item ->
                item.respostas?.removeAll {
                    it.respostaCompra.idResposta == idRespostaMinMixProdutos
                }
            }
            def find = compra.respostasCompra.find {def respComp-> respComp.idResposta == idRespostaMinMixProdutos}
            result += "<br/> <b> removendo empresa ${find.nomeFantasiaEmpresa}</b>"
            compra.respostasCompra.removeAll{it.idResposta == idRespostaMinMixProdutos}
            mixProdutos.remove(idRespostaMinMixProdutos)
        }
        render result
    }
    def limparDadosDemoUser = {
        def user = Usuario.findByEmail("demo@kote.com.br")
        def cliente = Cliente.findByComprador(user)
        List<Cotacao> cotacoes = Cotacao.findAllByEmpresa(cliente)
        List<Resposta> respostas = []
        cotacoes.each{
            it.respostas.each {
                respostas<<it
            }
        }
        List<Pedido> pedidos = []
        respostas.each{
            it.pedidos.each {
                pedidos << it
            }
        }
        pedidos.each {
            it.resposta.removeFromPedidos(it)
            it.delete(flush:true)
            it.errors.allErrors.each {print(it)}
        }
        respostas.each {
            it.cotacao.removeFromRespostas(it)
            it.delete(flush: true)
            it.errors.allErrors.each {print(it)}
        }
        cotacoes.each {
            if(it.compraId && !it.compraId.isEmpty())
                compraService.deleteCompra(it.compraId)
            it.delete(flush: true)
            it.errors.allErrors.each {print(it)}
        }





    }
}

