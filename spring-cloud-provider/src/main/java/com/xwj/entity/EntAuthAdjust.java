package com.xwj.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import com.xwj.dbdef.ColumnDef;
import com.xwj.dbdef.TableDef;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@TableDef("企业授信调整表")
public class EntAuthAdjust {

	@ColumnDef("主键id")
	@Id
	@TableGenerator(name = "global_id_gen", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "global_id_gen")
	private Long id;

	@ColumnDef("运营商id")
	private Long operId; // 运营商id

	@ColumnDef("企业id")
	private Long entId; // 企业id

	@ColumnDef("企业编号")
	private String entCode; // 企业编号

	private String entName; // 企业名称

	@ColumnDef("协议时间")
	private Date protocolDate; // 协议时间

	@ColumnDef("企业信用额度")
	private BigDecimal authLimit; // 企业信用额度

	@ColumnDef(value = "滞纳金比例", length = "19, 6")
	private BigDecimal penaltyFeeRatio; // 滞纳金比例

	@ColumnDef(value = "调整时间")
	private Date adjustDate; // 调整时间

	@ColumnDef("调整次数")
	private int adjustCount; // 调整次数

}