package com.waiyanhtet.jdbcTempate.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

@Configuration
@PropertySources(
		value = {
		@PropertySource("database.properties"),
		@PropertySource("sql.properties")})
@ComponentScan(basePackages = "com.waiyanhtet.jdbcTemplate.dao")
public class AppConfig {

	@Value("${db.url}")
	private String url;
	
	@Value("${db.user}")
	private String user;
	
	@Value("${db.password}")
	private String password;
	
	@Bean
	DataSource dataSource() {
		var config = new BoneCPConfig();
		config.setJdbcUrl(url);
		config.setUser(user);
		config.setPassword(password);
		return new BoneCPDataSource(config);
	}
	
	@Bean
	JdbcTemplate template() {
		return new JdbcTemplate(dataSource(), true);
	}
	
}
