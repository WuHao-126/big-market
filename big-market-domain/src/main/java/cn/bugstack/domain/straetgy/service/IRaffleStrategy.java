package cn.bugstack.domain.straetgy.service;

import cn.bugstack.domain.straetgy.model.entity.RaffleRequestEntity;
import cn.bugstack.domain.straetgy.model.entity.RaffleResponseEntity;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public interface IRaffleStrategy {
    RaffleResponseEntity performRaffle(RaffleRequestEntity raffleRequestEntity);
}
