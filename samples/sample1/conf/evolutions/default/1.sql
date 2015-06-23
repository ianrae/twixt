# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table flight (
  id                        bigint not null,
  s                         varchar(255),
  user_id                   bigint,
  size                      integer,
  lang                      varchar(255),
  is_admin                  boolean,
  start_date                timestamp,
  account_type_id           bigint,
  constraint pk_flight primary key (id))
;

create sequence flight_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists flight;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists flight_seq;

