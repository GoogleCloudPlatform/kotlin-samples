package kotlinbars

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


@Component
class TestDb(val databaseClient: DatabaseClient, @Value("classpath:init.sql") val initSql: Resource) {
    @PostConstruct
    fun init() {
        InitConfiguration.initDb(databaseClient, initSql)
    }
}
