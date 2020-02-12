package com.bookmanage.bookmanage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
  @ConfigurationProperties(prefix = "spring.datasource")
  @Bean
  public DataSourceConfig druid() {
    return new DataSourceConfig();
  }
}
