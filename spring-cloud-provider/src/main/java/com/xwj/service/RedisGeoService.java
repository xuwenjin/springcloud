package com.xwj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisGeoService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	private final String CITY_GEO_KEY = "geo_key";

	/**
	 * 添加经纬度信息
	 * 
	 * redis 命令：geoadd cityGeo 116.405285 39.904989 "北京"
	 */
	public Long addGeoPoin(String key, Point point, String member) {
		if (redisTemplate.hasKey(key)) {
			redisTemplate.opsForGeo().remove(key, member);
		}
		return redisTemplate.opsForGeo().add(key, point, member);
	}

	/**
	 * 添加经纬度信息
	 * 
	 * redis 命令：geoadd cityGeo 116.405285 39.904989 "北京"
	 */
	public Long addGeoPoin(Point point, String member) {
		return addGeoPoin(CITY_GEO_KEY, point, member);
	}

	/**
	 * 查找指定key的经纬度信息，可以指定多个member，批量返回
	 * 
	 * redis命令：geopos cityGeo 北京
	 */
	public List<Point> geoGet(String... members) {
		return geoGetByKey(CITY_GEO_KEY, members);
	}

	/**
	 * 查找指定key的经纬度信息，可以指定多个member，批量返回
	 * 
	 * redis命令：geopos cityGeo 北京
	 */
	public List<Point> geoGetByKey(String key, String... members) {
		return redisTemplate.opsForGeo().position(key, members);
	}

	/**
	 * 返回两个地方的距离，可以指定单位，比如米m，千米km，英里mi，英尺ft
	 * 
	 * redis命令：geodist cityGeo 北京 上海
	 */
	public Distance geoDist(String member1, String member2, Metric metric) {
		Distance distance = redisTemplate.opsForGeo().distance(CITY_GEO_KEY, member1, member2, metric);
		return distance;
	}

	/**
	 * 根据给定的经纬度，返回半径不超过指定距离的元素
	 * 
	 * @param count
	 *            限定返回的个数
	 * 
	 *            redis命令：georadius cityGeo 116.405285 39.904989 100 km WITHDIST
	 *            WITHCOORD ASC COUNT 5
	 */
	public GeoResults<RedisGeoCommands.GeoLocation<String>> nearByXY(Circle circle, long count) {
		return nearByXY(CITY_GEO_KEY, circle, count);
	}

	/**
	 * 根据给定的经纬度，返回半径不超过指定距离的元素
	 * 
	 * @param count
	 *            限定返回的个数
	 * 
	 *            redis命令：georadius cityGeo 116.405285 39.904989 100 km WITHDIST
	 *            WITHCOORD ASC COUNT 5
	 */
	public GeoResults<RedisGeoCommands.GeoLocation<String>> nearByXY(String key, Circle circle, long count) {
		// includeDistance 包含距离
		// includeCoordinates 包含经纬度
		// sortAscending 正序排序
		// limit 限定返回的记录数
		RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
				.includeDistance().includeCoordinates().sortAscending().limit(count);
		return redisTemplate.opsForGeo().radius(key, circle, args);
	}

	/**
	 * 根据指定的地点查询半径在指定范围内的位置
	 * 
	 * redis命令：georadiusbymember cityGeo 北京 100 km WITHDIST WITHCOORD ASC COUNT
	 * 5
	 */
	public GeoResults<RedisGeoCommands.GeoLocation<String>> nearByPlace(String member, Distance distance, long count) {
		// includeDistance 包含距离
		// includeCoordinates 包含经纬度
		// sortAscending 正序排序
		// limit 限定返回的记录数
		RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
				.includeDistance().includeCoordinates().sortAscending().limit(count);
		GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo().radius(CITY_GEO_KEY,
				member, distance, args);
		return results;
	}

	/**
	 * 返回的是geohash值
	 * 
	 * redis命令：geohash cityGeo 北京
	 */
	public List<String> geoHash(String member) {
		List<String> results = redisTemplate.opsForGeo().hash(CITY_GEO_KEY, member);
		return results;
	}

}
