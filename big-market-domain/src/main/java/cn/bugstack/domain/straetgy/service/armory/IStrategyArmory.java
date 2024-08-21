package cn.bugstack.domain.straetgy.service.armory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public interface IStrategyArmory{

    Boolean assembleLotteryStrategy(Long strategyId);


}
