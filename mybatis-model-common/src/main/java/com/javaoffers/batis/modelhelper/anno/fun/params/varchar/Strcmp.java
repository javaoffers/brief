package com.javaoffers.batis.modelhelper.anno.fun.params.varchar;

import com.javaoffers.batis.modelhelper.constants.ModelHelpperConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * strcmp(str1,str2): compareStringSizeFunction
 * create by cmj
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Strcmp {
    public static final String TAG = "STRCMP";

    /**
     * Defaults to the field corresponding to the current property. If not specified.
     */
    String expr1() default  ModelHelpperConstants.FIELD_COL_NAME;
    String expr2();
}