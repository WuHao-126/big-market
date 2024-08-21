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
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_BLACKLIST)
public class RuleBlackListLogicFilter implements ILogicFilter {

    @Resource
    private IStrategyRepository iStrategyRepository;

    @Override
    public RuleActionEntity filter(RuleMatterEntity ruleMatterEntity) {
        Long userId = ruleMatterEntity.getUserId();
        String  ruleValues= iStrategyRepository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),ruleMatterEntity.getAwardId(),ruleMatterEntity.getRuleModel());
        //规则 100:user01,user02,user03  100为奖品ID
        String[] ruleValue = ruleValues.split(":");
        String[] blackUserIds = ruleValue[1].split(",");
        for (String blackUserId : blackUserIds) {
            if(blackUserId.equals(userId)){
                return RuleActionEntity.builder()
                        .data(RuleActionEntity.RaffleBeforeEntity.builder()
                                .strategyId(ruleMatterEntity.getStrategyId())
                                .awardId(Long.parseLong(ruleValue[0]))
                                .build()
                        )
                        .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                        .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                        .build();
            }
        }
        return RuleActionEntity.builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }
}
