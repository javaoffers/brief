/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.javaoffers.thrid.jsqlparser.expression.operators.arithmetic;

import com.javaoffers.thrid.jsqlparser.expression.BinaryExpression;
import com.javaoffers.thrid.jsqlparser.expression.Expression;
import com.javaoffers.thrid.jsqlparser.expression.ExpressionVisitor;

public class BitwiseOr extends BinaryExpression {

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String getStringExpression() {
        return "|";
    }

    @Override
    public BitwiseOr withLeftExpression(Expression arg0) {
        return (BitwiseOr) super.withLeftExpression(arg0);
    }

    @Override
    public BitwiseOr withRightExpression(Expression arg0) {
        return (BitwiseOr) super.withRightExpression(arg0);
    }
}
