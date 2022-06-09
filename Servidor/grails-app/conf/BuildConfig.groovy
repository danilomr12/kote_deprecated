/*

grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        runtime 'postgresql:postgresql:9.1-901.jdbc4'
        // runtime 'mysql:mysql-connector-java:5.1.13'
    }
}
*/

//Para gerar war


grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
        // resolver conflito de versão 1.58 com 1.6
        excludes "slf4j-api:1.5.8", "slf4j-log4j12:1.5.8", "jul-to-slf4j:1.5.8", "jcl-over-slf4j:1.5.8"
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        "https://s3.amazonaws.com/grails-plugins/all/"
        // uncomment the below to enable remote dependency resolution
        //grailsRepo "http://grails.org/plugins"
        //grailsRepo "http://svn.codehaus.org/grails-plugins/"

        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        def slf4jVersion = "1.6"
        compile "org.slf4j:slf4j-api:$slf4jVersion"
        runtime "org.slf4j:slf4j-log4j12:$slf4jVersion", "org.slf4j:jul-to-slf4j:$slf4jVersion", "org.slf4j:jcl-over-slf4j:$slf4jVersion"

        runtime 'postgresql:postgresql:9.1-901.jdbc4'
        // runtime 'mysql:mysql-connector-java:5.1.13'
        // resolver conflito de versão 1.58 com 1.6
        //compile 'javax.mail:mail:1.4.4'
    }
    plugins{
        compile(":spring-security-core:1.2.7.3")
    }
}

/*
grails.war.copyToWebApp = { args ->
    fileset(dir:"web-app") {
        include(name: "arquivos/excel/resposta")
        include(name: "arquivos/excel/pedido")
        include(name: "arquivos/excel/analise")
        include(name: "arquivos/fileConvertionTemp")
        include(name: "arquivos/cotacao.xls")
        include(name: "arquivos/listaProdutos.xls")
    }
}*/
