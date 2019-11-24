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

package org.codeviation.commons.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.codeviation.commons.patterns.Filter;

/**
 *
 * @author phrebejk
 */
public class FieldUtilsTest {
    
    public FieldUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getDeclared() {
        System.out.println("getDeclared");
        
        Filter<Field> filter = null;
        Set<String> result = fields2names(
                FieldUtils.getDeclared(TestRecord2.class, null));
        Set<String> expResult = createStringSet(
                "ext_pub", "ext_pro", "ext_pkg", "ext_pri", "ext_pri_sta", "ext_pri_tra", 
                "oPri", "oPkg", "oPro", "oPub");        
        assertEquals(expResult, result);        
    }
    
    
    @Test
    public void getAll() {
        System.out.println("getAll");
        
        Filter<Field> filter = null;
        Set<String> result = fields2names(
                FieldUtils.getAll(TestRecord2.class, null, null).values());
        Set<String> expResult = createStringSet(
                "pub", "pro", "pkg", "pri", "pri_sta", "pri_tra", 
                "ext_pub", "ext_pro", "ext_pkg", "ext_pri", "ext_pri_sta", "ext_pri_tra", 
                "oPri", "oPkg", "oPro", "oPub");        
        assertEquals(expResult, result);        
    }
    
    @Test
    public void getAllStopAt() {
        System.out.println("getAllStopAt");
        
        Filter<Field> filter = null;
        Set<String> result = fields2names(
                FieldUtils.getAll(TestRecord3.class, TestRecord2.class, null).values());
        Set<String> expResult = createStringSet(                
                "ext_pub", "ext_pro", "ext_pkg", "ext_pri", "ext_pri_sta", "ext_pri_tra", 
                "oPri", "oPkg", "oPro", "oPub",
                "ext_ext_pub", "ext_ext_pro", "ext_ext_pkg", "ext_ext_pri", "ext_ext_pri_sta", "ext_ext_pri_tra" );
                
        assertEquals(expResult, result);        
    }
    
    @Test
    public void modifierFilterPositive() {
        System.out.println("modifierFilterPositive");

        Filter<Field> f = FieldUtils.modifierFilterPositive(
                Modifier.PRIVATE, Modifier.TRANSIENT );        
        Set<String> result = fields2names(
                FieldUtils.getDeclared(TestRecord1.class, f));
        Set<String> expResult = createStringSet("pri_tra");                
        assertEquals(expResult, result);
    } 
    
    @Test
    public void modifierFilterNegative() {
        System.out.println("modifierFilterNegative");
        
        Filter<Field> f = FieldUtils.modifierFilterNegative(
                Modifier.PRIVATE, Modifier.TRANSIENT );        
        Set<String> result = fields2names(
                FieldUtils.getDeclared(TestRecord1.class, f));
        Set<String> expResult = createStringSet(
                "pub", "pro", "pkg", "oPub", "oPro", "oPkg" );                
        assertEquals(expResult, result);
    } 
 
    
    // Private -----------------------------------------------------------------
    
    private Set<String> fields2names( Collection<Field> fields) {
        Set<String> result = new HashSet<String>();
        for (Field field : fields) {
            result.add( field.getName() ); 
        }
        return result;
    }
    
    private Set<String> createStringSet( Object... params) {
        Set<String> result = new HashSet<String>(params.length);
        for (Object o : params) {
            result.add(o.toString()); 
        }
        return result;
    }
    
}
