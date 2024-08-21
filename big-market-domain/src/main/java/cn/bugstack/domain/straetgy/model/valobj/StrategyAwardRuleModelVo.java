package cn.bugstack.domain.straetgy.model.valobj;

import cn.bugstack.domain.straetgy.service.rule.filter.factory.DefaultLogicFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrategyAwardRuleModelVo {

    private String ruleModels;

    public String[] getRuleModelList() {
        List<String> list=new ArrayList<>();
        String[] split = ruleModels.split(",");
        for (String s : split) {
            Boolean center = DefaultLogicFactory.isCenter(s);
            if (center){
                list.add(s);
            }
        }
        return list.toArray(new String[0]);
    }
}
