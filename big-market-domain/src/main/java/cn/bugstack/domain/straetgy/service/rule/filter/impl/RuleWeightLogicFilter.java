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
import java.util.*;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Service
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_WEIGHT)
public class RuleWeightLogicFilter implements ILogicFilter {

    @Resource
    private IStrategyRepository iStrategyRepository;

    @Override
    public RuleActionEntity filter(RuleMatterEntity ruleMatterEntity) {
        Long userCode=4500l;
        String ruleValueString= iStrategyRepository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),ruleMatterEntity.getAwardId(),ruleMatterEntity.getRuleModel());
        Map<Long, String> ruleWeightMap = getRuleWeightMap(ruleValueString);
        if(null == ruleWeightMap || ruleWeightMap.isEmpty()){
            return RuleActionEntity.builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }
        List<Long> analyticalSortedKeys=new ArrayList<>(ruleWeightMap.keySet());
        Collections.sort(analyticalSortedKeys);
        Long nextValue = analyticalSortedKeys.stream()
                .filter(data -> data <= userCode)
                .findFirst()
                .orElse(null);
        if(nextValue!=null){
            return RuleActionEntity.builder()
                    .data(RuleActionEntity.RaffleBeforeEntity.builder()
                        .strategyId(ruleMatterEntity.getStrategyId())
                        .ruleWeightValueKey(ruleWeightMap.get(nextValue))
                                .build()
                    )
                    .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                    .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                    .build();
        }
        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }

    public Map<Long, String> getRuleWeightMap(String ruleValueString){
        String[] ruleValueGroups = ruleValueString.split(" ");
        Map<Long, String> ruleValueMap = new HashMap<>();
        for (String ruleValueKey : ruleValueGroups) {
            // 检查输入是否为空
            if (ruleValueKey == null || ruleValueKey.isEmpty()) {
                return ruleValueMap;
            }
            // 分割字符串以获取键和值
            String[] parts = ruleValueKey.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueKey);
            }
            ruleValueMap.put(Long.parseLong(parts[0]), ruleValueKey);
        }
        return ruleValueMap;
    }
}
