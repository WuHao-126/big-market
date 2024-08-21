package cn.bugstack.domain.straetgy.service.armory;

import cn.bugstack.domain.straetgy.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.straetgy.model.entity.StrategyEntity;
import cn.bugstack.domain.straetgy.model.entity.StrategyRuleEntity;
import cn.bugstack.domain.straetgy.repository.IStrategyRepository;
import cn.bugstack.types.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Service
public class StrategyArmoryDispath implements IStrategyArmory,IStrategyDispatch{

    @Resource
    private IStrategyRepository strategyAwardRepository;


    @Override
    public Boolean assembleLotteryStrategy(Long strategyId) {
        List<StrategyAwardEntity> strategyAwardEntities = strategyAwardRepository.queryStrategyAwardListById(strategyId);
        //进行初始化
        assembleLotteryStrategy(String.valueOf(strategyId),strategyAwardEntities);
        //查询策略是否有权重
        StrategyEntity strategyEntity = strategyAwardRepository.queryStrategyByStrategyId(strategyId);
        //没有配置
        if(strategyEntity == null || StringUtils.isEmpty(strategyEntity.getRuleModels())) return true;
        //有权重 TODO 有问题
        String ruleWeight = strategyEntity.getRuleWeight();
        StrategyRuleEntity strategyRuleEntity = strategyAwardRepository.queryStrategyRuleWeight(strategyId,ruleWeight);
        if(null == strategyRuleEntity)  throw  new RuntimeException("配置异常");
        Map<String, List<Long>> ruleValueMap = strategyRuleEntity.getRuleValue();
        Set<String> keys = ruleValueMap.keySet();
        for (String key : keys) {
            List<Long> list = ruleValueMap.get(key);
            List<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            strategyAwardEntitiesClone.removeIf(entity -> !list.contains(entity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat("_").concat(key),strategyAwardEntitiesClone);
        }
        return true;
    }

    public void assembleLotteryStrategy(String key,List<StrategyAwardEntity> strategyAwardEntities){
        BigDecimal bigDecimal = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        //获取总概率值
        BigDecimal reduce = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //获取范围值
        BigDecimal rateRange  = reduce.divide(bigDecimal, 0, RoundingMode.CEILING);
        List<Long> strategyAwardSearchRateTables=new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Long awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();
            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchRateTables.add(strategyAwardEntity.getAwardId());
            }
        }
        //进行乱序
        // 6. 对存储的奖品进行乱序操作
        Collections.shuffle(strategyAwardSearchRateTables);

        // 7. 生成出Map集合，key值，对应的就是后续的概率值。通过概率来获得对应的奖品ID
        Map<Integer, Long> shuffleStrategyAwardSearchRateTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffleStrategyAwardSearchRateTable.put(i, strategyAwardSearchRateTables.get(i));
        }

        // 8. 存放到 Redis
        strategyAwardRepository.storeStrategyAwardSearchRateTable(key, shuffleStrategyAwardSearchRateTable.size(), shuffleStrategyAwardSearchRateTable);
    }


    @Override
    public Long getRandomAwardId(Long strategyId) {
        //获取范围
        Integer rateRange = strategyAwardRepository.getRateRange(String.valueOf(strategyId));
        return strategyAwardRepository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Long getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        Integer rateRange = strategyAwardRepository.getRateRange(key);
        return strategyAwardRepository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }
}
