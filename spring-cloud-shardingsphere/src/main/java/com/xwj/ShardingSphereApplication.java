package com.xwj;

import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 使用springboot配置方式分库分表(使用apache下的依赖)
 */
// 这里一定要排除这里的SpringBootConfiguration，因为我们已经自定义了DataSource，所以需要排除掉shardingjdbc设置的DataSource
@SpringBootApplication(exclude = SpringBootConfiguration.class)
public class ShardingSphereApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShardingSphereApplication.class, args);
	}

}
