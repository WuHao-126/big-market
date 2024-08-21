package cn.bugstack.domain.straetgy.service.rule.filter.impl;

import cn.bugstack.domain.straetgy.model.annotation.LogicStrategy;
import cn.bugstack.domain.straetgy.model.entity.RuleActionEntity;
import cn.bugstack.domain.straetgy.model.entity.RuleMatterEntity;
import cn.bugstack.domain.straetgy.model.valobj.RuleLogicCheckTypeVO;
import cn.bugstack.domain.straetgy.repository.IStrategyRepository;
import cn.bugstack.domain.straetgy.service.rule.filter.ILogicFilter;
import cn.bugstack.domain.straetgy.service.rule.filter.factory.DefaultLogicFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Service
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter {

    @Resource
    private IStrategyRepository iStrategyRepository;

    private Long userLotteryCount=2l;

    @Override
    public RuleActionEntity filter(RuleMatterEntity ruleMatterEntity) {
        //获取抽取次数
        String ruleValue = iStrategyRepository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(), ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        long raffleCount = Long.parseLong(ruleValue);
        if(userLotteryCount >= raffleCount){
            return RuleActionEntity.builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }
        return RuleActionEntity.builder()
                .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                .build();
    }
}
