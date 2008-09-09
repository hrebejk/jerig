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

import org.codeviation.pojson.records.RecordArrays;
import java.io.IOException;
import org.codeviation.pojson.records.RecordComplex;
import org.codeviation.pojson.records.RecordObjectTypes;
import org.codeviation.pojson.records.RecordPrimitiveTypes;
import org.codeviation.pojson.records.RecordSmall;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Hrebejk
 */
public class ReadTest {
    
    
    public ReadTest() {
    }
    
    
    @Test
    public void primitives() throws IOException {
        System.out.println("primitives");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();
        
        RecordPrimitiveTypes r1 = new RecordPrimitiveTypes();
        String s1 = save.asString(r1);
        
        RecordPrimitiveTypes r2 = RecordPrimitiveTypes.empty();
        load.update(s1, r2);
        String s2 = save.asString(r2);        
        assertEquals(s1, s2);       
        
        RecordPrimitiveTypes r3 = load.load(s1, RecordPrimitiveTypes.class);
        s2 = save.asString(r3);
        assertEquals(s1, s2);       
    } 
    
    @Test
    public void object() throws IOException {
        System.out.println("object");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();
                
        RecordObjectTypes o1 = new RecordObjectTypes();
        String s1 = save.asString(o1);
        
        RecordObjectTypes o2 = RecordObjectTypes.empty();

        load.update(s1, o2);
        String s2 = save.asString(o2);
        assertEquals(s1, s2);
        
        RecordObjectTypes o3 = load.load(s1, RecordObjectTypes.class);
        s2 = save.asString(o3);
        
    } 
    
    @Test
    public void complex() throws IOException {
        System.out.println("complex");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();
        
        RecordComplex c1 = new RecordComplex();
        String s1 = save.asString(c1);
        
        RecordComplex c2 = load.load(s1, RecordComplex.class);
        String s2 = save.asString(c2);
        
        assertEquals(s1, s2);                
    } 
        
    
    @Test 
    public void deep() throws IOException {
        System.out.println("deep");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();
        
        RecordSmall c1 = new RecordSmall( 1, "A", 
                new RecordSmall( 2 , "B", 
                    new RecordSmall(3, "C", 
                        new RecordSmall( 4, "D", 
                            new RecordSmall( 5, "E", 
                                new RecordSmall(6, "F"))))));
        String s1 = save.asString(c1);
        
        RecordSmall c2 = load.load(s1, RecordSmall.class);
        String s2 = save.asString(c2);
        
        assertEquals(s1, s2);                
                
    }
    
    @Test
    public void arrays() throws IOException {
        System.out.println("arrays");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();
        
        RecordArrays r1 = new RecordArrays();        
        String s1 = save.asString(r1);
        
        RecordArrays r2 = load.load(s1, RecordArrays.class);
        String s2 = save.asString(r2);
        
        assertEquals( s1, s2);
        
    }
        
    @Test
    public void arraysInteger() throws IOException {
        System.out.println("arraysInteger");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();
        
        
        Integer[] i1 = new Integer[] {1,2,3,900000};
        String s1 = save.asString(i1);
        
        Integer[] i2 = load.load(s1, Integer[].class);
        String s2 = save.asString(i2);
                
        assertEquals( s1, s2 );
        
    }
    
    @Test
    public void arraysLong() throws IOException {
        System.out.println("arraysLong");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();
        
        Long[] l1 = new Long[] {1l,2l,3l,900000l};
        String s1 = save.asString(l1);
        
        //Long[] l2 = load.load(s1, Long[].class);
//        String s2 = save.asString(l2);
        String s2 = save.asString(load.update(s1, new long[0]));
                
        assertEquals( s1, s2 );
        
    }
    
    @Test
    public void arraysFloat() throws IOException {
        System.out.println("arraysFloat");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();
        
        Float[] f1 = new Float[] {1.0f,2.0f,3.0f,900000.783293289f};
        String s1 = save.asString(f1);
        
        Float[] f2 = load.load(s1, Float[].class);
        String s2 = save.asString(f2);
                
        assertEquals( s1, s2 );
        
    }
    
    @Test
    public void arraysDouble() throws IOException {
        System.out.println("arraysDouble");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();
        
        Double[] d1 = new Double[] {1.0,2.0,3.0,900000.783293289};
        String s1 = save.asString(d1);
        
        Double[] d2 = load.load(s1, Double[].class);
        String s2 = save.asString(d2);
                
        assertEquals( s1, s2 );
        
    }
    
    @Test
    public void arraysObject() throws IOException {
        System.out.println("arraysObject");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();
        
        RecordSmall[] d1 = new RecordSmall[] {
            new RecordSmall(1, "A"),
            new RecordSmall(2, "B"),
            new RecordSmall(3, "C")
        };
        String s1 = save.asString(d1);
     
        System.out.println(s1);
        
        RecordSmall[] d2 = load.load(s1, RecordSmall[].class);
        String s2 = save.asString(d2);
                
        assertEquals( s1, s2 );
        
    }

}
