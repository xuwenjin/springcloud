package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.service.AMapService;

/**
 * 高德地图
 */
@RestController
@RequestMapping("amap")
public class AMapController {

	@Autowired
	private AMapService amapService;

	/**
	 * 增加轨迹
	 */
	@GetMapping("addTrack")
	public void addTrack() {
		amapService.addTrack("000001");
	}

}
