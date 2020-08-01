# 数据库创建脚本

drop database if exists mpdb_pl;
create database mpdb_pl;
use mpdb_pl;


#用户表
CREATE TABLE `user_info`
(
  `id`       varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  `avatar`   varchar(200) NOT NULL,
  `gender`   int(11)      NOT NULL,
  `country`  varchar(100) NOT NULL,
  `province` varchar(100) NOT NULL,
  `city`     varchar(100) NOT NULL,
  `language` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


# 文章表
CREATE TABLE `passage`
(
  `id`          varchar(100) NOT NULL,
  `url`         varchar(200) NOT NULL,
  `platform_id` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 评论表
CREATE TABLE `comment`
(
  `id`           varchar(100) NOT NULL,
  `passage_id`   varchar(100) NOT NULL,
  `content`      varchar(200) NOT NULL,
  `from_uid`     varchar(100) NOT NULL,
  `create_time`  timestamp    NOT NULL,
  `star_number`  int(11)      NOT NULL,
  `reply_number` int(11)      NOT NULL,
  `floor`        int(11)      NOT NULL,
  `verified`     int(11)      NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`),
  INDEX idx_passage_id (passage_id)                     
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 回复表
CREATE TABLE `reply`
(
  `id`          varchar(100) NOT NULL,
  `comment_id`  varchar(100) NOT NULL,
  `reply_id`    varchar(100) NOT NULL,
  `reply_type`  int(11)      NOT NULL,
  `content`     varchar(200) NOT NULL,
  `from_uid`    varchar(100) NOT NULL,
  `create_time` timestamp    NOT NULL,
  `star_number` int(11)      NOT NULL,
  PRIMARY KEY (id),
  INDEX idx_comment_id (comment_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 用户点赞表
CREATE TABLE `user_star`
(
  `id`          varchar(100) NOT NULL,
  `to_id`       varchar(100) NOT NULL,
  `to_type`     int(11)      NOT NULL,
  `user_id`     varchar(100) NOT NULL,
  `create_time` timestamp    NOT NULL,
  PRIMARY KEY (id),
  INDEX idx_to_id (to_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


# 对数据库插入测试数据

#插入用户测试数据
insert into user_info (id, username, avatar, gender, country, province, city, language)
values ('test1', '天下无敌', 'https://avatars1.githubusercontent.com/u/36162683?s=460&v=4', 1, '中国', '湖南省', '长沙市', '中文');

insert into user_info (id, username, avatar, gender, country, province, city, language)
values ('test2', '智勇双全', 'https://avatars1.githubusercontent.com/u/36260787?s=460&v=4', 1, '中国', '湖南省', '长沙市', '中文');

insert into user_info (id, username, avatar, gender, country, province, city, language)
values ('test3', '文武双修', 'https://avatars0.githubusercontent.com/u/7939566?s=460&v=4', 1, '中国', '湖南省', '长沙市', '中文');

insert into user_info (id, username, avatar, gender, country, province, city, language)
values ('test4', '天下无敌', 'https://avatars0.githubusercontent.com/u/9524411?s=460&v=4', 1, '中国', '湖南省', '长沙市', '中文');


#插入文章数据
insert into passage (id, url, platform_id)
values ('test1', 'www.baidu.com', 'test3');
insert into passage (id, url, platform_id)
values ('test2', 'www.baidu.com', 'test3');
insert into passage (id, url, platform_id)
values ('test3', 'www.baidu.com', 'test3');

# 插入评论数据
insert into comment (id, passage_id, content, from_uid, create_time, star_number, reply_number, floor)
values ('test1', 'test1', '测试评论1', 'test1', current_timestamp, 0, 6, 1);

insert into comment (id, passage_id, content, from_uid, create_time, star_number, reply_number, floor)
values ('test2', 'test1', '测试评论2', 'test2', current_timestamp, 0, 2, 2);

insert into comment (id, passage_id, content, from_uid, create_time, star_number, reply_number, floor)
values ('test3', 'test1', '测试评论3', 'test3', current_timestamp, 0, 0, 3);

insert into comment (id, passage_id, content, from_uid, create_time, star_number, reply_number, floor)
values ('test4', 'test1', '测试评论4', 'test4', current_timestamp, 0, 0, 4);

# 插入回复数据(对一个评论)
insert into reply (id, comment_id, reply_id, reply_type, content, from_uid, create_time, star_number)
values ('rtest1', 'test1', 'test1', 1, '你说的有道理!', 'test1', current_timestamp, 0);

insert into reply (id, comment_id, reply_id, reply_type, content, from_uid, create_time, star_number)
values ('rtest2', 'test1', 'rtest1', 2, '你说的有道理!', 'test2', current_timestamp, 0);

insert into reply (id, comment_id, reply_id, reply_type, content, from_uid, create_time, star_number)
values ('rtest3', 'test1', 'rtest1', 2, '你说的有道理!', 'test3', current_timestamp, 0);

insert into reply (id, comment_id, reply_id, reply_type, content, from_uid, create_time, star_number)
values ('rtest4', 'test2', 'test2', 1, '你说的有道理!', 'test1', current_timestamp, 0);

insert into reply (id, comment_id, reply_id, reply_type, content, from_uid, create_time, star_number)
values ('rtest5', 'test1', 'rtest2', 2, '你说的有道理!', 'test2', current_timestamp, 0);

insert into reply (id, comment_id, reply_id, reply_type, content, from_uid, create_time, star_number)
values ('rtest6', 'test2', 'rtest4', 2, '你说的有道理!', 'test3', current_timestamp, 0);

insert into reply (id, comment_id, reply_id, reply_type, content, from_uid, create_time, star_number)
values ('rtest7', 'test1', 'rtest2', 2, '云麓谷越做越好!!', 'test3', current_timestamp, 0);

insert into reply (id, comment_id, reply_id, reply_type, content, from_uid, create_time, star_number)
values ('rtest8', 'test1', 'test1', 1, '云麓谷越做越好!!', 'test3', current_timestamp, 0);

select * from comment;
select * from passage;