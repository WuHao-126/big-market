package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.Strategy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Mapper
public interface StrategyMapper extends BaseMapper<Strategy> {
    Strategy queryStrategyByStrategyId(Long strategyId);
}
