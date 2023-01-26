drop database if exists `goooocoin`;
create database `goooocoin`;
use `goooocoin`;
create table `user`
(
    `id`           int(20)      not null auto_increment,
    `flavor`       varchar(255) not null default '',
    `method`       varchar(255) not null default '',
    `type`         varchar(255) not null default '',
    `username`     varchar(255) not null default '',
    `password`     varchar(255) not null default '',
    `phone_number` varchar(20)  not null default '',
    `user_type`    int(2)       not null default 0,
    `avatar_address` varchar(255) not null default '',
    primary key (`id`)
) engine = InnoDB
  default charset = utf8mb4;
insert into `user` (username, password, phone_number, user_type)
values ('user', md5('passA'), '12345678900', 0);
insert into `user` (username, password, phone_number, user_type)
values ('admin', md5('passB'), '12345678901', 1);
insert into `user` (username, password, phone_number, user_type)
values ('fish', md5('Qwer12345678'), '12345678902', 0);



create table `collection`
(
    `id`          int(20)     not null auto_increment,
    `user_id`     int(20)     not null,
    `entity_id`   int(20)     not null,
    `entity_name` varchar(20) not null,
    primary key (id),
    foreign key (user_id) references `user` (id),
    index (user_id)
) engine = InnoDB
  default charset = utf8mb4;

commit;
