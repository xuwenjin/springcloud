package com.xwj.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import com.xwj.annotations.ColumnDef;
import com.xwj.annotations.TableDef;
import com.xwj.dbdef.JdbcTypeEnum;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@TableDef("数据库备注")
public class DbDefInfo {

	@Id
	@TableGenerator(name = "global_id_gen", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "global_id_gen")
	private Long id;

	@ColumnDef(value = "filed1", length = "100")
	public String filed1;

	@ColumnDef("filed2")
	public Integer filed2;
	@ColumnDef("filed3")
	public int filed3;

	@ColumnDef("filed4")
	public Long filed4;
	@ColumnDef("filed5")
	public long filed5;

	@ColumnDef("filed6")
	public Double filed6;
	@ColumnDef("filed7")
	public double filed7;

	@ColumnDef("filed1")
	public Float filed8;
	@ColumnDef("filed9")
	public float filed9;

	@ColumnDef("filed10")
	public BigDecimal filed10;

	@ColumnDef("filed1")
	public Boolean filed11;

	@ColumnDef("filed12")
	public boolean filed12;

	@ColumnDef("filed13")
	public Date filed13;

	@Lob
	@ColumnDef("filed14")
	public String filed14;

	@ManyToOne
	@ColumnDef("filed14")
	public UserInfo userInfo;

	@ColumnDef("filed15")
	public JdbcTypeEnum filed15;
	
	public JdbcTypeEnum filed16;

}
