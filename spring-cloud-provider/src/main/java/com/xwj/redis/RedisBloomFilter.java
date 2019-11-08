//package com.xwj.redis;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.google.common.hash.BloomFilter;
//
//import lombok.Getter;
//import lombok.Setter;
//
//@Component
//public class RedisBloomFilter {
//
//	/** 预计插入量 */
//	private int expectedInsertions;
//
//	/** 可接受的错误率 */
//	private int fpp;
//	
//	/** 可接受的错误率 */
//	 private final int numHashFunctions;
//
//	@Autowired
//	private JsonRedisTemplate redisTemplate;
//
//	public RedisBloomFilter create(int expectedInsertions, int fpp) {
//		long numBits = optimalNumOfBits(expectedInsertions, fpp);
//		int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
//
//	}
//
//	private long optimalNumOfBits(long n, double p) {
//		if (p == 0) {
//			p = Double.MIN_VALUE;
//		}
//		return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
//	}
//
//	private int optimalNumOfHashFunctions(long n, long m) {
//		return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
//	}
//
//	public int getExpectedInsertions() {
//		return expectedInsertions;
//	}
//
//	public void setExpectedInsertions(int expectedInsertions) {
//		this.expectedInsertions = expectedInsertions;
//	}
//
//	public int getFpp() {
//		return fpp;
//	}
//
//	public void setFpp(int fpp) {
//		this.fpp = fpp;
//	}
//
//}
