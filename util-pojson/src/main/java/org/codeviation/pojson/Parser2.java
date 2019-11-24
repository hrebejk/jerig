/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */

package org.codeviation.pojson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.Stack;

/** Good for parsing JSON files folowing RFC 4627. See
 *  http://www.ietf.org/rfc/rfc4627.txt
 *
 * @author Petr Hrebejk
 */
class Parser2<E extends Exception> {
    
    private static final int OBJECT_START = '{';
    private static final int OBJECT_END = '}';
    private static final int ARRAY_START = '[';
    private static final int ARRAY_END = ']';
    private static final int COLON = ':';
    private static final int COMMA = ',';
    private static final int PLUS = '+';
    private static final int MINUS = '-';
    private static final int DOUBLE_QUOTE = '"';            
    private static final int COMMENT_START = '/';
    private static final int LINE_COMMENT = COMMENT_START;
    private static final int BLOCK_COMMENT = '*';        
    
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String NULL = "null";
    
    private static final int TRUE_START = TRUE.charAt(0);
    private static final int FALSE_START = FALSE.charAt(0);
    private static final int NULL_START = NULL.charAt(0);
        
    private final Stack<Where> where;
    private Error error;
    private Handler<E> handler;
    private final PushbackReader reader;
    
    
    public static <E extends Exception> void parse( InputStream stream, PojsonBuilder<?,E> builder ) throws IOException, E {
        parse( new InputStreamReader(stream), builder );
    }
    
    public static <E extends Exception> void parse( Reader reader, PojsonBuilder<?,E> builder ) throws IOException, E {
          Parser2<E> jp = new Parser2<>(reader, builder);
          jp.parse(false);
    }
     
    /** For testing purposes only */
    static void parse( Reader reader, Handler<RuntimeException> handler ) throws IOException {
        Parser2<RuntimeException> jp = new Parser2<>(reader, null);
        jp.handler = handler;
        jp.parse(false);
    }

    private Parser2( Reader reader, PojsonBuilder<?,E> builder ) {
        this.where = new Stack<>();
        this.where.push(Where.OUT);
        this.handler = new Handler<>( builder );
        this.reader = new PushbackReader(reader, 1);
    }
        
    private void parse(boolean incremental) throws IOException, E {
    
        int cc = 0; // current character
               
        while( ( cc = reader.read() ) != -1 ) {
                                    
            if ( Character.isWhitespace(cc)) {
                continue;
            }
            
            switch( where.peek() ) {
                case OUT:
                    if ( cc == OBJECT_START ) {
                        handler.objectStart();
                        where.push(Where.OBJECT);
                    }
                    else if ( cc == ARRAY_START ) {
                        handler.arrayStart();
                        where.push(Where.ARRAY);                        
                    }
                    else if ( cc == COMMENT_START) {
                        where.push(Where.COMMENT);
                    }
                    else {
                        error = Error.OBJECT_OR_ARRAY_EXPECTED;
                    }
                    break;
                case COMMENT:
                    if (cc == LINE_COMMENT) {
                        handleComment(true);
                        where.pop();
                    }
                    else if (cc == BLOCK_COMMENT) {
                        handleComment(false);
                        where.pop();
                    }
                    else {
                        error = Error.WRONG_COMMENT;
                    }
                    break;
                case OBJECT:
                    if ( cc == DOUBLE_QUOTE ) {
                        String s = handleStringValue(); 
                        if ( s != null) {
                            handler.field(s);
                            where.push(Where.OBJECT_COLON);
                        }
                    }
                    else if ( cc == OBJECT_END ) {
                        handler.objectEnd();
                        where.pop();
                    }
                    else if ( cc == COMMENT_START) {
                        where.push(Where.COMMENT);
                    }
                    else {
                        error = Error.FIELD_NAME_EXPECTED;
                    }
                    break;
                case OBJECT_COLON:
                    if ( cc == COLON ) {
                        where.pop();
                        where.push(Where.OBJECT_VALUE);
                    }
                    else if ( cc == COMMENT_START) {
                        where.push(Where.COMMENT);
                    }
                    else {
                        error = Error.COLON_EXPECTED;
                    }
                    break;
                case OBJECT_VALUE:
                    if ( cc == OBJECT_START ) {
                        handler.objectStart();   
                        where.pop();
                        where.push(Where.OBJECT_COMMA_OR_END);
                        where.push(Where.OBJECT);
                    }
                    else if ( cc == ARRAY_START ) {
                        handler.arrayStart();
                        where.pop();
                        where.push(Where.OBJECT_COMMA_OR_END);
                        where.push(Where.ARRAY);
                    }
                    else if ( cc == COMMENT_START) {
                        where.push(Where.COMMENT);
                    }
                    else { 
                        handleSimpleValue(cc);
                        where.pop();
                        where.push(Where.OBJECT_COMMA_OR_END);
                    }
                    break;
                case OBJECT_COMMA_OR_END:
                    if ( cc == COMMA ) {
                        where.pop();
                       // where.push(Where.OBJECT);
                    }
                    else if ( cc == OBJECT_END ) {
                        where.pop();
                        where.pop();
                        handler.objectEnd();
                    }
                    else if ( cc == COMMENT_START) {
                        where.push(Where.COMMENT);
                    }
                    else {
                        error = Error.COMMA_OR_END_OF_OBJECT_EXPECTED;
                    }
                    break;
                case ARRAY:                                        
                    if ( cc == OBJECT_START ) {
                        handler.objectStart();
                        where.push(Where.ARRAY_COMMA_OR_END);
                        where.push(Where.OBJECT);
                    }
                    else if ( cc == ARRAY_START ) {
                        handler.arrayStart();
                        where.push(Where.ARRAY_COMMA_OR_END);
                        where.push(Where.ARRAY);
                    }                    
                    else if ( cc == ARRAY_END ) {
                        handler.arrayEnd();
                        where.pop();
                    }
                    else if ( cc == COMMENT_START) {
                        where.push(Where.COMMENT);
                    }
                    else { 
                        handleSimpleValue(cc);
                        where.push(Where.ARRAY_COMMA_OR_END);
                    }
                    break;
                case ARRAY_COMMA_OR_END:
                    if ( cc == COMMA ) {
                        where.pop();
                    }
                    else if ( cc == ARRAY_END ) {
                        where.pop();
                        where.pop();
                        handler.arrayEnd();
                    }
                    else if ( cc == COMMENT_START) {
                        where.push(Where.COMMENT);
                    }
                    else {
                        error = Error.COMMA_OR_END_OF_ARRAY_EXPECTED;
                    }
                    break;
            }
            
            if ( error != null ) {
                handler.error(error);
                break;
            }
        }
        
        if( cc == -1 && where.size() != 1) {
            handler.error(Error.UNCLOSED_OBJECT_OR_ARRAY);
        }
    }
    
