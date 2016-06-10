-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill;
-- 使用数据库
use seckill;
-- 创建秒杀库存表
CREATE TABLE IF NOT EXISTS seckill(
seckill_id bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
name varchar(120) NOT NULL COMMENT '商品名称',
number int NOT NULL COMMENT '库存数量',
start_time timestamp NOT NULL COMMENT '秒杀开始时间',
end_time timestamp NOT NULL COMMENT '秒杀结束时间',
create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (seckill_id),
KEY idx_start_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

-- 初始化数据
insert into
	seckill (name,number,start_time,end_time)
values
	('1000元秒杀IPhone6',100,'2016-06-10 00:00:00','2016-06-11 00:00:00'),
	('500元秒杀Ipad2',200,'2016-06-11 00:00:00','2016-06-12 00:00:00'),
	('300元秒杀小米4',300,'2016-06-12 00:00:00','2016-06-13 00:00:00'),
	('200元秒杀红米note',400,'2016-06-11 00:00:00','2016-06-12 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关信息
CREATE TABLE IF NOT EXISTS success_killed(
seckill_id bigint NOT NULL COMMENT '商品库存id',
user_phone bigint NOT NULL COMMENT '用户手机号',
state tinyint NOT NULL DEFAULT -1 COMMENT '状态标示：-1：无效，0：成功，1：已付款，2：已发货',
create_time timestamp NOT NULL  COMMENT '创建时间',
PRIMARY KEY (seckill_id,user_phone),
KEY idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';

//修改timestamp默认的on update current_timestamp
alter table seckill change start_time start_time timestamp not null default current_timestamp;