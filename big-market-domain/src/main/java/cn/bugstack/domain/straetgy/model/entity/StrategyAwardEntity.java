package cn.bugstack.domain.straetgy.model.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
public class StrategyAwardEntity {

    /**
     * 策略ID
     */
    private Long startegyId;

    /**
     * 奖品ID
     */
    private Long awardId;

    /**
     * 奖品库从总量
     */
    private Integer awardCount;

    /**
     * 奖品库从剩余
     */
    private Integer awardCountSurplus;

    /**
     * 奖品概率
     */
    private BigDecimal awardRate;
}