    private void handleSimpleValue(int cc) throws IOException, E {
        if ( cc == TRUE_START && finishString(reader, TRUE ) ) {
            handler.bool(true);            
        }
        else if ( cc == FALSE_START && finishString(reader, FALSE))  {                        
            handler.bool(false);            
        }
        else if (cc == NULL_START && finishString(reader, NULL)) {
            handler.nul();                                                
        }
        else if (cc == DOUBLE_QUOTE ) {
            String s = handleStringValue(); 
            if ( s != null) {
                handler.string(s);
            }
        }        
        else if ( cc == PLUS || cc == MINUS || isDecNumber((char)cc) ) {
            reader.unread(cc);
            handleNumberValue();
        } 
        else {
            error = Error.UNKNOWN_VALUE_TYPE;
        }
        
    }

    private void handleNumberValue() throws IOException, E {
        StringBuilder sb = new StringBuilder();
        int cc;
        boolean isFloatingPoint = false;
        while( ( cc = reader.read() ) != -1 ) {
           char c = (char)cc; 
           
           if ( !isFloatingPoint && ( c == '.' || c == 'e' || c == 'E' ) ) {
               isFloatingPoint = true;
           }
           
           if (isFloatChar(c)) {
               sb.append(c);
           }
           else if (c == COMMA || c == ARRAY_END || c == OBJECT_END || c == COMMENT_START ) {
               reader.unread(cc);
               break;
           }
           else if ( Character.isWhitespace(c)) {
                break;
           }
           else {
               error = Error.INVALID_NUMBER;
               return;
           }
        }
        try {
            if ( isFloatingPoint ) {
                handler.number(Double.parseDouble(sb.toString()));
            }
            else {
                handler.number(Long.parseLong(sb.toString()));
            }
        }
        catch( NumberFormatException ex ) {
            error = Error.INVALID_NUMBER;
        }
    }
    
    
    private String handleStringValue() throws IOException {
        int cc;
        StringBuilder sb = new StringBuilder();
//        System.out.println("");
        while( ( cc = reader.read() ) != -1 ) {
//            System.out.print((char)cc);
            switch (cc) {
            case DOUBLE_QUOTE:
                return sb.toString();
            case '\\':
                cc = reader.read();
                switch (cc) {
                case '\\':
                    sb.append('\\');
                    break;
                case '"':
                    sb.append('"');
                    break;
                case '/':
                    sb.append('/');
                    break;
                case 'b':
                    sb.append('\b');
                    break;
                case 't':
                    sb.append('\t');
                    break;
                case 'n':
                    sb.append('\n');
                    break;
                case 'f':
                    sb.append('\f');
                    break;
                case 'r':
                    sb.append('\r');
                    break;                
                case 'u':
                    int c = handleUnicodeChar(reader);
                    if ( c == -1 ) {
                        error = Error.INVALID_HEX_ENCODED_CHAR;
                        return null;
                    }
                    sb.append((char)c);
                    break;                
                default:
                    error = Error.INVALID_ESCAPE_CHAR;
                    return null;
                }
                break;
            default:
                if( !isJsonStringChar(cc)) {
                    error = Error.INVALID_CHARACTER_IN_STRING_LITERAL;
                }
                sb.append((char)cc);                 
            }
        }
        
        error = Error.UNCLOSED_STRING_VALUE;
        return null;
    }
    
