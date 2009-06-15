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

import org.codeviation.pojson.records.RecordPrimitiveTypes;
import java.io.IOException;
import java.lang.reflect.Modifier;
import org.codeviation.commons.reflect.ClassUtils;
import org.codeviation.pojson.records.RecordFilter;
import org.codeviation.pojson.records.RecordObjectTypes;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Hrebejk
 */
public class BeanTest {
    
    private static String GOLDEN_DEFAULT = null;
    
    public BeanTest() {
        
    }
    
    @BeforeClass
    public static void init() throws IOException {
        GOLDEN_DEFAULT = ClassUtils.getResourceAsString(BeanTest.class, "goldenfiles/DefaultFilter.txt");
        
    }

    @Test
    public void bean() throws IOException {
        System.out.println("bean");

        SomeBean sb = new SomeBean( "ahoj", 3, "cau", "nazddar");

        String s = Pojson.save(sb);

        //System.out.println(s);

        SomeBean sb2 = Pojson.load(SomeBean.class, s);

        assertEquals( "ahoj", sb2.getName());
        assertEquals( 3, sb.getNumber());
        assertNull( sb2.getSomePublicField());
        assertNull( sb2.getSomeTransientField());
    }

    
    @Pojson.ModifierPositive(Modifier.PRIVATE)
    public static class SomeBean {

        private String name;
        private int number;

        public String somePublicField;
        private transient String someTransientField;

        private SomeBean() {
            
        }

        public SomeBean(String name, int number, String somePublicField, String someTransientField) {
            this.name = name;
            this.number = number;
            this.somePublicField = somePublicField;
            this.someTransientField = someTransientField;
        }

        public String getName() {
            return name;
        }

        public int getNumber() {
            return number;
        }

        public String getSomePublicField() {
            return somePublicField;
        }

        public String getSomeTransientField() {
            return someTransientField;
        }


    }


}
