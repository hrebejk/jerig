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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codeviation.commons.reflect.ClassUtils;
import org.codeviation.pojson.records.RecordComplex;
import org.codeviation.pojson.records.RecordPrimitiveTypes;
import org.codeviation.pojson.records.RecordSmall;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Hrebejk
 */
public class CollectionsTest {
    
    private static Map<String,Object> GOLDEN_TYPES;
    private static Map<String,Object> GOLDEN_ARRAYS;
    
    public CollectionsTest() {
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
    public void primitive() throws IOException {
        System.out.println("primitive");

        RecordPrimitiveTypes p1 = new RecordPrimitiveTypes().init();
        String s1 = Pojson.save(p1);

        Map o = Pojson.load( HashMap.class, s1);
        assertEquals(GOLDEN_TYPES, o);
    }

    @Test
    public void complex() throws IOException {
        System.out.println("complex");
        
        
        RecordComplex c1 = new RecordComplex().init();
        String s1 = Pojson.save(c1);
        Map o = Pojson.load( HashMap.class, s1);

        Map<String,Object> golden = new HashMap<String,Object>();
        golden.put("primitives", GOLDEN_TYPES);
        golden.put("objects", GOLDEN_TYPES);
        golden.put("arrays", GOLDEN_ARRAYS);
                
        assertEquals(golden, o);
    }
    
    @Test
    public void arrays() throws IOException {
        System.out.println("arrays");
                
        String text = ClassUtils.getResourceAsString(CollectionsTest.class, "goldenfiles/Arrays.txt");
        Object o = Pojson.load(HashMap.class, text);
   
        assertEquals(GOLDEN_ARRAYS, o);                
    }  
    
    
    
    @Test
    public void arraysInteger() throws IOException {
        System.out.println("arraysInteger");
        
        Integer[] i1 = new Integer[] {1,2,3,900000};
        String s1 = Pojson.save(i1);
        Object o = Pojson.load(ArrayList.class, s1);

        assertEquals( Arrays.asList(1l,2l,3l,900000l), o );
        
    }
    
    
    @Test
    public void arraysLong() throws IOException {
        System.out.println("arraysLong");
        
   
        Long[] l1 = new Long[] {1l,2l,3l,900000l};
        String s1 = Pojson.save(l1);
        Object o = Pojson.load(ArrayList.class, s1);
        
        assertEquals( Arrays.asList(l1), o );
        
    }
    
    @Test
    public void arraysFloat() throws IOException {
        System.out.println("arraysFloat");
        
        Float[] f1 = new Float[] {1.0f,2.0f,3.0f,9000.78f};
        String s1 = Pojson.save(f1);
//        System.out.println(s1);
//        float d = f1[3].floatValue();
//        System.out.println(d);
        Object o = Pojson.load(ArrayList.class, s1);
                
        assertEquals( Arrays.asList(1l,2l,3l,9000.78), o );
        
    }
    
    @Test
    public void arraysDouble() throws IOException {
        System.out.println("arraysDouble");
        
        Double[] d1 = new Double[] {1.0,2.0,3.0,900000.783293289};
        String s1 = Pojson.save(d1);
        Object o = Pojson.load(ArrayList.class, s1);
        
        assertEquals( Arrays.asList(1l,2l,3l,900000.783293289), o );
        
    }

    @Test
    public void loadList() throws IOException {

        System.out.println("loadList");

        List<RecordSmall> d1 = new ArrayList<RecordSmall>();
        d1.add( new RecordSmall(1, "A"));
        d1.add(new RecordSmall(2, "B"));
        d1.add(new RecordSmall(3, "C"));

        String s1 = Pojson.save(d1);

        System.out.println(s1);

        @SuppressWarnings("unchecked")
        List<RecordSmall> d2 = Pojson.load(ArrayList.class, s1);
        String s2 = Pojson.save(d2);

        List<RecordSmall> d3 = new ArrayList<RecordSmall>();
        d3 = Pojson.update(d3, s1);
        assertEquals( s1, Pojson.save(d3) );
//      assertEquals(RecordSmall.class, d3.get(0).getClass());

        assertEquals( s1, s2 );

    }
}
