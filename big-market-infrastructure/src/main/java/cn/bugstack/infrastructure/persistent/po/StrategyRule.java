package cn.bugstack.infrastructure.persistent.po;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName straetgy_rule
 */

@Data
public class StrategyRule implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 奖品ID
     */
    private Long awardId;

    /**
     * 抽奖规则类型
     */
    private Integer ruleType;

    /**
     * 抽奖规则类型
     */
    private String ruleModel;

    /**
     * 抽奖规则比值
     */
    private String ruleValue;

    /**
     * 抽奖规则描述
     */
    private String ruleDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


    private static final long serialVersionUID = 1L;

}