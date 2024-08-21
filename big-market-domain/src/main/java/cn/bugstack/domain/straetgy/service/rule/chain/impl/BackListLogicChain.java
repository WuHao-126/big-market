package cn.bugstack.domain.straetgy.service.rule.chain.impl;

import cn.bugstack.domain.straetgy.repository.IStrategyRepository;
import cn.bugstack.domain.straetgy.service.rule.chain.factory.AbstractLogicChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Slf4j
@Component("rule_blacklist")
public class BackListLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository iStrategyRepository;

    @Override
    public Long logic(Long userId, Long strategyId) {
        log.info("抽奖责任链-黑名单开始 userId:{} strategyId:{} reuleModel:{}",userId,strategyId,getRuleModel());
        String  ruleValues= iStrategyRepository.queryStrategyRuleValue(strategyId,getRuleModel());
        //规则 100:user01,user02,user03  100为奖品ID
        String[] ruleValue = ruleValues.split(":");
        String[] blackUserIds = ruleValue[1].split(",");
        Long awardId = Long.parseLong(ruleValue[0]);
        for (String blackUserId : blackUserIds) {
            if(blackUserId.equals(userId)){
                log.info("抽奖责任链-黑名单接管 userId:{} strategyId:{} reuleModel:{}",userId,strategyId,getRuleModel());
                return awardId;
            }
        }
        log.info("抽奖责任链-黑名单放行 userId:{} strategyId:{} reuleModel:{}",userId,strategyId,getRuleModel());
        return next().logic(userId,strategyId);
    }


    @Override
    public String getRuleModel() {
        return "rule_blacklist";
    }
}
