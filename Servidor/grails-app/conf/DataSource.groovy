 dataSource {
    pooled = true
    driverClassName = "org.postgresql.Driver"
    dialect = org.hibernate.dialect.PostgreSQLDialect
    username = "postgres"
    password = "pass2pass"
 }

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}

// environment specific settings
environments {
    development {
        dataSource {
            username = "postgres"
            password = "pass2pass"
            dbCreate = "update"
            url="jdbc:postgresql://localhost:5432/kote_app"
            loggingSql = false
        }
    }

    test {
        dataSource {
            dbCreate = "create-drop"
            url="jdbc:postgresql://localhost:5432/cotecom_test"
            loggingSql = false
        }
    }

    production {
        dataSource {
            username = "portal"
            password = "yZ56exC7gBmK"
            dbCreate = "update"
            url="jdbc:postgresql://localhost:5432/kote_app"
            loggingSql = false
        }
    }
}



