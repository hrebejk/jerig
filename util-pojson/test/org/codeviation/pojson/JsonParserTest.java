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

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import org.codeviation.pojson.Parser.Error;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Hrebejk
 */
public class JsonParserTest {

    public JsonParserTest() {
    }

    
    @Test
    public void emptyObject() throws IOException {
        System.out.println("emptyObject");
        
        String golden = "(OO)";        
        assertEquals(golden, testString("{}"));
        assertEquals(golden, testString("  {}  "));
        assertEquals(golden, testString(" { } "));
        assertEquals(golden, testString("{ \t\r\n } "));
        
    }
    
    @Test
    public void emptyArray() throws IOException {
        System.out.println("emptyArray");
        
        String golden = "(AA)";        
        assertEquals(golden, testString("[]"));
        assertEquals(golden, testString("  []  "));
        assertEquals(golden, testString(" [] "));
        assertEquals(golden, testString("[ \t\r\n ] "));
                
        assertEquals("(A(OO)(OO)A)", testString("[{},{}]"));
        assertEquals("(A(OO)(!COMMA_OR_END_OF_ARRAY_EXPECTED!)", testString("[{}{}]"));
    }

    @Test 
    public void booleanArray() throws IOException {
        System.out.println("booleanArray");
        
        String golden = "(A(BtrueB)A)";
        
        assertEquals(golden, testString("[true]"));
        assertEquals(golden, testString("[ \ntrue ]"));
        assertEquals(golden, testString("[ true ]"));
        
        golden = "(A(BtrueB)(BfalseB)A)";
        assertEquals(golden, testString("[true,false]"));
        
        golden = "(A(BtrueB)(NULL)(NULL)(BfalseB)(NULL)A)";
        assertEquals(golden, testString("[true,null,null,false,null]"));
                
    }
    
    @Test 
    public void stringArray() throws IOException {
        System.out.println("stringArray");
        
        String golden = "(A(Snazdar bazarS)A)";
        
        assertEquals(golden, testString("[\"nazdar bazar\"]"));
        assertEquals(golden, testString("[ \n\"nazdar bazar\" ]"));
        assertEquals(golden, testString("[ \"nazdar bazar\" ]"));
        
        golden = "(A(Snazdar bazarS)(SahojS)A)";
        assertEquals(golden, testString("[\"nazdar bazar\",\"ahoj\"]"));
    }
    
    @Test 
    public void longArray() throws IOException {
        System.out.println("longArray");
        
        String golden = "(A(L100L)A)";
        
        assertEquals(golden, testString("[100]"));
        assertEquals(golden, testString("[ \n100 ]"));
        assertEquals(golden, testString("[ \n\t100 ]"));
        
        golden = "(A(L10L)(L20L)A)";
        assertEquals(golden, testString("[ 10, 20]"));
        assertEquals(golden, testString("[ 10, 20,]"));
        
        assertEquals("(A(!UNKNOWN_VALUE_TYPE!)", testString("[ , 10, 20,]"));
    }
    
    @Test 
    public void doubleArray() throws IOException {
        System.out.println("doubleArray");
        
        String golden = "(A(D1000.0D)A)";
        
        assertEquals(golden, testString("[10e2]"));
        assertEquals(golden, testString("[ \n1000.0 ]"));
        assertEquals(golden, testString("[ \n\t10.0E2 ]"));
        
        golden = "(A(D10.0D)(D20.0D)A)";
        assertEquals(golden, testString("[ 10.0, 20.0]"));
    }
    
    
    @Test 
    public void complexArray() throws IOException {
        System.out.println("complexArray");
        
        assertEquals("(A(AA)A)", testString("[[]]"));
        assertEquals("(A(A(AA)A)A)", testString("[[[]]]"));
        assertEquals("(A(A(AA)(AA)A)A)", testString("[[[],[]]]"));
        
        assertEquals("(A(A(OO)A)A)", testString("[[{}]]"));
        assertEquals("(A(A(OO)(OO)A)A)", testString("[[{},{}]]"));
        
        
        assertEquals("(A(A(L3L)(L2L)(L1L)A)(A(BtrueB)(BfalseB)A)(A(NULL)(NULL)A)A)", 
                     testString("[ [3,2,1],[true,false],[null, null] ]"));
    }
    
