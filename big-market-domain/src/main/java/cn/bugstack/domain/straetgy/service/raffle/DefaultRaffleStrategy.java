package cn.bugstack.domain.straetgy.service.raffle;

import cn.bugstack.domain.straetgy.model.entity.RaffleRequestEntity;
import cn.bugstack.domain.straetgy.model.entity.RuleActionEntity;
import cn.bugstack.domain.straetgy.model.entity.RuleMatterEntity;
import cn.bugstack.domain.straetgy.model.valobj.RuleLogicCheckTypeVO;
import cn.bugstack.domain.straetgy.repository.IStrategyRepository;
import cn.bugstack.domain.straetgy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.straetgy.service.rule.chain.factory.DefaultChainFactory;
import cn.bugstack.domain.straetgy.service.rule.filter.ILogicFilter;
import cn.bugstack.domain.straetgy.service.rule.filter.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy{

    @Resource
    private DefaultLogicFactory logicFactory;

    @Resource
    private DefaultChainFactory defaultChainFactory;


    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch,DefaultChainFactory defaultChainFactory) {
        super(repository, strategyDispatch,defaultChainFactory);
    }
    @Override
    protected RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleRequestEntity raffleRequestEntity, String... logics) {
        if(logics == null || 0 == logics.length){
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }
        Map<String, ILogicFilter<RuleActionEntity.RaffleBeforeEntity>> logicFilterGroup = logicFactory.openLogicFilter();
        // 黑名单规则优先过滤
        String ruleBackList = Arrays.stream(logics)
                .filter(str -> str.contains(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
                .findFirst()
                .orElse(null);

        if (StringUtils.isNotBlank(ruleBackList)) {
            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> logicFilter = logicFilterGroup.get(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());
            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
            ruleMatterEntity.setUserId(raffleRequestEntity.getUserId());
            ruleMatterEntity.setAwardId(ruleMatterEntity.getAwardId());
            ruleMatterEntity.setStrategyId(raffleRequestEntity.getStrategyId());
            ruleMatterEntity.setRuleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());
            RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = logicFilter.filter(ruleMatterEntity);
            if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) {
                return ruleActionEntity;
            }
        }

        // 顺序过滤剩余规则
        List<String> ruleList = Arrays.stream(logics)
                .filter(s -> !s.equals(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
                .collect(Collectors.toList());

        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = null;
        for (String ruleModel : ruleList) {
            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> logicFilter = logicFilterGroup.get(ruleModel);
            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
            ruleMatterEntity.setUserId(raffleRequestEntity.getUserId());
            ruleMatterEntity.setAwardId(ruleMatterEntity.getAwardId());
            ruleMatterEntity.setStrategyId(raffleRequestEntity.getStrategyId());
            ruleMatterEntity.setRuleModel(ruleModel);
            ruleActionEntity = logicFilter.filter(ruleMatterEntity);
            // 非放行结果则顺序过滤
            log.info("抽奖前规则过滤 userId: {} ruleModel: {} code: {} info: {}", raffleRequestEntity.getUserId(), ruleModel, ruleActionEntity.getCode(), ruleActionEntity.getInfo());
            if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) return ruleActionEntity;
        }

        return ruleActionEntity;
    }

    @Override
    protected RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleRequestEntity raffleRequestEntity, String... logics) {
        if(logics == null || 0 == logics.length){
            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }
        Map<String, ILogicFilter<RuleActionEntity.RaffleCenterEntity>> logicFilterGroup = logicFactory.openLogicFilter();
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionEntity = null;
        for (String ruleModel : logics) {
            ILogicFilter<RuleActionEntity.RaffleCenterEntity> raffleBeforeEntityILogicFilter = logicFilterGroup.get(ruleModel);
            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
            ruleMatterEntity.setUserId(raffleRequestEntity.getUserId());
            ruleMatterEntity.setAwardId(raffleRequestEntity.getAwardId());
            ruleMatterEntity.setStrategyId(raffleRequestEntity.getStrategyId());
            ruleMatterEntity.setRuleModel(ruleModel);
            ruleActionEntity = raffleBeforeEntityILogicFilter.filter(ruleMatterEntity);

        }
        return ruleActionEntity;
    }
}
