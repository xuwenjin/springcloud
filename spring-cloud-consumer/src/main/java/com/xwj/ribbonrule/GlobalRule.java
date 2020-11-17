package com.xwj.ribbonrule;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;

import java.util.List;

/**
 * 自定义ribbon负载均衡策略(实现IRule接口)
 */
public class GlobalRule implements IRule {

	private ILoadBalancer iLoadBalancer;

	@Override
	public Server choose(Object o) {
		List<Server> servers = iLoadBalancer.getAllServers();
		// 始终取服务列表的第一个
		return servers.get(0);
	}

	@Override
	public void setLoadBalancer(ILoadBalancer iLoadBalancer) {
		this.iLoadBalancer = iLoadBalancer;
	}

	@Override
	public ILoadBalancer getLoadBalancer() {
		return this.iLoadBalancer;
	}
}
