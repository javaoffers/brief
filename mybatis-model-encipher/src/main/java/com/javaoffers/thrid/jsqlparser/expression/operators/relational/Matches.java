/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.javaoffers.thrid.jsqlparser.expression.operators.relational;

import com.javaoffers.thrid.jsqlparser.expression.Expression;
import com.javaoffers.thrid.jsqlparser.expression.ExpressionVisitor;

public class Matches extends OldOracleJoinBinaryExpression {

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String getStringExpression() {
        return "@@";
    }

    @Override
    public Matches withLeftExpression(Expression arg0) {
        return (Matches) super.withLeftExpression(arg0);
    }

    @Override
    public Matches withRightExpression(Expression arg0) {
        return (Matches) super.withRightExpression(arg0);
    }

    @Override
    public Matches withOldOracleJoinSyntax(int oldOracleJoinSyntax) {
        return (Matches) super.withOldOracleJoinSyntax(oldOracleJoinSyntax);
    }

    @Override
    public Matches withOraclePriorPosition(int oraclePriorPosition) {
        return (Matches) super.withOraclePriorPosition(oraclePriorPosition);
    }
}
