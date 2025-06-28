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
@EnableJpaRepositories(
        basePackages = "org.example.productcatalogservice.repository",
        entityManagerFactoryRef = "productCatalogEntityManagerFactory",
        transactionManagerRef = "productCatalogTransactionManager"
)
public class ProductCatalogConfig {

    @Primary
    @Bean(name = "productCatalogDataSource")
    @ConfigurationProperties("spring.datasource")
    public DataSource productCatalogDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "productCatalogEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean productCatalogEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("productCatalogDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("org.example.productcatalogservice.model")
                .persistenceUnit("productcatalog")
                .build();
    }

    @Primary
    @Bean(name = "productCatalogTransactionManager")
    public PlatformTransactionManager productCatalogTransactionManager(
            @Qualifier("productCatalogEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}

