package cn.bugstack.domain.straetgy.service.armory;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: 策略抽奖调度
 */
public interface IStrategyDispatch {
    /**
     * 获取抽奖策略随机抽取
     * @param strategyId
     * @return
     */
    Long getRandomAwardId(Long strategyId);

    Long getRandomAwardId(Long strategyId,String ruleWeightValue);
}
