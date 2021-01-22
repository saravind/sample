package com.bookstore.catalog.dao.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JdbcConfigurationConfig {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
