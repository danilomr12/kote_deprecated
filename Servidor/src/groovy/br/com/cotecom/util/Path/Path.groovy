package br.com.cotecom.util.Path

import org.apache.log4j.Logger
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.springframework.context.ApplicationContext

public class Path{
    static final log = Logger.getLogger(Path.class)
    private ApplicationContext applicationContext = ApplicationHolder.application.mainContext

    public String getPathArquivos(){
        return applicationContext.getResource("arquivos").getFile().toString()
    }

    public String getPathImages(){
        return applicationContext.getResource("images").getFile().toString()
    }

    public String getPathArquivosDeTeste(){
        return applicationContext.getResource("arquivos").getFile().toString()+ File.separator + "test"
    }

    public String getPathArquivosExcelAnalises(){
        return applicationContext.getResource("arquivos").getFile().toString()+ File.separator + "excel" +
                File.separator + "analise"

    }

    public String getPathArquivosExcelPedidos(){
        return applicationContext.getResource("arquivos").getFile().toString()+ File.separator + "excel" +
                File.separator + "pedido"

    }

    public String getPathArquivosExcelRespostas() {
        return applicationContext.getResource("arquivos").getFile().toString()+ File.separator + "excel" +
                File.separator + "resposta"
    }

    public String getPathFileConvertionTemp(){
        return applicationContext.getResource("arquivos").getFile().toString() + File.separator + "fileConvertionTemp"+File.separator
    }
}