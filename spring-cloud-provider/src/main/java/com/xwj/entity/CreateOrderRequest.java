package com.xwj.entity;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 创建订单-请求实体
 */
@Getter
@Setter
public class CreateOrderRequest {

	/** 出发地纬度 */
	private Double slat;

	/** 出发地经度 */
	private Double slng;

	/** 目的地纬度 */
	private Double elat;

	/** 目的地经度 */
	private Double elng;

	/** 订单类型(默认实时单) */
	private String orderType = "1"; // 1:实时单 2:预约订单 3:接机 4:送机

	/** 用车时间 */
	private Date departureTime; // 预约单、送机单必填。时间范围为当前时间+30分钟 ~ 当前日期+3天

	/** 城市编码 */
	private String cityCode;

	/** 叫车人手机号 */
	private String userMobile;

	/** 出发地名称 */
	private String startName;

	/** 出发地详细地址 */
	private String startAddress;

	/** 目的地名称 */
	private String endName;

	/** 目的地详细地址 */
	private String endAddress;

	/** 乘车人手机号 */
	private String passengerMobile; // 不填表示给自己叫车

	/** 供应商车型信息列表 */
	private List<String> supCarList;

}
