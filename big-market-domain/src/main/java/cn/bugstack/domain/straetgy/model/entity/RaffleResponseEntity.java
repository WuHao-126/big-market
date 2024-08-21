package cn.bugstack.domain.straetgy.model.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
@Builder
public class RaffleResponseEntity {
    /** 策略ID */
    private Long strategyId;
    /** 奖品ID */
    private Long awardId;
    /** 奖品对接标识 - 每一个都是一个对应的发奖策略 */
    private String awardKey;
    /** 奖品配置信息 */
    private String awardConfig;
    /** 奖品内容描述 */
    private String awardDesc;
}
