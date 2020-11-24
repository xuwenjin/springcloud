# 创建订单表
CREATE TABLE t_order_0 (
	id BIGINT(20),
	order_type TINYINT(1),
	status VARCHAR(255)
); 
CREATE TABLE t_order_1 (
	id BIGINT(20),
	order_type TINYINT(1),
	status VARCHAR(255)
); 
# 创建订单详情表(子表)
CREATE TABLE t_order_detail_0 (
	id BIGINT(20),
	order_id BIGINT(20),
	order_type TINYINT(1),
	description VARCHAR(255)
); 
CREATE TABLE t_order_detail_1 (
	id BIGINT(20),
	order_id BIGINT(20),
	order_type TINYINT(1),
	description VARCHAR(255)
); 
# 创建字典表
CREATE TABLE t_dict (
	id BIGINT(20),
	code VARCHAR(255),
	status VARCHAR(255)
); 
