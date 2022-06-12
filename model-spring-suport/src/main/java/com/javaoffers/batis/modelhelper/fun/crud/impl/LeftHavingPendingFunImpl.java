package com.javaoffers.batis.modelhelper.fun.crud.impl;

import com.javaoffers.batis.modelhelper.fun.Condition;
import com.javaoffers.batis.modelhelper.fun.ConditionTag;
import com.javaoffers.batis.modelhelper.fun.GGetterFun;
import com.javaoffers.batis.modelhelper.fun.GetterFun;
import com.javaoffers.batis.modelhelper.fun.condition.GroupByCondition;
import com.javaoffers.batis.modelhelper.fun.condition.LeftGroupByCondition;
import com.javaoffers.batis.modelhelper.fun.condition.LimitCondition;
import com.javaoffers.batis.modelhelper.fun.crud.GroupFun;
import com.javaoffers.batis.modelhelper.fun.crud.HavingFun;
import com.javaoffers.batis.modelhelper.fun.crud.HavingPendingFun;
import com.javaoffers.batis.modelhelper.fun.crud.LeftHavingPendingFun;
import com.javaoffers.batis.modelhelper.fun.crud.impl.HavingFunImpl;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description:
 * @Auther: create by cmj on 2022/6/5 19:42
 */
public class LeftHavingPendingFunImpl<M, M2, C extends GetterFun<M,?>, C2 extends GGetterFun<M2,?>, V, V2> implements
        LeftHavingPendingFun<M, M2, C, C2, V, LeftHavingFunImpl<M,M2,C,C2,V,V2>> {

    private LinkedList<Condition> conditions;

    public LeftHavingPendingFunImpl(LinkedList<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public LinkedList<Condition> getConditions() {
        return this.conditions;
    }

    @Override
    public M ex() {
        List<M> exs = exs();
        if (exs != null && exs.size() > 0) {
            return exs.get(0);
        }
        return null;
    }

    /**
     * 主表分组
     * @param c 主表分组字段
     * @return
     */
    public LeftHavingPendingFunImpl<M, M2, C,C2,V,V2> groupBy(GetterFun<M, V>... c) {
        conditions.add(new LeftGroupByCondition(c, ConditionTag.GROUP_BY));
        return new LeftHavingPendingFunImpl<M, M2, C,C2,V,V2>(conditions);
    }

    /**
     * 子表分组
     * @param c 子表分组字段
     * @return
     */
    public LeftHavingPendingFunImpl<M, M2, C,C2,V,V2> groupBy(GGetterFun<M2, V>... c) {
        conditions.add(new LeftGroupByCondition(c, ConditionTag.GROUP_BY));
        return new LeftHavingPendingFunImpl<M, M2, C,C2,V,V2>(conditions);
    }

    /**
     * 分页
     * @return
     */
    public LeftHavingPendingFunImpl<M, M2, C,C2,V,V2> limitPage(int pageNum, int size) {
        this.conditions.add(new LimitCondition(pageNum, size));
        return new LeftHavingPendingFunImpl<M, M2, C,C2,V,V2>(conditions);
    }


    @Override
    public LeftHavingFunImpl<M,M2,C,C2,V,V2>  having() {
        LeftHavingFunImpl mcvHavingFun = new LeftHavingFunImpl<>(this.conditions);
        return mcvHavingFun;
    }

}
