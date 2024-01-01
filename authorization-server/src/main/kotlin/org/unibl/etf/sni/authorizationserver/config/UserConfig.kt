package org.unibl.etf.sni.authorizationserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.provisioning.JdbcUserDetailsManager
import javax.sql.DataSource

@Configuration
class UserConfig {

    @Bean
    fun jdbcUserDetailsManager(dataSource: DataSource) = JdbcUserDetailsManager(dataSource)

}
