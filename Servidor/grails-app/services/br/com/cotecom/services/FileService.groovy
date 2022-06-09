package br.com.cotecom.services

import org.apache.log4j.Logger
import org.artofsolving.jodconverter.office.OfficeManager
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration
import org.artofsolving.jodconverter.OfficeDocumentConverter
import br.com.cotecom.util.Path.Path
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.springframework.context.ApplicationContext
import grails.util.GrailsUtil
import org.codehaus.groovy.grails.commons.GrailsApplication
import java.util.concurrent.ExecutionException
import org.artofsolving.jodconverter.office.OfficeException

class FileService {

    private static final log = Logger.getLogger(FileService.class)
    boolean transactional = true

    private ApplicationContext applicationContext = ApplicationHolder.application.mainContext

    private final OfficeManager officeManager;
    private final OfficeDocumentConverter documentConverter;

    public static final int PARAMETER_OFFICE_PORT = 8100;

    public static final String PARAMETER_OFFICE_HOME_LIBREOFFICE_UBUNTU = "/opt/openoffice.org3";
    public static final String PARAMETER_OFFICE_HOME_LIBREOFFICE_UBUNTU_DESKTOP11 = "/usr/lib/libreoffice";
    public static final String PARAMETER_OFFICE_HOME_LIBREOFFICE_WINDOWS = "C:" + File.separator + "Program Files (x86)" +
            File.separator + "OpenOffice.org 3"//null;
    public String PARAMETER_OFFICE_HOME_LIBREOFFICE = ""
    public static final String PARAMETER_OFFICE_PROFILE = null//"/home/converter/.openoffice.org/3";
    public static final int PARAMETER_FILEUPLOAD_FILE_SIZE_MAX = 5242880//5MB

    FileService(){
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration()

        if(PARAMETER_OFFICE_PORT)
            configuration.setPortNumber(PARAMETER_OFFICE_PORT)
        //caso seu ambiente de desenvolvimento seja windows ou mac ou sua instalação do openoffice seja outra,
        // direcionar para sua pasta de instalação
        //configuration.setOfficeHome(new File(PARAMETER_OFFICE_HOME_LIBREOFFICE_UBUNTU))
        if(GrailsUtil.environment.equals(GrailsApplication.ENV_PRODUCTION)){
            PARAMETER_OFFICE_HOME_LIBREOFFICE = PARAMETER_OFFICE_HOME_LIBREOFFICE_UBUNTU
        }else{
            PARAMETER_OFFICE_HOME_LIBREOFFICE = PARAMETER_OFFICE_HOME_LIBREOFFICE_UBUNTU_DESKTOP11
        }
        configuration.setOfficeHome(new File(PARAMETER_OFFICE_HOME_LIBREOFFICE))
        if(PARAMETER_OFFICE_PROFILE)
            configuration.setTemplateProfileDir(new File(PARAMETER_OFFICE_PROFILE))
        configuration.setMaxTasksPerProcess(50)
        officeManager = configuration.buildOfficeManager()
        documentConverter = new OfficeDocumentConverter(officeManager)
    }

    public File graveArquivo(byte[] bytes, String completePathArquivo) {
        String caminhoPlanilha = completePathArquivo
        File arquivo = new File(caminhoPlanilha);
        FileOutputStream fileOutputStream = new FileOutputStream(arquivo);
        try{
            fileOutputStream.write(bytes);
        } catch (IOException x){
            log.info "Erro ao gravar arquivo ${x.toString()}"
        }finally {
            if(fileOutputStream!=null){
                fileOutputStream.flush()
                fileOutputStream.close()
            }
        }
        return arquivo
    }

    def leiaArquivo(String caminhoCompleto) throws Exception{
        File file = new File(caminhoCompleto);
        return file;
    }

    boolean convertFile(String fileToConvertFullPath, String fileExtensionWithoutDot){
        if(fileToConvertFullPath == null || fileExtensionWithoutDot == null)
            return null
        boolean fileMoved = false
        String tempFilePath = new Path().pathFileConvertionTemp + "tempFile" + new Date().time + "." + fileExtensionWithoutDot
        try{
            documentConverter.convert(new File(fileToConvertFullPath), new File(tempFilePath));
        }catch (Throwable throwable){
            log.error(throwable.toString())
        }finally{
            fileMoved = copyAndOverwriteFile(fileToConvertFullPath, tempFilePath);
            return fileMoved
        }
    }

    private boolean copyAndOverwriteFile(String fileToBeOverwritedFullPath, String fileToOverwriteFullPath) {
        boolean fileMoved
        File fileToBeOverwrited = new File(fileToBeOverwritedFullPath)
        File dir = new File(fileToBeOverwrited.parent);
        String sourceFileName = fileToBeOverwrited.getName()
        fileToBeOverwrited.delete()
        File fileToOverwrite = new File(fileToOverwriteFullPath);
        fileMoved = fileToOverwrite.renameTo(new File(dir, sourceFileName))
        return fileMoved
    }

    void startOpenOfficeProcess(){
        try{
            officeManager.start()
        }catch (OfficeException e){
            log.debug "process already running! ${e.getMessage() +e.getCause()}"
        }catch(Exception e){
            e.printStackTrace()
            log.debug("Directory of officeHome is set to ${PARAMETER_OFFICE_HOME_LIBREOFFICE}. Verify if it's correct!")
        }
    }

    void stopOpenOfficeProcess(){
        officeManager.stop()
    }
}
