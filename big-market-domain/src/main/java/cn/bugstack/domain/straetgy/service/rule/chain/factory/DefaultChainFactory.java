package cn.bugstack.domain.straetgy.service.rule.chain.factory;

import cn.bugstack.domain.straetgy.model.entity.StrategyEntity;
import cn.bugstack.domain.straetgy.repository.IStrategyRepository;
import cn.bugstack.domain.straetgy.service.rule.chain.ILogicChain;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Service
public class DefaultChainFactory {

    private final Map<String, ILogicChain> logicChainGroup;

    private IStrategyRepository iStrategyRepository;

    public DefaultChainFactory(Map<String, ILogicChain> logicChainGroup, IStrategyRepository iStrategyRepository) {
        this.logicChainGroup = logicChainGroup;
        this.iStrategyRepository = iStrategyRepository;
    }

    public ILogicChain openLoginChain(Long userId,Long strategyId){
        StrategyEntity strategyEntity = iStrategyRepository.queryStrategyByStrategyId(strategyId);
        String[] ruleModels = strategyEntity.ruleModels();
        ILogicChain logicChain=logicChainGroup.get(ruleModels[0]);
        ILogicChain next = logicChain;
        for (int i = 1; i < ruleModels.length; i++) {
            ILogicChain nextLogicChain = logicChainGroup.get(ruleModels[i]);
            next.appendNext(nextLogicChain);
            next=nextLogicChain;
        }
        next.appendNext(logicChainGroup.get("default"));
        return logicChain;
    }
}
