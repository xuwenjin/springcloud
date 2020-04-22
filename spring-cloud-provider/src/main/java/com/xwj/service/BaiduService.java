package com.xwj.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.xwj.util.TimeUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 百度地图api
 */
@Slf4j
@Service
public class BaiduService {

	private static final String BAIDU_AK = "1aXNpW7CNdZx90MVxesFEhE0mtB40HnQ";
	private static final String SERVICE_ID = "220559";

	private String domain = "http://yingyan.baidu.com";

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 发送post请求
	 */
	private String requestPost(Map<String, Object> params, String url) {
		String apiDomain = domain;

		MultiValueMap<String, Object> urlParams = this.mapToMultiValueMap(params);

		String sUrl = apiDomain + url;
		return restTemplate.postForObject(sUrl, urlParams, String.class);
	}

	/**
	 * HashMap转为MultiValueMap
	 */
	private MultiValueMap<String, Object> mapToMultiValueMap(Map<String, Object> map) {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() != null) {
				params.add(entry.getKey(), entry.getValue().toString());
			}
		}
		return params;
	}

	/**
	 * 增加终端用户
	 */
	@SuppressWarnings("unchecked")
	public void addEntity(String entityName) {
		Map<String, Object> params = new HashMap<>();
		params.put("ak", BAIDU_AK);
		params.put("service_id", SERVICE_ID);
		params.put("entity_name", entityName);
		params.put("cityCode", "100001");
		String responseStr = requestPost(params, "/api/v3/entity/add");
		Map<String, Object> objMap = (Map<String, Object>) JSON.parse(responseStr);
		if (isSuccess(objMap)) {
			System.out.println("objMap:" + objMap);
		}
	}

	/**
	 * 上传轨迹
	 */
	@SuppressWarnings("unchecked")
	public void addPoint(String entityName, double lat, double lng) {
		Map<String, Object> params = new HashMap<>();
		params.put("ak", BAIDU_AK);
		params.put("service_id", SERVICE_ID);
		params.put("entity_name", entityName);
		params.put("latitude", lat);
		params.put("longitude", lng);
		params.put("loc_time", TimeUtil.getCurrSecondTimestamp());
		params.put("coord_type_input", "wgs84");
		params.put("cityCode", "100001");
		String responseStr = requestPost(params, "/api/v3/track/addpoint");
		Map<String, Object> objMap = (Map<String, Object>) JSON.parse(responseStr);
		if (isSuccess(objMap)) {
			System.out.println("objMap:" + objMap);
		}
	}

	/**
	 * 是否成功
	 */
	private boolean isSuccess(Map<String, Object> objMap) {
		int status = MapUtils.getIntValue(objMap, "status");
		if (status != 0) {
			String message = MapUtils.getString(objMap, "message");
			log.error("错误码：{}，错误信息：{}", status, message);
			return false;
		}
		return true;
	}

}