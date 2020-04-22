package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.service.BaiduService;

/**
 * 百度地图
 */
@RestController
@RequestMapping("baidu")
public class BaiduController {

	@Autowired
	private BaiduService baiduService;

	private static final String ENTITY_NAME = "000001";

	/**
	 * 增加终端用户
	 */
	@PostMapping("addEntity")
	public void addEntity() {
		baiduService.addEntity(ENTITY_NAME);
	}

	/**
	 * 上传轨迹
	 */
	@PostMapping("addPoint")
	public void addPoint(double lat, double lng) {
		baiduService.addPoint(ENTITY_NAME, lat, lng);
	}

}
