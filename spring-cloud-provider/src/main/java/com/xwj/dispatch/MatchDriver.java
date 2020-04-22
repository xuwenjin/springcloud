package com.xwj.dispatch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.stereotype.Component;

import com.xwj.entity.CreateOrderRequest;
import com.xwj.entity.DriverPosition;
import com.xwj.service.RedisGeoService;
import com.xwj.utils.CommonUtil;

@Component
public class MatchDriver {

	@Autowired
	private RedisGeoService redisGeoService;

	private final String GEO_KEY = "geo_key";

	public List<DriverPosition> getNearDrivers(CreateOrderRequest order) {
		String cityId = order.getCityCode();
		double slng = order.getSlng();
		double slat = order.getSlat();

		String redisKey = CommonUtil.buildRedisKey(GEO_KEY, cityId);

		Circle circle = new Circle(slng, slat, Metrics.KILOMETERS.getMultiplier());
		GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisGeoService.nearByXY(redisKey, circle, 5);
		System.out.println("查询附近司机位置：" + results);

		List<DriverPosition> list = new ArrayList<>();
		results.forEach(item -> {
			GeoLocation<String> location = item.getContent();
			Point point = location.getPoint();
			DriverPosition position = DriverPosition.builder().cityCode(cityId).driverId(location.getName())
					.lng(point.getX()).lat(point.getY()).build();
			list.add(position);
		});

		return list;
	}

}
