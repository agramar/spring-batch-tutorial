package kr.co.agramar.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Lazy
@Configuration
public class DataBaseConfig {

    @Primary
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource createMasterDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource createSlaveDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

//    public DataSource createMasterDataSource() {
//        return DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .url(null)
//                .driverClassName(null)
//                .password(null)
//                .username(null)
//                .build();
//    }
//
//    public DataSource createSlaveDataSource() {
//        return DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .url(null)
//                .driverClassName(null)
//                .password(null)
//                .username(null)
//                .build();
//    }

    @Bean
    public DataSource routingDataSource() {
        ReplicationRoutingDataSource replicationRoutingDataSource = new ReplicationRoutingDataSource();

        DataSource masterDataSource = createMasterDataSource();

        Map<Object, Object> dataSourceMap = new LinkedHashMap<>();
        dataSourceMap.put("master", createMasterDataSource());
        dataSourceMap.put("slave", createSlaveDataSource());

        replicationRoutingDataSource.setTargetDataSources(dataSourceMap);
        replicationRoutingDataSource.setDefaultTargetDataSource(masterDataSource);

        return replicationRoutingDataSource;
    }

    @Bean
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan("kr.co.agramar");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory);
        return tm;
    }
}