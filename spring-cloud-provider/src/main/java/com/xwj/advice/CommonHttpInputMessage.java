package com.xwj.advice;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import lombok.extern.slf4j.Slf4j;

/**
 * 常规请求数据（未加密的请求）
 */
@Slf4j
public class CommonHttpInputMessage implements HttpInputMessage {

	private HttpHeaders headers;
	private InputStream body;

	public CommonHttpInputMessage(HttpInputMessage inputMessage) throws Exception {
		String json = IOUtils.toString(inputMessage.getBody(), "UTF-8");
		log.error("app请求未加密参数:{}", json);
		this.headers = inputMessage.getHeaders();
		this.body = IOUtils.toInputStream(json, "UTF-8");
	}

	@Override
	public InputStream getBody() throws IOException {
		return body;
	}

	@Override
	public HttpHeaders getHeaders() {
		return headers;
	}
}
