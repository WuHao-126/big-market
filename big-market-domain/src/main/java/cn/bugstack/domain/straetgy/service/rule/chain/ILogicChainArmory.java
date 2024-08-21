package cn.bugstack.domain.straetgy.service.rule.chain;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public interface ILogicChainArmory {
    /**
     * 添加责任链接口
     * @param next
     * @return
     */
    ILogicChain appendNext(ILogicChain next);

    /**
     * 获取该节点后的责任链数据
     * @return
     */
    ILogicChain next();
}
