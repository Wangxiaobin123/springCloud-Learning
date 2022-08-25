package flink.mf.entity;

import org.apache.flink.api.common.functions.RichMapFunction;

/**
 * @author: wangshengbin
 * @date: 2020/12/12 下午6:23
 */
public class Employee extends RichMapFunction {
    private static final long serialVersionUID = -3563513226707530624L;
    public int salary;

    @Override
    public Object map(Object value) throws Exception {
        return value;
    }
}
