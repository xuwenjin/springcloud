package com.xwj.billcode.base;

/**
 * Twitter的snowflake移植到Java:
 * 
 * (a)、id构成(64位二进制，对应18~19位十进制): 1、42位的毫秒级时间 (1位最高位标识符 + 41位时间截差值(毫秒级))
 * 2、10位的节点标识(5位datacenterId和5位workerId) 3、12位的序列号(毫秒内的计数)，避免并发的数字
 * 
 * 注意这里进行了小改动: snowkflake是5位的datacenter加5位的机器id; 这里变成使用10位的机器id
 * 
 * (b) 对系统时间的依赖性非常强，需关闭ntp的时间同步功能。当检测到ntp时间调整后，将会拒绝分配id
 */
public class IdWorker {

	private final long workerId;
	private final long epoch = 1403854494756L; // 时间起始标记点，作为基准，一般取系统的最近时间
	private final long workerIdBits = 10L; // 机器标识位数
	private long sequence = 0L; // 0，并发控制
	private final long sequenceBits = 12L; // 毫秒内自增位

	private final long workerIdShift = this.sequenceBits; // 12
	private final long timestampLeftShift = this.sequenceBits + this.workerIdBits;// 22
	private final long sequenceMask = -1L ^ -1L << this.sequenceBits; // 4095,111111111111,12位
	private long lastTimestamp = -1L;

	private IdWorker(long workerId) {
		this.workerId = workerId;
	}

	public synchronized long nextId() throws Exception {
		long timestamp = this.timeGen();
		if (this.lastTimestamp == timestamp) {
			// 如果上一个timestamp与新产生的相等，则sequence加一(0-4095循环)；对新的timestamp，sequence从0开始
			this.sequence = this.sequence + 1 & this.sequenceMask;
			if (this.sequence == 0) {
				timestamp = this.tilNextMillis(this.lastTimestamp);// 重新生成timestamp
			}
		} else {
			this.sequence = 0;
		}

		if (timestamp < this.lastTimestamp) {
			throw new Exception(String.format("clock moved backwards.Refusing to generate id for %d milliseconds",
					(this.lastTimestamp - timestamp)));
		}

		this.lastTimestamp = timestamp;
		return timestamp - this.epoch << this.timestampLeftShift | this.workerId << this.workerIdShift | this.sequence;
	}

	private static IdWorker FLOWID_WORKER = new IdWorker(1);

	/**
	 * 单例模式
	 */
	public static IdWorker getFlowIdWorkerInstance() {
		return FLOWID_WORKER;
	}

	/**
	 * 等待下一个毫秒的到来, 保证返回的毫秒数在参数lastTimestamp之后
	 */
	private long tilNextMillis(long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	/**
	 * 获得系统当前毫秒数
	 */
	private long timeGen() {
		return System.currentTimeMillis();
	}

}
