package com.waiyanhtet.jdbcTemplate.dao.config;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;

@Configuration
public class FactoryConfig {

	@Bean
	@Qualifier("memberInsert")
	PreparedStatementCreatorFactory insertFactory(@Value("${member.insert}") String sql) {
		return new PreparedStatementCreatorFactory(sql, new int[] {
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
		});
	}
	
	@Bean
	@Qualifier("memberFindByUsernameLike")
	PreparedStatementCreatorFactory selectFactoryByName(@Value("${member.select.by.name}") String sql) {
		return new PreparedStatementCreatorFactory(sql, new int[] {
				Types.VARCHAR
		});
	}
	
	@Bean
	@Qualifier("memberFindByPK")
	PreparedStatementCreatorFactory selectFactoryByPK(@Value("${member.select.by.pk}") String sql) {
		return new PreparedStatementCreatorFactory(sql, new int[] {
				Types.VARCHAR
		});
	}
}
