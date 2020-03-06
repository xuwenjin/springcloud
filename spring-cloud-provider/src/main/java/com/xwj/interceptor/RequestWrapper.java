package com.xwj.interceptor;

import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.xwj.utils.CommonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 修改request
 */
@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {

	/** 将request对象中的参数修改后，放在这个集合里，随后项目取的所有Parameter都是从这个集合中取数 */
	private Map<String, String[]> params = new ConcurrentHashMap<String, String[]>();

	public RequestWrapper(HttpServletRequest request) {
		super(request);

		Map<String, String[]> parameterMap = request.getParameterMap();
		if (MapUtils.isEmpty(parameterMap)) {
			// 请求参数为空，直接返回
			return;
		}

		String[] keyArr = parameterMap.get("key");
		String[] dataArr = parameterMap.get("data");
		if (ArrayUtils.isEmpty(keyArr) || ArrayUtils.isEmpty(dataArr)) {
			// 如果请求参数没有加密，则直接将参数添加到request
			params.putAll(parameterMap);
			return;
		}

		try {
			String key = keyArr[0];
			String data = dataArr[0];
			log.info("key:{}", key);
			log.info("data:{}", data);

			// 将解密后的请求参数重新放入params
			String appId = CommonUtil.getAppId(request.getHeader("AppId"));
			JSONObject jsonObj = CommonUtil.decrpt(key, data, appId);
			for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
				String sKey = entry.getKey();
				Object obj = entry.getValue();
				String sValue = Objects.toString(obj, null);
				String[] arr = { sValue };
				params.put(sKey, arr);
			}
		} catch (Exception e) {
			params.putAll(parameterMap);
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public String getParameter(String name) {
		String[] values = params.get(name);
		if (ArrayUtils.isEmpty(values)) {
			return null;
		}
		return StringUtils.trim(values[0]);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		Vector<String> vector = new Vector<String>();
		Set<Entry<String, String[]>> entrySet = params.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			vector.add(entry.getKey());
		}
		return vector.elements();
	}

	@Override
	public String[] getParameterValues(String name) {
		return params.get(name);
	}

}