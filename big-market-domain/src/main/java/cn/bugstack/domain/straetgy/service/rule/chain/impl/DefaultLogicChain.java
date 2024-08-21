package cn.bugstack.domain.straetgy.service.rule.chain.impl;

import cn.bugstack.domain.straetgy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.straetgy.service.rule.chain.factory.AbstractLogicChain;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Component("default")
public class DefaultLogicChain extends AbstractLogicChain {

    @Resource
    IStrategyDispatch iStrategyDispatch;

    @Override
    public Long logic(Long userId, Long strategyId) {
        return iStrategyDispatch.getRandomAwardId(strategyId);
    }

    @Override
    public String getRuleModel() {
        return "default";
    }
}
