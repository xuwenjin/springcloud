package com.xwj.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverPosition {

	/** 司机id */
	private String driverId;

	/** 城市编码 */
	private String cityCode;

	/** 经度 */
	private double lng;

	/** 纬度 */
	private double lat;

}
