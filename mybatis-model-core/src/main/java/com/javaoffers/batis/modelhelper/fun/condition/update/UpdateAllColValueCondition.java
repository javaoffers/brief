package com.javaoffers.batis.modelhelper.fun.condition.update;

import com.javaoffers.batis.modelhelper.fun.ConditionTag;
import com.javaoffers.batis.modelhelper.utils.TableHelper;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * all Col value update include null value
 * @author create by cmj
 */
public class UpdateAllColValueCondition implements UpdateCondition {

    boolean isUpdateNull;

    boolean isExistsNoneNullValue;

    Class modelClass;

    Map<String,Object> params = new HashMap<>();

    Map<String,Object> npdateNullParams = new HashMap<>();

    Object model;

    private String tableName;

    //all Col
    private StringBuilder updateSqlNull = new StringBuilder(ConditionTag.UPDATE.getTag());

    // col not null only
    private StringBuilder updateSql = new StringBuilder(ConditionTag.UPDATE.getTag());

    @Override
    public ConditionTag getConditionTag() {
        return ConditionTag.UPDATE;
    }

    @Override
    public String getSql() {
        if(!isUpdateNull && !isExistsNoneNullValue){
            return "";
        }
        return isUpdateNull ? updateSqlNull.toString() : updateSql.toString();
    }

    @Override
    public Map<String, Object> getParams() {
        return isUpdateNull ? params : npdateNullParams;
    }

    public UpdateAllColValueCondition(boolean isUpdateNull, Class modelClass, Object model) {
        Assert.isTrue(model != null , "model is not allowed to be null");
        this.isUpdateNull = isUpdateNull;
        this.modelClass = modelClass;
        this.model = model;
        this.tableName = TableHelper.getTableName(modelClass);
        Map<String, List<Field>> colAllAndFieldOnly = TableHelper.getColAllAndFieldOnly(modelClass);

        updateSqlNull.append(tableName);
        updateSql.append(tableName);
        AtomicInteger status = new AtomicInteger(0);
        AtomicInteger statusNull = new AtomicInteger(0);
        colAllAndFieldOnly.forEach((cloName, fields) -> {
            try {
                if(statusNull.get() == 0){
                    updateSqlNull.append(ConditionTag.SET.getTag());
                    statusNull.incrementAndGet();
                }else {
                    updateSqlNull.append(", ");
                }

                updateSqlNull.append(cloName);
                updateSqlNull.append(" = #{");
                updateSqlNull.append(cloName);
                updateSqlNull.append("}");
                Object oValue = null;
                for(int i = 0; i < fields.size(); i++){
                    Field field = fields.get(0);
                    Object o = field.get(model);
                    //take the first non-null value
                    if(o != null){
                        oValue = o;
                        break;
                    }
                }
                params.put(cloName, oValue);
                if(oValue!=null){
                    npdateNullParams.put(cloName, oValue);
                    isExistsNoneNullValue = true;
                    if(status.get() == 0){
                        updateSql.append(ConditionTag.SET.getTag());
                        status.incrementAndGet();
                    }else{
                        updateSql.append(", ");
                    }
                    updateSql.append(cloName);
                    updateSql.append(" = #{");
                    updateSql.append(cloName);
                    updateSql.append("}");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}