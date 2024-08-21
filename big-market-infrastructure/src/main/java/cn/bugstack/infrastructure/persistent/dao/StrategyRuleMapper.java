package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.domain.straetgy.model.valobj.StrategyAwardRuleModelVo;
import cn.bugstack.infrastructure.persistent.po.StrategyRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Mapper
public interface StrategyRuleMapper extends BaseMapper<StrategyRule> {
    StrategyRule queryStrategyRuleWeight(@Param("strategyId") Long strategyId, @Param("ruleModel") String ruleModel);

    String queryStrategyRuleValue(@Param("strategyId")Long strategyId,@Param("awardId")Long awardId,@Param("ruleModel")String ruleModel);

    StrategyAwardRuleModelVo queryStrategyRuleModels(@Param("strategyId") Long strategyId,@Param("awardId") Long awardId);
}
