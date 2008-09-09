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

package org.codeviation.commons.patterns;

import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phrebejk
 */
public class FactoriesTest {
    
//    @Test
//    public void array() {
//        System.out.println("array");
//        Factory<T, P> elementFactory = null;
//        Factory expResult = null;
//        Factory result = Factories.array(elementFactory);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    } /* Test of array method, of class Factories. */

    @Test
    public void fromMap() {
        System.out.println("fromMap");
        
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "jedna");
        map.put(2, "dve");
        map.put(3, "tri");
        map.put(4, "ctyri");
        map.put(5, "pet");
        
        Factory<String,Integer> factory = Factories.fromMap(map);

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            assertEquals(entry.getValue(), factory.create(entry.getKey()));
        }
        
    } 

//    @Test
//    public void chain() {
//        System.out.println("chain");
//        Factory<OT, IT> outer = null;
//        Factory<IT, IP> inner = null;
//        Factory expResult = null;
//        Factory result = Factories.chain(outer, inner);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    } /* Test of chain method, of class Factories. */

    
    @Test
    public void layered() {
        System.out.println("layered");
        
        Factory<String,Integer> f1, f2, f3;
        
        Map<Integer, String> map;
        map = new HashMap<Integer, String>();
        map.put(1, "1.1");
        map.put(2, "1.2");
        map.put(3, null);
        map.put(4, null);
        map.put(5, null);
        map.put(6, null);
        f1 = Factories.fromMap(map);
        
        map = new HashMap<Integer, String>();
        map.put(1, "2.1");
        map.put(2, null);
        map.put(3, "2.3");
        map.put(4, null);
        map.put(5, "2.5");
        map.put(6, null);
        f2 = Factories.fromMap(map);
        
        map = new HashMap<Integer, String>();
        map.put(1, null);
        map.put(2, "3.2");
        map.put(3, "3.3");
        map.put(4, "3.4");
        map.put(5, null);
        map.put(6, null);
        f3 = Factories.fromMap(map);
        
        Factory<String,Integer> f = Factories.layered(f1,f2,f3);
        
        assertEquals("1.1", f.create(1));
        assertEquals("1.2", f.create(2));
        assertEquals("2.3", f.create(3));
        assertEquals("3.4", f.create(4));
        assertEquals("2.5", f.create(5));
        assertNull(f.create(6));
        
    }
    
    @Test
    public void field() {
        System.out.println("field");
    
        Point p1 = new Point(1, 10);
        Point p2 = new Point(2, 20);
        
        Factory<Integer,Point> factoryX = Factories.field(Point.class, "x");
        Factory<Integer,Point> factoryY = Factories.field(Point.class, "y");
        
        assertEquals(1, factoryX.create(p1));
        assertEquals(10, factoryY.create(p1));
        
        assertEquals(2, factoryX.create(p2));
        assertEquals(20, factoryY.create(p2));
        
    } 
      
    @Test
    public void method() {
        System.out.println("method");
    
        double EPSILON = 0.0000001d;
        
        Point p1 = new Point(1, 10);
        Point p2 = new Point(2, 20);
        
        Factory<Double,Point> factoryX = Factories.method(Point.class, "getX");
        Factory<Double,Point> factoryY = Factories.method(Point.class, "getY");
        
        assertEquals(1, factoryX.create(p1), EPSILON);
        assertEquals(10, factoryY.create(p1), EPSILON);
        
        assertEquals(2, factoryX.create(p2), EPSILON);
        assertEquals(20, factoryY.create(p2), EPSILON);
        
    }
    
    @Test
    public void staticMethod() {
        System.out.println("staticMethod");
                    
        Factory<Integer,String> factory = Factories.method(String.class, StaticMethodTestClass.class, "get");
        //Factory<Integer,String> factory = Factories.<Integer,String>method(String.class, Point.class, "get");
        
        assertEquals(1, factory.create("1"));
        assertEquals(2, factory.create("2"));
        assertEquals(254, factory.create("254"));
    } 
    
    @Test
    public void methodOnObject() {
        System.out.println("staticMethod");
        
        MethodOnObjectClass mooc1 = new MethodOnObjectClass("A");
        MethodOnObjectClass mooc2 = new MethodOnObjectClass("B");
                
        Factory<String,File> factory1 = Factories.method(mooc1, String.class, File.class, "getPath");
        Factory<String,File> factory2 = Factories.method(mooc2, String.class, File.class, "getPath");
        
        File f = new File("/tmp/x.json");
        
        assertEquals("A" + f.getPath(), factory1.create(f));
        assertEquals("B" + f.getPath(), factory2.create(f));        
    }
    
    
    // Private section ---------------------------------------------------------
    
    public static class StaticMethodTestClass {
        
        public static Integer get( String text ) {
            return Integer.parseInt(text);
        }
        
    }
    
    public static class MethodOnObjectClass {
        
        private String prefix;

        public MethodOnObjectClass(String prefix) {
            this.prefix = prefix;
        }
        
        public String getPath(File f) {
            return prefix + f.getPath();
        }
        
    }
    
}
