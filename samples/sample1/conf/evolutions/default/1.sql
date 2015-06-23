# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table user_model (
  id                        bigint not null,
  name                      varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  subj_id                   bigint,
  pwd                       varchar(255),
  email                     varchar(255),
  confirmed                 boolean,
  triflag                   integer,
  user_type                 integer,
  srt_hint                  varchar(255),
  last_mod                  timestamp not null,
  constraint pk_user_model primary key (id))
;

create sequence user_model_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists user_model;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists user_model_seq;

