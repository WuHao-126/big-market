package cn.bugstack.infrastructure.persistent.repository;

import cn.bugstack.domain.straetgy.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.straetgy.model.entity.StrategyEntity;
import cn.bugstack.domain.straetgy.model.entity.StrategyRuleEntity;
import cn.bugstack.domain.straetgy.model.valobj.StrategyAwardRuleModelVo;
import cn.bugstack.domain.straetgy.repository.IStrategyRepository;
import cn.bugstack.infrastructure.persistent.dao.StrategyAwardMapper;
import cn.bugstack.infrastructure.persistent.dao.StrategyMapper;
import cn.bugstack.infrastructure.persistent.dao.StrategyRuleMapper;
import cn.bugstack.infrastructure.persistent.po.Strategy;
import cn.bugstack.infrastructure.persistent.po.StrategyAward;
import cn.bugstack.infrastructure.persistent.po.StrategyRule;
import cn.bugstack.infrastructure.persistent.redis.RedissonService;
import cn.bugstack.types.common.Constants;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.redisson.api.RBatch;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Component
public class StrategyAwardRepository implements IStrategyRepository {

    @Resource
    private RedissonService redissonService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private StrategyAwardMapper strategyAwardMapper;
    @Resource
    private StrategyMapper strategyMapper;
    @Resource
    private StrategyRuleMapper strategyRuleMapper;


    @Override
    public List<StrategyAwardEntity> queryStrategyAwardListById(Long strategyId) {
        String key=Constants.STRAETGY_AWARD_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = redissonClient.<List<StrategyAwardEntity>>getBucket(key).get();
        if(strategyAwardEntities != null && !strategyAwardEntities.isEmpty()){
            return strategyAwardEntities;
        }
//        List<StrategyAward> straetgyAwards = straetgyAwardMapper.queryStraetgyAwardListByStrategyId(strategyId);
        List<StrategyAward> straetgyAwards = strategyAwardMapper.selectList(new QueryWrapper<StrategyAward>().eq("strategy_id", strategyId));
        strategyAwardEntities=new ArrayList<StrategyAwardEntity>();
        for (StrategyAward award : straetgyAwards) {
            StrategyAwardEntity strategyAwardEntity=new StrategyAwardEntity();
            BeanUtils.copyProperties(award,strategyAwardEntity);
            strategyAwardEntities.add(strategyAwardEntity);
        }
        redissonClient.getBucket(key).set(strategyAwardEntities);
        return strategyAwardEntities;
    }

    @Override
    public void storeStrategyAwardSearchRateTable(String key, int size, Map<Integer, Long> shuffleStrategyAwardSearchRateTable) {
        redissonService.setValue(Constants.STRATEGY_RATE_RANGE_KEY + key, size);
        // 2. 存储概率查找表
        RBatch batch = redissonClient.createBatch();
        for (Map.Entry<Integer, Long> entry : shuffleStrategyAwardSearchRateTable.entrySet()) {
            batch.getMap(Constants.STRAETGY_RATE_TABLE_KEY + key).fastPutAsync(entry.getKey(), entry.getValue());
        }
        // 执行批量操作
        batch.execute();
    }

    @Override
    public StrategyEntity queryStrategyByStrategyId(Long strategyId) {
        String cacheKey = Constants.STRATEGY_KEY+strategyId;
        StrategyEntity strategyEntity = redissonService.getValue(cacheKey);
        if(null != strategyEntity) return strategyEntity;
        Strategy strategy = strategyMapper.queryStrategyByStrategyId(strategyId);
        strategyEntity=new StrategyEntity();
        BeanUtils.copyProperties(strategy,strategyEntity);
        redissonService.setValue(cacheKey,strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRuleWeight(Long strategyId, String ruleModel) {
        StrategyRule strategyRule=strategyRuleMapper.queryStrategyRuleWeight(strategyId,ruleModel);
        StrategyRuleEntity strategyRuleEntity=new StrategyRuleEntity();
        BeanUtils.copyProperties(strategyRule,strategyRuleEntity);
        return strategyRuleEntity;
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, Long awardId, String ruleModel) {
        return strategyRuleMapper.queryStrategyRuleValue(strategyId,awardId,ruleModel);
    }
    @Override
    public String queryStrategyRuleValue(Long strategyId, String ruleModel) {
        return strategyRuleMapper.queryStrategyRuleValue(strategyId,null,ruleModel);
    }

    @Override
    public StrategyAwardRuleModelVo queryStrategyRuleModels(Long strategyId, Long awardId) {
        return strategyRuleMapper.queryStrategyRuleModels(strategyId,awardId);
    }

    @Override
    public Integer getRateRange(String key) {
        return redissonService.getValue(Constants.STRATEGY_RATE_RANGE_KEY+key);
    }

    @Override
    public Long getStrategyAwardAssemble(String key, int nextInt) {
        return redissonService.getFromMap(Constants.STRAETGY_RATE_TABLE_KEY+key,nextInt);
    }

}
