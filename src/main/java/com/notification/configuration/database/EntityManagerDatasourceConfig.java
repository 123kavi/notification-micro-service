package com.notification.configuration.database;

import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@RequiredArgsConstructor
@Configuration
@EnableJpaRepositories(basePackages = "com.cloudofgoods.notification.repository")
public class EntityManagerDatasourceConfig {

    private final DataSource dataSource;

    /**
     * Defines project specific entity manager configurations for Hibernate including table auto-generation
     * and overriding & replace audit table suffix etc.
     * @return javax.persistence.EntityManagerFactory
     */
    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceProvider(new HibernatePersistenceProvider());
        em.setPackagesToScan("com.cloudofgoods.notification.entity");
        em.setDataSource(dataSource);

        Properties properties = new Properties();
        properties.setProperty("org.hibernate.envers.audit_table_suffix", "_hst"); // Overrides Audit table suffix of "_AUD" to "_hst"
//        properties.setProperty("hibernate.hbm2ddl.auto", "update"); // Note:- Uncomment only when needed to manually update table structure for development purpose
        em.setJpaProperties(properties);
        em.afterPropertiesSet();

        return em.getObject();
    }
}