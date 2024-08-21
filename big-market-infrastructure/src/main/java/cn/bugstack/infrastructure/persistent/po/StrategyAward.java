package cn.bugstack.infrastructure.persistent.po;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName straetgy_award
 */
@Data
public class StrategyAward implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 奖品ID
     */
    private Long awardId;

    /**
     * 奖品标题
     */
    private String awardTitle;

    /**
     * 奖品副ID
     */
    private String awardSuptitle;

    /**
     * 奖品概率
     */
    private BigDecimal awardRate;

    /**
     * 奖品库存
     */
    private Integer awardCount;

    /**
     * 奖品剩余库从
     */
    private Integer awardCountSurplus;

    /**
     * 抽奖类型
     */
    private String ruleModels;

    /**
     * 奖品排序
     */
    private Integer sort;

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