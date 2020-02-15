create database book_manage;
use book_manage;
create table `t_account`(
  id bigint(20) not null AUTO_INCREMENT primary key,
  is_manager char(1) not null default '0',
  name varchar(10) not null ,
  password varchar(100) not null ,
  sex varchar(2),
  age int,
  address varchar(20)
);
create table `t_book`(
   id bigint(20) not null AUTO_INCREMENT primary key,
   account_id bigint(20),
   title varchar(40) not null ,
   author_name varchar(10) not null ,
   source varchar(100),
   path varchar(200) not null ,
   verifyed char(1), #是否审核过
   push_date bigint(20) #发表时间
);
alter table t_book add column user_name varchar(30);
insert into t_account values (0,'1','admin','admin','男','100','未知');