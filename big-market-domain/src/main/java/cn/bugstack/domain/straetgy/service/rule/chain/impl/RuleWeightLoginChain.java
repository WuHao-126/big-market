package cn.bugstack.domain.straetgy.service.rule.chain.impl;

import cn.bugstack.domain.straetgy.repository.IStrategyRepository;
import cn.bugstack.domain.straetgy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.straetgy.service.rule.chain.factory.AbstractLogicChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Slf4j
@Component("rule_weight")
public class RuleWeightLoginChain extends AbstractLogicChain {

    @Resource
    private IStrategyDispatch iStrategyDispatch;

    @Resource
    private IStrategyRepository iStrategyRepository;

    private static Long userCode=6000l;

    @Override
    public Long logic(Long userId, Long strategyId) {
        String ruleValueString= iStrategyRepository.queryStrategyRuleValue(strategyId,getRuleModel());
        Map<Long, String> ruleWeightMap = getRuleWeightMap(ruleValueString);
        if(null == ruleWeightMap || ruleWeightMap.isEmpty()){
            return null;
        }
        List<Long> analyticalSortedKeys=new ArrayList<>(ruleWeightMap.keySet());
        Long nextValue = analyticalSortedKeys.stream()
                .sorted(Comparator.reverseOrder())
                .filter(data -> data <= userCode)
                .findFirst()
                .orElse(null);
        if(nextValue!=null){
            //TODO
            log.info("抽奖责任链--权重接管 userId:{} strategyId:{} reuleModel:{}",userId,strategyId,getRuleModel());
            //4000:101,102,103,104......
            String[] split = ruleWeightMap.get(nextValue).split(":");
            return iStrategyDispatch.getRandomAwardId(strategyId,split[0]);
        }
        return next().logic(userId,strategyId);
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

    @Override
    public String getRuleModel() {
        return "rule_weight";
    }
}
