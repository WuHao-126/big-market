package cn.bugstack.domain.straetgy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleRequestEntity {
    private Long userId;
    private Long strategyId;
    private Long awardId;
}
