package com.bzw.api;

import com.bzw.common.db.DataSourceChangeAdvice;
import com.bzw.common.db.DataSourceSwitcher;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class DBConfig {

    @Value("${spring.dataSource.type}")
    private Class<? extends DataSource> dataSourceType;
    /**
     * 写库 数据源配置
     * @return
     */
    @Bean(name = "dataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.dataSource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    /**
     * 有多少个从库就要配置多少个
     * @return
     */
    @Bean(name = "salve1")
    @ConfigurationProperties(prefix = "spring.dataSource.slave1")
    public DataSource slaveDataSource1() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Bean(name = "slave2")
    @ConfigurationProperties(prefix = "spring.dataSource.slave2")
    public DataSource slaveDataSource2() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Bean(name="dataSourceAdvice")
    public DataSourceChangeAdvice dataSourceChangeAdvice(){
        List<String> slaves = Lists.newArrayList();
        slaves.add("slave1");
        slaves.add("slave2");
        return new DataSourceChangeAdvice(new DataSourceSwitcher(slaves));
    }
}
