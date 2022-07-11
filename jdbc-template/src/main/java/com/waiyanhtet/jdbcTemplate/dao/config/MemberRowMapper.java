package com.waiyanhtet.jdbcTemplate.dao.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.waiyanhtet.jdbcTemplate.dao.meta.MapRow;
import com.waiyanhtet.jdbcTemplate.dto.Member;

@MapRow
public class MemberRowMapper implements RowMapper<Member> {

	@Override
	public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
		Member member = new Member();
		member.setLoginId(rs.getString(1));
		member.setPassword(rs.getString(2));
		member.setUsername(rs.getString(3));
		member.setPhone(rs.getString(4));
		member.setEmail(rs.getString(5));
		return member;
	}

}
