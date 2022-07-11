package com.waiyanhtet.test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.waiyanhtet.jdbcTempate.config.AppConfig;
import com.waiyanhtet.jdbcTemplate.dto.Member;

@TestMethodOrder(OrderAnnotation.class)
@SpringJUnitConfig(classes = AppConfig.class)
public class JdbcOpertionsTestWithStaticStatement {
	
	@Autowired
	private JdbcOperations operations;

	@Test
	@Order(1) //execute(String sql)
	@Sql(scripts = "/database.sql")
	void jdbcOpertionExecute() {
		operations.execute("insert into member(loginId,password,username) values ('admin','admin','Admin')");
	}

	@Test
	@Order(2) //execute(ConnectionCallback<Integer> action)
	void executeWithConnection() {
		var data = operations.execute((Connection conn) -> {
			var stmt = conn.createStatement();
			var rs = stmt.executeQuery("select count(*) from member");
			while (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		});

		Assertions.assertEquals(1, data);
	}

	@Test
	@Order(3) //execute(StatementCallback<Integer> action)
	void executeWithStatement() {
		var data = operations.execute((Statement stmt) -> {
			var rs = stmt.executeQuery("select count(*) from member");
			while (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		});

		Assertions.assertEquals(1, data);
	}

	@Test
	@Order(4) //query(String sql, ResultSetExtractor<ArrayList<Object>> rse) 
	void queryWithResultSetExtractor() {
		var data = operations.query("select * from member", rs -> {
			var list = new ArrayList<>();
			while (rs.next()) {
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

		Assertions.assertEquals(1, data.size());
	}

	@Test
	@Order(5) //query(String sql, RowCallbackHandler rch)
	void queryWithRowCallBackHandler() {
		var list = new ArrayList<>();
		operations.query("select * from member", rs -> {

			Member member = new Member();
			member.setLoginId(rs.getString(1));
			member.setPassword(rs.getString(2));
			member.setUsername(rs.getString(3));
			member.setPhone(rs.getString(4));
			member.setEmail(rs.getString(5));
			list.add(member);
		});

		Assertions.assertEquals(1, list.size());
	}

	@Test
	@Order(6) //query(String sql, RowMapper<Member> rowMapper)
	void queryWithRowMapper() {

		var data = operations.query("select * from member", (rs, no) -> {

			Member member = new Member();
			member.setLoginId(rs.getString(1));
			member.setPassword(rs.getString(2));
			member.setUsername(rs.getString(3));
			member.setPhone(rs.getString(4));
			member.setEmail(rs.getString(5));

			return member;

		});

		Assertions.assertEquals(1, data.size());
	}

	@Test
	@Order(7) //queryForObject(String sql, RowMapper<Member> rowMapper)
	void queryForObjectWithRowMapper() {

		var data1 = operations.queryForObject("select * from member where loginId = 'admin'", (rs, no) -> {

			Member member = new Member();
			member.setLoginId(rs.getString(1));
			member.setPassword(rs.getString(2));
			member.setUsername(rs.getString(3));
			member.setPhone(rs.getString(4));
			member.setEmail(rs.getString(5));

			return member;

		});

		Assertions.assertEquals("Admin", data1.getUsername());

		var data2 = operations.queryForObject("select username from member where loginId = 'admin'", String.class);
		Assertions.assertEquals("Admin", data2);

	}
}
