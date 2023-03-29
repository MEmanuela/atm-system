package org.internship.jpaonlinebanking.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

//@Configuration
public class MultipleDataSourceAppConfig {
//    @Bean(name = "primaryDataSource")
//    @ConfigurationProperties(prefix = "datasource.primary")
//    public DataSource primaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//    @Bean
//    @ConfigurationProperties(prefix = "datasource.primary.liquibase")
//    public LiquibaseProperties primaryLiquibaseProperties() {
//        return new LiquibaseProperties();
//    }
//    @Bean("liquibase")
//    public SpringLiquibase primaryLiquibase() {
//        return springLiquibase(primaryDataSource(), primaryLiquibaseProperties());
//    }
//    @Bean(name = "secondaryDataSource")
//    @Primary
//    @ConfigurationProperties(prefix = "datasource.secondary")
//    public DataSource secondaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//    @Bean
//    @ConfigurationProperties(prefix = "datasource.secondary.liquibase")
//    public LiquibaseProperties secondaryLiquibaseProperties() {
//        return new LiquibaseProperties();
//    }
//    @Bean
//    public SpringLiquibase secondaryLiquibase() {
//        return springLiquibase(secondaryDataSource(), secondaryLiquibaseProperties());
//    }
//    private static SpringLiquibase springLiquibase(DataSource dataSource, LiquibaseProperties properties) {
//        SpringLiquibase liquibase = new SpringLiquibase();
//        liquibase.setDataSource(dataSource);
//        liquibase.setChangeLog(properties.getChangeLog());
//        liquibase.setContexts(properties.getContexts());
//        liquibase.setDefaultSchema(properties.getDefaultSchema());
//        liquibase.setDropFirst(properties.isDropFirst());
//        liquibase.setShouldRun(properties.isEnabled());
//        liquibase.setChangeLogParameters(properties.getParameters());
//        liquibase.setRollbackFile(properties.getRollbackFile());
//        return liquibase;
//    }
}
