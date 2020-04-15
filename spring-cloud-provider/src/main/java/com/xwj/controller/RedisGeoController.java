package com.xwj.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.service.RedisGeoService;
import com.xwj.utils.CommonUtil;

/**
 * 测试redis
 */
@RestController
@RequestMapping("redisGeo")
public class RedisGeoController {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private RedisGeoService redisGeoService;

	/**
	 * 使用redis+GEO，计算两个坐标点的距离
	 */
	@GetMapping("getDistance")
	public void getDistance() {
		GeoOperations<String, String> ops = redisTemplate.opsForGeo();

		Set<RedisGeoCommands.GeoLocation<String>> locations = new HashSet<>();

		locations.add(new RedisGeoCommands.GeoLocation<String>("DFYL", new Point(114.366384, 30.4082)));
		locations.add(new RedisGeoCommands.GeoLocation<String>("WUDX", new Point(114.365248, 30.53786)));

		// 放入两个坐标：东方雨林-武汉大学
		Map<String, Point> pointMap = new HashMap<>();
		pointMap.put("DFYL", new Point(114.366384, 30.4082));
		pointMap.put("WUDX", new Point(114.365248, 30.53786));
		ops.add("GET_DISTANCE", locations);

		Distance distance = ops.distance("GET_DISTANCE", "DFYL", "WUDX", RedisGeoCommands.DistanceUnit.METERS);
		System.out.println("间隔距离：" + distance.getValue());
	}

	/**
	 * 使用redis+GEO，计算不同的值
	 */
	@GetMapping("test")
	public void test() {
		redisGeoService.addGeoPoin(new Point(114.366384, 30.4082), "东方雨林");
		redisGeoService.addGeoPoin(new Point(114.365282, 30.406869), "怡景江南");
		redisGeoService.addGeoPoin(new Point(114.368047, 30.412896), "梅南山居");
		redisGeoService.addGeoPoin(new Point(114.365248, 30.53786), "武汉大学");

		List<Point> points = redisGeoService.geoGet("东方雨林", "武汉大学");
		System.out.println("坐标点：" + points);

		Distance distance = redisGeoService.geoDist("东方雨林", "武汉大学", RedisGeoCommands.DistanceUnit.KILOMETERS);
		System.out.println("两点坐标间隔公里数：" + distance.getValue() + distance.getUnit());

		Circle circle = new Circle(114.366384, 30.4082, Metrics.KILOMETERS.getMultiplier());
		GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisGeoService.nearByXY(circle, 5);
		System.out.println("根据给定的经纬度，查询附近的坐标点位：" + results);

		Distance centerDistance = new Distance(5, Metrics.KILOMETERS);
		results = redisGeoService.nearByPlace("东方雨林", centerDistance, 5);
		System.out.println("根据指定地点，查询附近5km以内的坐标点位：:" + results);

		redisGeoService.geoHash("东方雨林");
		System.out.println("返回指定位置hash值：" + results);
	}

	/**
	 * 使用redis+GEO，上报司机位置
	 */
	@PostMapping("addDriverPosition")
	public Long addDriverPosition(String cityId, String driverId, Double lng, Double lat) {
		String redisKey = CommonUtil.buildRedisKey("geo_key", cityId);
		Long addnum = redisGeoService.addGeoPoin(redisKey, new Point(lng, lat), driverId);

		List<Point> points = redisGeoService.geoGetByKey(redisKey, driverId);
		System.out.println("坐标点：" + points);

		return addnum;
	}

	/**
	 * 使用redis+GEO，查询附近司机位置
	 */
	@GetMapping("getNearDrivers")
	public GeoResults<RedisGeoCommands.GeoLocation<String>> getNearDrivers(String cityId, Double lng, Double lat) {
		String redisKey = CommonUtil.buildRedisKey("geo_key", cityId);

		Circle circle = new Circle(lng, lat, Metrics.KILOMETERS.getMultiplier());
		GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisGeoService.nearByXY(redisKey, circle, 5);
		System.out.println("查询附近司机位置：" + results);

		return results;
	}

}