    private int handleUnicodeChar(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        
        char chars[] = new char[4];
        
        for(int i = 0; i < 4; i++) {
            int rc = reader.read();
            
            if (rc == -1) {
                return -1;
            }
            
            chars[i] = (char)rc;
            
            if (!isHexaChar(chars[i]) ) {
                return -1;
            }
            
        }
                
        String nr = new String(chars);
        try {
            int r = Integer.parseInt(nr, 16);
            return r;
        }
        catch(NumberFormatException ex) {
            return -1;
        }        
    }
    
    private void handleComment(boolean line) throws IOException, E {
        int cc;
        StringBuilder sb = new StringBuilder();
        while( ( cc = reader.read() ) != -1 ) {
            if (line && ( cc == '\n' || cc == '\r') ) {
                handler.lineComment(sb.toString());
                return;
            }
            else if (!line && cc == BLOCK_COMMENT) {
                int cc1 = reader.read();
                if ( cc1 == -1 ) {
                    break;
                }
                if ( cc1 == COMMENT_START ) {
                    handler.comment(sb.toString());
                    return;
                }
                else {
                    sb.append((char)cc);
                    sb.append((char)cc1);
                }
            }
            else {
                sb.append((char)cc);
            }
        }
        
        error = Error.UNCLOSED_COMMENT;
    }
    
    
    private static boolean isFloatChar(char c) {
        return isDecNumber(c) || c == '.' || 
               c == PLUS || c == MINUS || 
               c == 'e' || c == 'E';
    }
    
    private static boolean isDecNumber(char c) {
        return ( c >= '0' && c <= '9');
    }
    
    private static boolean isHexaChar(char c) {
        char uc = Character.toUpperCase(c);
        return isDecNumber(c) || ( uc >= 'A' && uc <= 'F');
    }
    
    private static boolean isJsonStringChar(int c) {
        
        return c == 0x20 || c == 0x21 ||
               (c >= 0x23 && c <= 0x5B) ||
               (c >= 0x5D && c <= 0x10FFFF );        
    } 
    
    private static boolean finishString(Reader r, String s) throws IOException {
        
        for( int i = 1; i < s.length(); i++ ) {
            if ( r.read() != s.charAt(i)) {
                return false;
            }
        }
        
        return true;
                
    }
        
    public static class Handler<E extends Exception> {

        private PojsonBuilder<?,E> builder;

        public Handler( PojsonBuilder<?,E> builder ) {
            this.builder = builder;
        }

        public void objectStart() throws E  {
            builder = builder.hash();
        }

        public void arrayStart() throws E {
            builder = builder.array();
        }

        public void field(String name) throws E {
            builder = builder.field(name);
        }

        public void objectEnd() throws E {
            builder = builder.up();
        }

        public void arrayEnd() throws E {
            builder = builder.up();
        }

        public void bool(boolean value) throws E {
            builder = builder.value( value );
        }

        public void string(String value) throws E {
            builder = builder.value( value );
        }

        public void nul() throws E  {
            builder = builder.value();
        }

        public void number(long value) throws E {
            builder = builder.value( value );
        }

        public void number(double value) throws E {
            builder = builder.value( value );
        }

        public void comment(String comment) throws E {
            System.out.println("Comment " + comment);
        }

        public void lineComment(String comment) throws E {
            System.out.println("Line comment " + comment);
        }

        public void error(Error error) throws E {
            throw new IllegalArgumentException("JSON Format error " + error);
        }

    }

    
    private static enum Where {
        
        OUT,            // Waiting for a object or array to start
        
        OBJECT,         // Object started awaiting field name
        OBJECT_COLON,        
        OBJECT_VALUE,
        OBJECT_COMMA_OR_END,
        
        ARRAY,                  // Array before the first item
        ARRAY_COMMA_OR_END,     // Array before neesds "," | "]"
        
        COMMENT,
        
        NAME;
        
    }
    
    public static enum Error {
        OBJECT_OR_ARRAY_EXPECTED,
        UNKNOWN_VALUE_TYPE,
        COMMA_OR_END_OF_ARRAY_EXPECTED,
        COMMA_OR_END_OF_OBJECT_EXPECTED,
        INVALID_ESCAPE_CHAR,
        INVALID_HEX_ENCODED_CHAR,
        INVALID_CHARACTER_IN_STRING_LITERAL,
        UNCLOSED_STRING_VALUE,
        INVALID_NUMBER,
        FIELD_NAME_EXPECTED,
        COLON_EXPECTED,
        WRONG_COMMENT,
        UNCLOSED_COMMENT,
        UNCLOSED_OBJECT_OR_ARRAY;
    }
    
}
