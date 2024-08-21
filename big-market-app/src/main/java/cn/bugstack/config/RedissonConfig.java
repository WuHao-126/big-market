package cn.bugstack.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClientConfig(){
        Config config = new Config();
        config.setCodec(JsonJacksonCodec.INSTANCE);
        config.useSingleServer()
                .setAddress("redis://localhost:6379") // Redis 服务器地址
                .setDatabase(3) // Redis 数据库编号
                .setPassword("12345678") // Redis 密码（如果有的话）
                .setConnectionPoolSize(64)
                .setConnectionMinimumIdleSize(24)
                .setTimeout(3000)
                .setRetryAttempts(3)
                .setRetryInterval(1500);
        return Redisson.create(config);
    }
}
