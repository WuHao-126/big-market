package cn.bugstack.domain.straetgy.model.entity;

import cn.bugstack.types.common.Constants;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
public class StrategyEntity {
    /**
     * 策略ID
     */
    private Integer strategyId;

    /**
     * 策略描述
     */
    private String strategyDes;

    /**
     * 抽奖规则模型
     */
    private String ruleModels;

    public String[] ruleModels(){
        if (StringUtils.isEmpty(ruleModels)) return null;
        return ruleModels.split(",");
    }

    public String getRuleWeight(){
        String[] strings = ruleModels();
        if (strings==null || strings.length==0) return null;
        for (String ruleModel : strings) {
            if(Constants.RULE_WEIGHT.equals(ruleModel)) return ruleModel;
        }
        return null;
    }
}
