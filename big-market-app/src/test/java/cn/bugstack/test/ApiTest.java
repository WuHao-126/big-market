package cn.bugstack.test;

import cn.bugstack.domain.straetgy.model.entity.RaffleRequestEntity;
import cn.bugstack.domain.straetgy.model.entity.RaffleResponseEntity;
import cn.bugstack.domain.straetgy.model.entity.StrategyEntity;
import cn.bugstack.domain.straetgy.repository.IStrategyRepository;
import cn.bugstack.domain.straetgy.service.IRaffleStrategy;
import cn.bugstack.domain.straetgy.service.armory.StrategyArmoryDispath;
import cn.bugstack.infrastructure.persistent.dao.AwardMapper;
import cn.bugstack.infrastructure.persistent.dao.StrategyAwardMapper;
import cn.bugstack.infrastructure.persistent.dao.StrategyMapper;
import cn.bugstack.infrastructure.persistent.dao.StrategyRuleMapper;
import cn.bugstack.infrastructure.persistent.po.StrategyAward;
import cn.bugstack.types.common.Constants;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Autowired
    StrategyMapper strategyMapper;

    @Autowired
    AwardMapper awardMapper;

    @Autowired
    StrategyAwardMapper strategyAwardMapper;

    @Autowired
    StrategyRuleMapper strategyRuleMapper;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    IStrategyRepository iStrategyRepository;

    @Autowired
    StrategyArmoryDispath strategyArmoryDispath;

    @Autowired
    IRaffleStrategy iRaffleStrategy;


    @Test
    public void test() {
        Long randomAwardId = strategyArmoryDispath.getRandomAwardId(10001l, "4000");
        System.out.println(randomAwardId);
    }
    @Test
    public void testStrategyMapper(){
        strategyArmoryDispath.assembleLotteryStrategy(10001l);
    }

    @Test
    public void testIraffleStrategy(){
        RaffleRequestEntity raffleRequestEntity=new RaffleRequestEntity();
        raffleRequestEntity.setStrategyId(10001l);
        raffleRequestEntity.setUserId(101l);
        RaffleResponseEntity raffleResponseEntity = iRaffleStrategy.performRaffle(raffleRequestEntity);
        System.out.println(raffleResponseEntity);

    }

    @Test()
    public void aaa(){
        StrategyEntity strategyEntity = iStrategyRepository.queryStrategyByStrategyId(10001l);

    }



    @Test
    public void test1(){
        Map<String, AtomicInteger> map=new HashMap<>();
        for (int i = 0; i < 100; i++) {
            int s = new Random().nextInt(1000000);
            Object o = redissonClient.getMap(Constants.STRAETGY_RATE_TABLE_KEY + 10001l).get(s);
            long l = Long.parseLong(o + "");
            StrategyAward award_id = strategyAwardMapper.selectOne(new QueryWrapper<StrategyAward>().eq("award_id", l));
            map.computeIfAbsent(award_id.getAwardTitle(), k -> new AtomicInteger(0)).incrementAndGet();
        }
        Set<String> strings = map.keySet();
        for (String string : strings) {
            System.out.println(string +"获得了："+ map.get(string).get()+"次");
        }
    }


}
