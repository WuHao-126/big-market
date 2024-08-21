package cn.bugstack.domain.straetgy.service.rule.chain.factory;

import cn.bugstack.domain.straetgy.service.rule.chain.ILogicChain;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public abstract class AbstractLogicChain implements ILogicChain {

    private ILogicChain next;


    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next=next;
        return next;
    }

    @Override
    public ILogicChain next() {
        return next;
    }

    public abstract String getRuleModel();
}
