package com.bookstore.catalog.module;

import com.bookstore.catalog.dao.BookDao;
import com.bookstore.catalog.dao.config.JdbcConfigurationConfig;
import com.bookstore.catalog.dao.impl.DefaultBookStoreDaoImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

public class JdbcModule extends AbstractModule {

    private JdbcConfigurationConfig jdbcConfiguration;

    public JdbcModule(JdbcConfigurationConfig configurationConfig) {
        this.jdbcConfiguration = configurationConfig;
    }

    @Override
    protected void configure() {
        bind(BookDao.class).to(DefaultBookStoreDaoImpl.class);
    }

    @Provides
    @Singleton
    public DataSource providesDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(jdbcConfiguration.getUrl(),
                jdbcConfiguration.getUsername(), jdbcConfiguration.getPassword());
        dataSource.setDriverClassName(jdbcConfiguration.getDriverClassName());
        return dataSource;
    }

    @Provides
    @Singleton
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
