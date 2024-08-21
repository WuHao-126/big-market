package cn.bugstack.test;

import cn.bugstack.types.common.Constants;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public class Main {

    @Test
    public void aaa(){
        Random random=new Random();
        int i = random.nextInt(1000000);
        Config config = new Config();
        config.setCodec(JsonJacksonCodec.INSTANCE);
        config.useSingleServer()
                .setAddress("redis://localhost:6379") // Redis 服务器地址
                .setDatabase(0) // Redis 数据库编号
                .setPassword("12345678") // Redis 密码（如果有的话）
                .setConnectionPoolSize(64)
                .setConnectionMinimumIdleSize(24)
                .setTimeout(3000)
                .setRetryAttempts(3)
                .setRetryInterval(1500);
        RedissonClient redissonClient = Redisson.create(config);
        Object o = redissonClient.getMap(Constants.STRAETGY_RATE_TABLE_KEY + 10001l).get(i);
        System.out.println(o);
    }
}
