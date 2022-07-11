package com.waiyanhtet.test;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.waiyanhtet.jdbcTempate.config.AppConfig;
import com.waiyanhtet.jdbcTemplate.dao.MemberDao;
import com.waiyanhtet.jdbcTemplate.dto.Member;

@TestMethodOrder(OrderAnnotation.class)
@SpringJUnitConfig(classes = AppConfig.class)
public class MemberDaoTest {

	@Autowired
	private MemberDao dao;

	@Test
	@Sql("/database.sql")
	@Order(1)
	public void createTest() {
		Member member = new Member();
		member.setLoginId("admin");
		member.setPassword("admin");
		member.setUsername("Admin");
		member.setPhone("095958486");

		dao.create(member);
	}
}
