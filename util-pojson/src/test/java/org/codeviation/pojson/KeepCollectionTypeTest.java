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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.codeviation.commons.reflect.ClassUtils;
import org.codeviation.pojson.records.RecordComplex;
import org.codeviation.pojson.records.RecordObjectTypes;
import org.codeviation.pojson.records.RecordPrimitiveTypes;
import org.codeviation.pojson.records.RecordSmall;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Hrebejk
 */
public class KeepCollectionTypeTest {
    
    public KeepCollectionTypeTest() {
    }
    
    
    @BeforeClass
    public static void init() throws IOException {
    }

    @Test
    public void testDirectField() throws IOException {
        System.out.println("primitive");

        TR1 tr1 = new TR1().init();
        String s1 = Pojson.save(tr1);

        TR1 o = Pojson.load( TR1.class, s1);
        assertTrue( o.als instanceof ArrayList );
        assertTrue( o.lls instanceof LinkedList );
        assertTrue( o.hms instanceof HashMap );
        assertTrue( o.lms instanceof LinkedHashMap );
        assertTrue( o.tms instanceof TreeMap );
    }


    public static class TR1 {

        ArrayList<String> als;
        LinkedList<String> lls;

        HashMap<String,String> hms;
        LinkedHashMap<String,String> lms;
        TreeMap<String,String> tms;


        TR1 init() {
            als = new ArrayList<>();
            lls = new LinkedList<>();
            hms = new HashMap<>();
            lms = new LinkedHashMap<>();
            tms = new TreeMap<>();

            return this;
        }

    }


}
