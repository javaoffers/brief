package com.javaoffers.brief.modelhelper.fun.crud.impl.insert;

import com.javaoffers.brief.modelhelper.fun.Condition;
import com.javaoffers.brief.modelhelper.fun.ExecutOneFun;
import com.javaoffers.brief.modelhelper.fun.GetterFun;
import com.javaoffers.brief.modelhelper.fun.condition.insert.InsertAllColValueCondition;
import com.javaoffers.brief.modelhelper.fun.condition.mark.OnDuplicateKeyUpdateMark;
import com.javaoffers.brief.modelhelper.fun.condition.mark.ReplaceIntoMark;
import com.javaoffers.brief.modelhelper.fun.crud.insert.MoreInsertFun;
import com.javaoffers.brief.modelhelper.fun.crud.insert.SmartMoreInsertFun;
import com.javaoffers.brief.modelhelper.core.Id;
import com.javaoffers.brief.modelhelper.core.LinkedConditions;

import java.util.Collection;

public class SmartMoreInsertFunImpl<M> implements SmartMoreInsertFun<M, GetterFun<M, Object>, Object> {

    private Class<M> mClass;

    private LinkedConditions<Condition> conditions;

    private OneInsertFunImpl oneInsertFun;

    private MoreInsertFun moreInsertFun;

    /**
     * Returns the primary key id after success, and the order of multiple ids is consistent with the inserted model
     * @return ids
     */
    @Override
    public Id ex() {
        return oneInsertFun.ex();
    }

    @Override
    public MoreInsertFun<M, GetterFun<M, Object>, Object> colAll(M model) {
        conditions.add(new InsertAllColValueCondition(mClass, model));
        return moreInsertFun;
    }

    @Override
    public MoreInsertFun<M, GetterFun<M, Object>, Object> colAll(boolean condition, M model) {
        if(condition){
            colAll(model);
        }
        return moreInsertFun;
    }

    @Override
    @SafeVarargs
    public final MoreInsertFun<M, GetterFun<M, Object>, Object> colAll(M... models) {
        for(M m: models){
            colAll(m);
        }
        return moreInsertFun;
    }

    @Override
    @SafeVarargs
    public final MoreInsertFun<M, GetterFun<M, Object>, Object> colAll(boolean condition, M... models) {
        if(condition){
            colAll(models);
        }
        return moreInsertFun;
    }

    @Override
    public MoreInsertFun<M, GetterFun<M, Object>, Object> colAll(Collection<M> models) {
        if(models!=null && models.size() > 0){
            models.forEach(model->{
                colAll(model);
            });
        }
        return moreInsertFun;
    }

    @Override
    public MoreInsertFun<M, GetterFun<M, Object>, Object> colAll(boolean condition, Collection<M> models) {
        if(condition){
            colAll(models);
        }
        return moreInsertFun;
    }

    public SmartMoreInsertFunImpl(Class<M> mClass, LinkedConditions<Condition> conditions) {
        this.mClass = mClass;
        this.conditions = conditions;
        this.oneInsertFun = new OneInsertFunImpl(this.mClass, this.conditions);
        this.moreInsertFun = new MoreInsertFunImpl(this.mClass, this.conditions);
    }

    @Override
    public ExecutOneFun<Id> dupUpdate() {
        this.conditions.add(new OnDuplicateKeyUpdateMark(this.mClass));
        return this;
    }

//    @Override
//    public ExecutOneFun<Id> dupReplace() {
//        this.conditions.add(new ReplaceIntoMark());
//        return this;
//    }
}
