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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.codeviation.commons.reflect.ClassUtils;
import org.codeviation.pojson.records.RecordComplex;
import org.codeviation.pojson.records.RecordSmall;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Hrebejk
 */
public class ToCollectionsTest {
    
    private static Map<String,Object> GOLDEN_TYPES;
    private static Map<String,Object> GOLDEN_ARRAYS;
    
    public ToCollectionsTest() {
    }
    
    
    @BeforeClass
    public static void init() throws IOException {
        
        Map<String,Object> GOLDEN_SR1 = new HashMap<String,Object>();
        GOLDEN_SR1.put("i",  1l);
        GOLDEN_SR1.put("s",  "A");
        GOLDEN_SR1.put("rs",  null);
        
        Map<String,Object> GOLDEN_SR2 = new HashMap<String,Object>();
        GOLDEN_SR2.put("i",  2l);
        GOLDEN_SR2.put("s",  "B");
        GOLDEN_SR2.put("rs",  null);
        
        Map<String,Object> GOLDEN_SR3 = new HashMap<String,Object>();
        GOLDEN_SR3.put("i",  3l);
        GOLDEN_SR3.put("s",  "C");
        GOLDEN_SR3.put("rs",  null);
        
        GOLDEN_ARRAYS = new HashMap();
        GOLDEN_ARRAYS.put("charArray", Arrays.asList("x", "y", "z"));
        GOLDEN_ARRAYS.put("intArray",  Arrays.asList(1l, 2l, 3l));
        GOLDEN_ARRAYS.put("stringArray", Arrays.asList("A", "B", "C"));
        GOLDEN_ARRAYS.put("enumArray", Arrays.asList("CLASS", "RUNTIME"));
        GOLDEN_ARRAYS.put("objectArray", Arrays.asList( GOLDEN_SR1, GOLDEN_SR2, GOLDEN_SR3 ));
        GOLDEN_ARRAYS.put("empty", Arrays.asList());
        
        GOLDEN_TYPES = new HashMap<String,Object>();
        GOLDEN_TYPES.put("fBoolean" ,true);    
        GOLDEN_TYPES.put("fCharacter",  "c");
        GOLDEN_TYPES.put("fByte", 1l);
        GOLDEN_TYPES.put("fShort", 2l);
        GOLDEN_TYPES.put("fInteger", 3l);
        GOLDEN_TYPES.put("fLong", 4l);
        GOLDEN_TYPES.put("fFloat", 5.5d);
        GOLDEN_TYPES.put("fDouble", 6.6d);
        GOLDEN_TYPES.put("fString", "string");
        GOLDEN_TYPES.put("fEnum", "SOURCE"); 
        
    }
    
    @Test
    public void complex() throws IOException {
        System.out.println("complex");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();
        
        RecordComplex c1 = new RecordComplex();
        String s1 = save.asString(c1);
        Object o = load.toCollections(s1);
        
        Map<String,Object> golden = new HashMap<String,Object>();
        golden.put("primitives", GOLDEN_TYPES);
        golden.put("objects", GOLDEN_TYPES);
        golden.put("arrays", GOLDEN_ARRAYS);
                
        assertEquals(golden, o);
    }
    
    @Test
    public void arrays() throws IOException {
        System.out.println("arrays");
                
        String text = ClassUtils.getResourceAsString(ToCollectionsTest.class, "goldenfiles/Arrays.txt");        
        PojsonLoad load = PojsonLoad.create();
        Object o = load.toCollections(text);
   
        assertEquals(GOLDEN_ARRAYS, o);                
    }  
    
    
    
    @Test
    public void arraysInteger() throws IOException {
        System.out.println("arraysInteger");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();        
        
        Integer[] i1 = new Integer[] {1,2,3,900000};
        String s1 = save.asString(i1);        
        Object o = load.toCollections(s1);
                
        
        assertEquals( Arrays.asList(1l,2l,3l,900000l), o );
        
    }
    
    
    @Test
    public void arraysLong() throws IOException {
        System.out.println("arraysLong");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();
        
        Long[] l1 = new Long[] {1l,2l,3l,900000l};
        String s1 = save.asString(l1);
        Object o = load.toCollections(s1);
        
        assertEquals( Arrays.asList(l1), o );
        
    }
    
    @Test
    public void arraysFloat() throws IOException {
        System.out.println("arraysFloat");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();
        
        Float[] f1 = new Float[] {1.0f,2.0f,3.0f,9000.78f};
        String s1 = save.asString(f1);
        Object o = load.toCollections(s1);
                
        assertEquals( Arrays.asList(1l,2l,3l,9000.78), o );
        
    }
    
    @Test
    public void arraysDouble() throws IOException {
        System.out.println("arraysDouble");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();
        
        Double[] d1 = new Double[] {1.0,2.0,3.0,900000.783293289};
        String s1 = save.asString(d1);
        Object o = load.toCollections(s1);
        
        assertEquals( Arrays.asList(1l,2l,3l,900000.783293289), o );
        
    }
}
