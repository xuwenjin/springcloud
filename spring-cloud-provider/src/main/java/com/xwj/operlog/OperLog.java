package com.xwj.operlog;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import lombok.Getter;
import lombok.Setter;

/**
 * 操作日志
 * 
 * @author xwj
 *
 */
@Entity
@Getter
@Setter
public class OperLog implements Serializable {

	private static final long serialVersionUID = -9025235999092063537L;

	@Id
	@TableGenerator(name = "global_id_gen", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "global_id_gen")
	private Long id;

	private String username; // 用户名

	private String operation; // 操作

	private String method; // 方法名

	private String params; // 参数

	private String ip; // ip地址

	private Date createDate; // 操作时间

}
