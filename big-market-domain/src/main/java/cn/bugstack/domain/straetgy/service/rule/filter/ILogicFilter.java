package cn.bugstack.domain.straetgy.service.rule.filter;

import cn.bugstack.domain.straetgy.model.entity.RuleActionEntity;
import cn.bugstack.domain.straetgy.model.entity.RuleMatterEntity;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity>  {
    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}
