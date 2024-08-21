package cn.bugstack.domain.straetgy.repository;

import cn.bugstack.domain.straetgy.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.straetgy.model.entity.StrategyEntity;
import cn.bugstack.domain.straetgy.model.entity.StrategyRuleEntity;
import cn.bugstack.domain.straetgy.model.valobj.StrategyAwardRuleModelVo;

import java.util.List;
import java.util.Map;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */

public interface IStrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardListById(Long strategyId);

    void storeStrategyAwardSearchRateTable(String key, int size, Map<Integer, Long> shuffleStrategyAwardSearchRateTable);

    Integer getRateRange(String key);

    Long getStrategyAwardAssemble(String key, int nextInt);

    StrategyEntity queryStrategyByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRuleWeight(Long strategyId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, Long awardId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId,String ruleModel);

    StrategyAwardRuleModelVo queryStrategyRuleModels(Long strategyId, Long awardId);
}
