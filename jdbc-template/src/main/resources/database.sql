
drop table if exists member;

create table member(
	loginId varchar(8) primary key,
	password varchar(8) not null,
	username varchar(30) not null,
	phone varchar(13),
	email varchar(20)
);