    @Test 
    public void simpleObject() throws IOException {
        System.out.println("simpleObject");
        
        assertEquals("(O(FaF)(L1L)O)", testString("{\"a\":1}"));
        assertEquals("(O(FaF)(L1L)(Fb bF)(D10.1D)O)", testString("{ \"a\" : 1, \"b b\" : 10.1 }"));
    }
    
    @Test 
    public void complexObject() throws IOException {
        System.out.println("complexObject");
        
        assertEquals("(O(FaF)(OO)O)", testString("{\"a\":{}}"));
        assertEquals("(O(FaF)(O(FbF)(L1L)O)O)", testString("{\"a\":{\"b\":1}}"));
        assertEquals("(O(FaF)(O(FbF)(L1L)(FcF)(L2L)O)O)", testString("{\"a\":{\"b\":1,\"c\":2}}"));
        assertEquals("(O(FaF)(O(FbF)(L1L)O)O)", testString("{\"a\":{\"b\":1}}"));
        assertEquals("(O(FaF)(OO)(FbF)(OO)O)", testString("{\"a\":{},\"b\":{}}"));
        assertEquals("(O(FaF)(O(Fb1F)(OO)(Fb2F)(OO)O)O)", testString("{\"a\":{\"b1\":{},\"b2\":{}}}"));
        
        assertEquals("(O(FaF)(A(BtrueB)(NULL)(BfalseB)A)O)", testString("{ \"a\" : [true,null,false] }"));
        
        assertEquals("(O(FaF)(O(FbF)(L1L)O)(FcF)(O(FdF)(L2L)(FeF)(L3L)O)O)", testString("{\"a\":{\"b\":1},\"c\":{\"d\":2,\"e\":3}}"));
        
    }
    
    
    @Test 
    public void unicode() throws IOException {
        System.out.println("unicode");
        
        assertEquals("(A(SmS)A)", testString("[\"\\u006D\"]"));
        assertEquals("(A(SmS)A)", testString("[\"\\u006d\"]"));
    }
   
    @Test 
    public void comment() throws IOException {
        System.out.println("comment");
        
        assertEquals("(cac)(A(cbc)(L100L)(ccc)A)(cd/**///c)", testString("//a\n[//b\n100//c\n]//d/**///\n"));
        assertEquals("(CaC)(A(CbC)(L100L)(CcC)A)(Cd* //C)", testString("/*a*/[/*b*/100/*c*/]/*d* //*/"));
    }
        
    // XXX 
    // Invalid escapes
        
    private String testString(String json) throws IOException {
        
        CharArrayWriter w = new CharArrayWriter();
        
        TestHandler h = new TestHandler(w);
        Reader r = new CharArrayReader(json.toCharArray());        
        Parser.parse(r, h);
        
        return w.toString();
    }
    
    private class TestHandler implements Parser.Handler {

        PrintWriter pw;
        
        private TestHandler( Writer w ) {
            this.pw = new PrintWriter(w);
        }
        
        public void objectStart() {            
            pw.print("(O");
        }

        public void objectEnd() {
            pw.print("O)");
        }

        public void arrayStart() {
            pw.print("(A");
        }

        public void arrayEnd() {
            pw.print("A)");
        }

        public void field(String value) {
            pw.print("(F" + value + "F)");
        }

        public void bool(boolean value) {                       
            pw.print("(B" + value + "B)");
        }
        
        public void nul() {                       
            pw.print("(NULL)");
        }

        public void string(String value) {
            pw.print("(S" + value + "S)");
        }

        public void number(long value) {
            pw.print("(L" + value + "L)");
        }
        
        public void number(double value) {
            pw.print("(D" + value + "D)");
        }

        public void error(Error error) {
            pw.print("(!" + error + "!)");
        }
        
        public void comment(String comment) {
            pw.print("(C" + comment + "C)");
        }
        
        public void lineComment(String comment) {
            pw.print("(c" + comment + "c)");
        }
        
    }
    
    
    
    
}