package org.example.productcatalogservice.configs.dbconfigs;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "org.example.productcatalogservice.TableInheritanceExample")
public class TableInheritanceExamplesConfig {

    @Bean(name = "inheritanceDataSource")
    @ConfigurationProperties("spring.inheritance-datasource")
    public DataSource inheritanceDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "inheritanceEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean inheritanceEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("inheritanceDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("org.example.productcatalogservice.TableInheritanceExample")
                .persistenceUnit("inheritance")
                .build();
    }

    @Bean(name = "inheritanceTransactionManager")
    public PlatformTransactionManager inheritanceTransactionManager(
            @Qualifier("inheritanceEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
