package com.waiyanhtet.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.waiyanhtet.jdbcTempate.config.AppConfig;
import com.waiyanhtet.jdbcTemplate.dao.config.MemberRowMapper;
import com.waiyanhtet.jdbcTemplate.dto.Member;

@SpringJUnitConfig(classes = AppConfig.class)
@TestMethodOrder(OrderAnnotation.class)
public class JdbcOpertaionTestWithPrepareStatement {

	@Autowired
	private JdbcOperations operations;
	
	@Autowired
	private MemberRowMapper rowMapper;
	
	@Test
	@DisplayName("1. Execute with PrepareStatment Creator and callback for insert")
	@Order(1)
	@Sql(scripts = "/database.sql") //execute(PreparedStatementCreator psc, PreparedStatementCallback<Object> action)
	void test1(@Value("${member.insert}") String sql) {
		
		var result = operations.execute( (Connection conn) ->{
			
			var stmt = conn.prepareStatement(sql);
			stmt.setString(1, "admin");
			stmt.setString(2, "admin");
			stmt.setString(3, "Admin");
			stmt.setString(4, "094847393");
			stmt.setString(5, "admin@gmail.com");
			
			return stmt;
			
		}, PreparedStatement::executeUpdate);
		
		Assertions.assertEquals(1, result);
		
	}
	
	@Test
	@DisplayName("2. Execute with PrepareStatment Creator and callback using factory for insert")
	@Order(2)
	@Sql(scripts = "/database.sql") //execute(PreparedStatementCreator psc, PreparedStatementCallback<Object> action)
	void test2(@Qualifier("memberInsert") PreparedStatementCreatorFactory factory) {
		
		var creator = factory.newPreparedStatementCreator(List.of(
				"admin","admin","Admin","094847393","admin@gmail.com"));
		
		var result = operations.execute(creator, PreparedStatement::executeUpdate);

		Assertions.assertEquals(1, result);
		
	}
	
	@Test
	@DisplayName("3. Update with PrepareStatment Creator using factory for insert")
	@Order(3)
	@Sql(scripts = "/database.sql") //update(PreparedStatementCreator psc)
	void test3(@Qualifier("memberInsert") PreparedStatementCreatorFactory factory) {
		
		var creator = factory.newPreparedStatementCreator(List.of(
				"admin","admin","Aung Aung","094847393","admin@gmail.com"));
		
		var result = operations.update(creator);
		Assertions.assertEquals(1, result);
		
	}
	
	@Test
	@DisplayName("4. Execute with PrepareStatment Creator and callback using factory for select")
	@Order(4) //execute(PreparedStatementCreator psc, PreparedStatementCallback<ArrayList<Object>> action) 
	void test4(@Qualifier("memberFindByUsernameLike") PreparedStatementCreatorFactory factory) {
		
		var creator = factory.newPreparedStatementCreator(List.of(
				"Aung%"));
		
		var result = operations.execute(creator, stmt -> {
			var list = new ArrayList<>();
			var rs = stmt.executeQuery();
			while(rs.next()) {
				Member member = new Member();
				member.setLoginId(rs.getString(1));
				member.setPassword(rs.getString(2));
				member.setUsername(rs.getString(3));
				member.setPhone(rs.getString(4));
				member.setEmail(rs.getString(5));
				list.add(member);
			}
			return list;
		});
		
		Assertions.assertEquals(1, result.size());
		
	}
	
	@Test
	@DisplayName("5. Query with PrepareStatment Creator and rowmapper using factory for select")
	@Order(5) //query(PreparedStatementCreator psc, RowMapper<Member> rowMapper)
	void test5(@Qualifier("memberFindByUsernameLike") PreparedStatementCreatorFactory factory) {
		
		var creator = factory.newPreparedStatementCreator(List.of(
				"Aung%"));
		
		RowMapper<Member> mapper = (rs, no) -> {
			Member member = new Member();
			member.setLoginId(rs.getString(1));
			member.setPassword(rs.getString(2));
			member.setUsername(rs.getString(3));
			member.setPhone(rs.getString(4));
			member.setEmail(rs.getString(5));
			return member;
		};
		
		var result = operations.query(creator, mapper);
		
		Assertions.assertEquals(1, result.size());
		
	}
	
	@Test
	@DisplayName("6. Query with PrepareStatment Creator and rowmapper using factory for select")
	@Order(6) //query(PreparedStatementCreator psc, RowMapper<Member> rowMapper)
	void test6(@Qualifier("memberFindByUsernameLike") PreparedStatementCreatorFactory factory) {
		
		//using autowired rowmapper
		var result = operations.query(factory.newPreparedStatementCreator(List.of("Aung%")), rowMapper); 
	
		Assertions.assertEquals(1, result.size());
		
	}
	
	@Test
	@DisplayName("7. Query with PrepareStatment Creator and ResultSetExtractor using factory for select")
	@Order(7) //query(PreparedStatementCreator psc, ResultSetExtractor<Member> rse)
	void test7(@Qualifier("memberFindByPK") PreparedStatementCreatorFactory factory) {
		
		var result = operations.query(factory.newPreparedStatementCreator(List.of("admin")), rs ->{
			while(rs.next()) {
				return rowMapper.mapRow(rs, 1);
			}
			return null;
		}); 
	
		Assertions.assertEquals("Aung Aung", result.getUsername());
		
	}
}
