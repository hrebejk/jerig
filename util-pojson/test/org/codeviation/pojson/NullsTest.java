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
import org.codeviation.commons.reflect.ClassUtils;
import org.codeviation.pojson.records.RecordArrays;
import org.codeviation.pojson.records.RecordComplex;
import org.codeviation.pojson.records.RecordObjectTypes;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Hrebejk
 */
public class NullsTest {

    private static String GOLDEN_OBJECT_TYPES;
    private static String GOLDEN_ARRAYS;
    private static String GOLDEN_COMPLEX;
    
    public NullsTest() {
    }
    
    @BeforeClass
    public static void init() throws IOException {
        GOLDEN_OBJECT_TYPES = ClassUtils.getResourceAsString(JsonTypesTest.class, "goldenfiles/Nulls.txt");
        GOLDEN_COMPLEX = ClassUtils.getResourceAsString(JsonTypesTest.class, "goldenfiles/NullComplex.txt");
        GOLDEN_ARRAYS = ClassUtils.getResourceAsString(JsonTypesTest.class, "goldenfiles/NullArrays.txt");
    }

    @Test
    public void nullObjectTypes() throws IOException {
        System.out.println("nullObjectTypes");

        assertEquals( GOLDEN_OBJECT_TYPES, Pojson.save(new RecordObjectTypes()));
    } 
    
    @Test
    public void nullArrays() throws IOException {
        System.out.println("nullArrays");

        assertEquals( GOLDEN_ARRAYS, Pojson.save(new RecordArrays()));        
    }
    
    @Test
    public void nullComplex() throws IOException {
        System.out.println("nullComplex");
        
        assertEquals( GOLDEN_COMPLEX, Pojson.save(new RecordComplex()));        
    } 

    
}
