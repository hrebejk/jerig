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

import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.codeviation.commons.reflect.ClassUtils;
import org.codeviation.commons.utils.StreamUtil;
import org.codeviation.pojson.records.RecordArrays;
import org.codeviation.pojson.records.RecordComplex;
import org.codeviation.pojson.records.RecordPrimitiveTypes;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phrebejk
 */
public class ZipSaverTest {

    public ZipSaverTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    @Test 
    public void all() throws Exception {
        File file = File.createTempFile("ZipSaver", "Test.zip");
        System.out.println("all : " + file.getPath());
                
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
        
        RecordPrimitiveTypes rpt = new RecordPrimitiveTypes();
        RecordArrays ra = new RecordArrays();
        RecordComplex rc = new RecordComplex();
        
        ZipSaver zs = new ZipSaver(zos);
        
        zs.save("primitive/r.json", rpt);
        zs.save("arrays/r.json", ra);
        zs.save("complex/r.json", rc);
        
        zos.close();
        
        ZipFile zf = new ZipFile(file);

        assertEquals( 
            ClassUtils.getResourceAsString(ZipSaverTest.class, "goldenfiles/JsonTypes.txt"),
            StreamUtil.asString(zf.getInputStream(zf.getEntry("primitive/r.json"))));
        
        assertEquals( 
            ClassUtils.getResourceAsString(ZipSaverTest.class, "goldenfiles/Arrays.txt"),
            StreamUtil.asString(zf.getInputStream(zf.getEntry("arrays/r.json"))));
        
        assertEquals( 
            ClassUtils.getResourceAsString(ZipSaverTest.class, "goldenfiles/Complex.txt"),
            StreamUtil.asString(zf.getInputStream(zf.getEntry("complex/r.json"))));
        
    }

}