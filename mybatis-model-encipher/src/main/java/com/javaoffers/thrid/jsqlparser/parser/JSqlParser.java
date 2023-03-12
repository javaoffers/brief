/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.javaoffers.thrid.jsqlparser.parser;

import com.javaoffers.thrid.jsqlparser.JSQLParserException;
import com.javaoffers.thrid.jsqlparser.statement.Statement;

import java.io.Reader;

public interface JSqlParser {

    Statement parse(Reader statementReader) throws JSQLParserException;
}
