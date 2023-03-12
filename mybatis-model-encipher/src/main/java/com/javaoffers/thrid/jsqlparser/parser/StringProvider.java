/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2022 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
/* Generated By:JavaCC: Do not edit this line. StringProvider.java Version 7.0 */
/* JavaCCOptions:KEEP_LINE_COLUMN=true */
package com.javaoffers.thrid.jsqlparser.parser;

	
	import java.io.IOException;

public class StringProvider implements Provider {

    String _string;
    int _position = 0;
    int _size;

    public StringProvider(String string) {
        _string = string;
        _size = string.length();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int numCharsOutstandingInString = _size - _position;

        if (numCharsOutstandingInString == 0) {
            return -1;
        }

        int numBytesInBuffer = cbuf.length;
        int numBytesToRead = numBytesInBuffer -off;
        numBytesToRead = numBytesToRead > len ? len : numBytesToRead;

        if (numBytesToRead > numCharsOutstandingInString) {
            numBytesToRead = numCharsOutstandingInString;
        }

        _string.getChars(_position, _position + numBytesToRead, cbuf, off);

        _position += numBytesToRead;

        return numBytesToRead;
    }

    @Override
    public void close() throws IOException {
        _string = null;
    }

}
/* JavaCC - OriginalChecksum=378309cdcf0d00ac5d27d6c7a2b4f010 (do not edit this line) */
