package com.xwj.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OpenApiService {

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 请求参数增加签名
	 */
	private String sign(Map<String, Object> params) {
		String signKey = "a535edf075ec89962d762f876babe232";

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.putAll(params);

		// 1、生成签名时，将sign_key加入传递的参数，参与签名，但是不参与参数传递
		paramMap.put("sign_key", signKey);

		// 2、将所有的参数按照key值按字符升序排列(key1value1key2value2),生成小写的签名作为sign;
		Map<String, Object> sortedMap = new TreeMap<String, Object>(paramMap);

		// 3、将所有参数拼接成字符串
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
			sb.append(entry.getKey()).append(entry.getValue());
		}

		return DigestUtils.md5DigestAsHex(sb.toString().getBytes());
	}

	/**
	 * 请求参数中增加必传参数
	 */
	private void buildRequestMap(Map<String, Object> params) {
		String clientId = "66b9816c7d7b55c8";
		if (params == null) {
			params = new HashMap<>();
		}
		params.put("client_id", clientId);
		params.put("timestamp", getCurrMinSecondTimestamp());
		// params.put("timestamp", "1569232774204");
		params.put("sign", this.sign(params));
	}

	/**
	 * 发送get请求
	 */
	private String request(Map<String, Object> params, String url, String urlParams) {
		String apiDomain = "http://localhost:8088";
		this.buildRequestMap(params);

		log.info("请求参数：{}", params);

		String sUrl = apiDomain + url + urlParams;
		return restTemplate.getForObject(sUrl, String.class, params);
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
	 * 发送post请求
	 */
	private String requestPost(Map<String, Object> params, String url) {
		String apiDomain = "http://localhost:8088";
		this.buildRequestMap(params);

		MultiValueMap<String, Object> urlParams = this.mapToMultiValueMap(params);

		String sUrl = apiDomain + url;
		return restTemplate.postForObject(sUrl, urlParams, String.class);
	}

	/**
	 * 构建请求参数，用于拼到url后面
	 * 
	 * @return
	 */
	private StringBuffer buildRequestParam() {
		StringBuffer sb = new StringBuffer();
		sb.append("?client_id={client_id}");
		sb.append("&timestamp={timestamp}");
		sb.append("&sign={sign}");
		return sb;
	}

	/**
	 * 同步城市列表
	 */
	public void getOpenApi() {
		StringBuffer sb = this.buildRequestParam();
		String responseStr = request(new HashMap<>(), "/app/resource/common/getCarList", sb.toString());
		log.info("responseStr:{}", responseStr);
	}

	/**
	 * 同步城市列表
	 */
	public void getOpenApi2() {
		// StringBuffer sb = this.buildRequestParam();
		Map<String, Object> params = new HashMap<>();
		params.put("orderId", "123456");
		// String responseStr = request(new HashMap<>(),
		// "/app/resource/redis/get/abc", sb.toString());
		String responseStr = requestPost(params, "/app/resource/debug/order/orderPush");
		log.info("responseStr:{}", responseStr);
	}

	/**
	 * 取消订单
	 */
	public void postOpenApi() {
		Map<String, Object> params = new HashMap<>();
		params.put("callNo", "HJ201909205270");
		params.put("cancelReason", "不想打车123456789");
		params.put("supCode", "sq");
		params.put("orderId", "1234567");
		params.put("cancelReason", "不想打车123456789");
		// String responseStr = requestPost(params,
		// "/app/resource/order/cancel");
		String responseStr = requestPost(params, "/app/resource/order/create/1234");
		log.info("responseStr:{}", responseStr);
	}

	/**
	 * 获取当前时间精确到毫秒的时间戳(13位)
	 */
	public static long getCurrMinSecondTimestamp() {
		return getMinSecondTimestamp(new Date());
	}

	public static long getMinSecondTimestamp(Date date) {
		if (null == date) {
			return 0;
		}
		String timestamp = String.valueOf(date.getTime());
		return Long.valueOf(timestamp);
	}

	public static void main(String[] args) {
		System.out.println(getCurrMinSecondTimestamp());
	}

}
