package com.xwj.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

/**
 * 高德地图api
 */
@Service
public class AMapService {

	private static final String key = "c1ba21700a30aa779a59338470826733";// 高德Key
	private static final String SERVICE_ID = "131714";

	private String domain = "https://tsapi.amap.com";

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
	 * 增加轨迹
	 */
	@SuppressWarnings("unchecked")
	public void addService() {
		Map<String, Object> params = new HashMap<>();
		params.put("key", key);
		params.put("name", "travel_supplier");
		String responseStr = requestPost(params, "/v1/track/service/add");
		Map<String, Object> objMap = (Map<String, Object>) JSON.parse(responseStr);
		System.out.println("objMap:" + objMap);
	}

	/**
	 * 增加轨迹
	 */
	@SuppressWarnings("unchecked")
	public void addTrack(String tid) {
		addService();

		Map<String, Object> params = new HashMap<>();
		params.put("key", key);
		params.put("sid", SERVICE_ID);
		params.put("tid", tid);
		String responseStr = requestPost(params, "/v1/track/trace/add");
		Map<String, Object> objMap = (Map<String, Object>) JSON.parse(responseStr);
		System.out.println("objMap:" + objMap);
	}

}