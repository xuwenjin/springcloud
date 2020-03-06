package com.xwj.handler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;

import com.xwj.common.ApiResponseData;

public final class HandlerChain extends AbstractHandler {

	// 所有链(使用CopyOnWriteArrayList线程安全)
	private List<AbstractHandler> handlerList = new CopyOnWriteArrayList<>();

	// 链的索引(AtomicInteger原子性)
	private AtomicInteger index = new AtomicInteger(0);

	// 添加 case
	public HandlerChain addHandler(AbstractHandler baseCase) {
		handlerList.add(baseCase);
		return this;
	}

	@Override
	public ApiResponseData handleAuth(HandlerChain chain, HandlerMethod handlerMethod, HttpServletRequest request,
			HttpServletResponse response) {
		if (index.get() < handlerList.size()) {
			// 获取当前链
			AbstractHandler currentHandler = handlerList.get(index.get());

			// 修改索引值，以便下次回调获取下个链，达到遍历效果
			index.incrementAndGet();

			if (currentHandler.isSupport(handlerMethod)) {
				// 支持校验，调用当前链处理方法
				return currentHandler.handleAuth(this, handlerMethod, request, response);
			} else {
				// 不支持校验，调用下一链
				return handleAuth(this, handlerMethod, request, response);
			}
		}

		return ApiResponseData.success();
	}

}