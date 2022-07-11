package com.waiyanhtet.jdbcTemplate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.waiyanhtet.jdbcTemplate.dto.Member;

@Component
public class MemberDao {

	private JdbcTemplate template;

	public MemberDao(JdbcTemplate template) {
		super();
		this.template = template;
	}
	
	public void create(Member member) {
		template.update("insert into member values(?,?,?,?,?)", 
				member.getLoginId(),
				member.getPassword(),
				member.getUsername(),
				member.getPhone(),
				member.getEmail());
	}
}
