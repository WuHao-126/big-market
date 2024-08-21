package cn.bugstack.domain.straetgy.service.raffle;

import cn.bugstack.domain.straetgy.model.entity.RaffleRequestEntity;
import cn.bugstack.domain.straetgy.model.entity.RaffleResponseEntity;
import cn.bugstack.domain.straetgy.model.entity.RuleActionEntity;
import cn.bugstack.domain.straetgy.model.valobj.RuleLogicCheckTypeVO;
import cn.bugstack.domain.straetgy.model.valobj.StrategyAwardRuleModelVo;
import cn.bugstack.domain.straetgy.repository.IStrategyRepository;
import cn.bugstack.domain.straetgy.service.IRaffleStrategy;
import cn.bugstack.domain.straetgy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.straetgy.service.rule.chain.ILogicChain;
import cn.bugstack.domain.straetgy.service.rule.chain.factory.DefaultChainFactory;
import cn.bugstack.types.exception.AppException;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public abstract   class AbstractRaffleStrategy implements IRaffleStrategy {

    // 策略仓储服务 -> domain层像一个大厨，仓储层提供米面粮油
    protected IStrategyRepository repository;
    // 策略调度服务 -> 只负责抽奖处理，通过新增接口的方式，隔离职责，不需要使用方关心或者调用抽奖的初始化
    protected IStrategyDispatch iStrategyDispatch;

    private DefaultChainFactory defaultChainFactory;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch) {
        this.repository = repository;
        this.iStrategyDispatch = strategyDispatch;
    }

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch,DefaultChainFactory defaultChainFactory) {
        this.repository = repository;
        this.iStrategyDispatch = strategyDispatch;
        this.defaultChainFactory = defaultChainFactory;
    }

    @Override
    public RaffleResponseEntity performRaffle(RaffleRequestEntity raffleRequestEntity) {
        Long userId = raffleRequestEntity.getUserId();
        Long strategyId = raffleRequestEntity.getStrategyId();
        if(userId == null || strategyId == null){
            throw new AppException("参数异常");
        }
//        StrategyEntity strategy = repository.queryStrategyByStrategyId(strategyId);
//        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = this.doCheckRaffleBeforeLogic(raffleRequestEntity, strategy.ruleModels());
//
//        if(RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionEntity.getCode())){
//            if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionEntity.getRuleModel())){
//                return RaffleResponseEntity.builder()
//                        .awardId(ruleActionEntity.getData().getAwardId())
//                        .build();
//            } else if(DefaultLogicFactory.LogicModel.RULE_WEIGHT.getCode().equals(ruleActionEntity.getRuleModel())){
//                String ruleWeightValueKey = ruleActionEntity.getData().getRuleWeightValueKey();
//                Long awardId = iStrategyDispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
//                return RaffleResponseEntity.builder()
//                        .awardId(awardId)
//                        .build();
//            }
//        }
        ILogicChain logicChain = defaultChainFactory.openLoginChain(userId, strategyId);
        Long awardId = logicChain.logic(userId, strategyId);

        StrategyAwardRuleModelVo ruleModelVo = repository.queryStrategyRuleModels(strategyId,awardId);
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> centerEntityRuleActionEntity = this.doCheckRaffleCenterLogic(raffleRequestEntity.builder()
                .userId(userId)
                .strategyId(strategyId)
                .awardId(awardId).build(),
                ruleModelVo.getRuleModelList());
        if(RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(centerEntityRuleActionEntity.getCode())){
            return RaffleResponseEntity.builder()
                    .awardDesc("兜底奖励")
                    .build();
        }

        return RaffleResponseEntity.builder()
                .awardId(awardId)
                .build();
    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleRequestEntity raffleRequestEntity, String... logics);

    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleRequestEntity raffleRequestEntity, String... logics);
}
