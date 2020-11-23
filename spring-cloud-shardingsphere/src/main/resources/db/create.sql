CREATE TABLE t_order0 (
	id BIGINT(20),
	create_date DATETIME ,
	version INT(11),
	order_type TINYINT(1),
	status VARCHAR(255)
); 
CREATE TABLE t_order1 (
	id BIGINT(20),
	create_date DATETIME ,
	version INT(11),
	order_type TINYINT(1),
	status VARCHAR(255)
); 

CREATE TABLE t_dict (
	id BIGINT(20),
	code VARCHAR(255),
	status VARCHAR(255)
); 
