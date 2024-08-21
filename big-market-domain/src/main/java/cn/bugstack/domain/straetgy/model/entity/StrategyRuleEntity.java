package cn.bugstack.domain.straetgy.model.entity;

import cn.bugstack.types.common.Constants;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
public class StrategyRuleEntity {
    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 奖品ID
     */
    private Long awardId;

    /**
     * 抽奖规则类型
     */
    private Integer ruleType;

    /**
     * 抽奖规则类型
     */
    private String ruleModel;

    /**
     * 抽奖规则比值
     */
    private String ruleValue;

    /**
     * 抽奖规则描述
     */
    private String ruleDesc;


    public Map<String,List<Long>> getRuleValue(){
        if(!Constants.RULE_WEIGHT.equals(ruleModel)) return null;
        String[] ruleValueGroups = this.ruleValue.split(" ");
        Map<String,List<Long>> map=new HashMap<>();
        for (String ruleValueGroup : ruleValueGroups) {
            String[] ruleValue = ruleValueGroup.split(":");
            if(ruleValue.length!=2){
                throw new RuntimeException("权重格式不正确");
            }
            String key = ruleValue[0];
            String[] valueStrings = ruleValue[1].split(",");
            List<Long> values = new ArrayList<>();
            for (String value : valueStrings) {
                values.add(Long.parseLong(value));
            }
            map.put(key,values);
        }
        return map;
    }
}
