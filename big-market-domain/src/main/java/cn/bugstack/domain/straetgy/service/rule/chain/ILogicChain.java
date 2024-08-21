package cn.bugstack.domain.straetgy.service.rule.chain;

import cn.bugstack.domain.straetgy.service.rule.chain.ILogicChainArmory;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: 过滤责任链接口
 */
public interface ILogicChain extends ILogicChainArmory {
    /**
     * 抽奖接口
     * @param strategyId
     * @param userId
     * @return
     */
    Long logic(Long userId,Long strategyId);

}
