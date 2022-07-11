package com.waiyanhtet.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.waiyanhtet.jdbcTempate.config.AppConfig;
import com.waiyanhtet.jdbcTemplate.dto.Member;

@SpringJUnitConfig(classes = AppConfig.class)
@TestMethodOrder(OrderAnnotation.class)
public class JdbcOperationTestWithSimpleQueryAndParams {

	@Autowired
	private JdbcOperations operations;
	
	@Autowired
	private RowMapper<Member> rowMapper;
	
	@Test
	@DisplayName("Update with PrepareStatementSetter")
	@Order(1)
	@Sql(scripts = "/database.sql")
	void test1(@Value("${member.insert}") String sql) {
		
		var data = operations.update(sql, stmt -> {
			stmt.setString(1, "admin");
			stmt.setString(2, "admin");
			stmt.setString(3, "Admin");
			stmt.setString(4, "3049473938");
			stmt.setString(5, "admin@gmail.com");
		});
					
		assertEquals(1, data);
	}
	
	@Test
	@DisplayName("Update with params")
	@Order(2)
	@Sql(scripts = "/database.sql")
	void test2(@Value("${member.insert}") String sql) {
		var data = operations.update(sql,"admin","admin","Admin","0949484393","admin@gmail.com");
		assertEquals(1, data);
	}
	
	@Test
	@DisplayName("Query with ResultExtractor and params")
	@Order(3)
	void test3(@Value("${member.select.by.name}") String sql) {
	
		ResultSetExtractor<Member> extractor = rs -> {
			while(rs.next()) {
				Member member = new Member();
				member.setLoginId(rs.getString(1));
				member.setPassword(rs.getString(2));
				member.setUsername(rs.getString(3));
				member.setPhone(rs.getString(4));
				member.setEmail(rs.getString(5));

				return member;
			}
			return null;
		};
		
		var data = operations.query(sql, extractor, "Admin%");
		
		assertEquals("Admin", data.getUsername());
	}
	
	@Test
	@DisplayName("Query with ResultExtractor and params")
	@Order(4)
	void test4(@Value("${member.select.by.name}") String sql) {
		ResultSetExtractor<Member> extractor = rs -> {
			while(rs.next()) {
				return rowMapper.mapRow(rs, 1);
			}
			return null;
		};
		var data = operations.query(sql,extractor,"Admin%");
		
		assertEquals("Admin", data.getUsername());
	}
	
	@Test
	@DisplayName("Query with rowmapper and params")
	@Order(5)
	void test5(@Value("${member.select.by.name}") String sql) {
		var list = operations.query(sql, rowMapper, "Admin%");
		assertEquals(1, list.size());
	}
	
	@Test
	@DisplayName("Query with property rowmapper and params")
	@Order(6)
	void test6(@Value("${member.select.by.name}") String sql) {
		var list = operations.query(sql, new BeanPropertyRowMapper<>(Member.class), "Admin%");
		assertEquals(1, list.size());
	}
	
	@Test
	@DisplayName("Select one with params")
	@Order(7)
	void test7(@Value("${member.select.by.pk}") String sql) {
		var data = operations.queryForObject(sql, rowMapper,"admin");
		assertEquals("Admin", data.getUsername());
	}
	
	@Test
	@DisplayName("Select one colum for one row with params")
	@Order(8)
	void test8() {
		var count = operations.queryForObject("select count(*) from member where loginId = ?", Long.class,"admin");
		assertEquals(1, count);
	}
	
}
