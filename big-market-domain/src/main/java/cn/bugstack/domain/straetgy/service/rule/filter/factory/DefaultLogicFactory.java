package cn.bugstack.domain.straetgy.service.rule.filter.factory;

import cn.bugstack.domain.straetgy.model.annotation.LogicStrategy;
import cn.bugstack.domain.straetgy.model.entity.RuleActionEntity;
import cn.bugstack.domain.straetgy.service.rule.filter.ILogicFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Service
public class DefaultLogicFactory {
    public Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {
        logicFilters.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (null != strategy) {
                logicFilterMap.put(strategy.logicMode().getCode(), logic);
            }
        });
    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String, ILogicFilter<T>> openLogicFilter() {
        return (Map<String, ILogicFilter<T>>) (Map<?, ?>) logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_WEIGHT("rule_weight","【抽奖前规则】根据抽奖权重返回可抽奖范围KEY","before"),
        RULE_BLACKLIST("rule_blacklist","【抽奖前规则】黑名单规则过滤，命中黑名单则直接返回","before"),
        RULE_LOCK("rule_lock","【抽奖中规则】抽奖次数过滤，到达指定次数后则中奖","center"),
        RULE_LUCK_AWARD("rule_lock_award","【抽奖后置规则】抽奖库从没有，则指定返回幸运奖","after")
        ;

        private final String code;
        private final String info;
        private final String type;
    }

    public static Boolean isCenter(String code){
        return "center".equals(LogicModel.valueOf(code.toUpperCase()).type);
    }

    public static Boolean isAfter(String code){
        return "after".equals(LogicModel.valueOf(code.toUpperCase()).type);
    }
}
