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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Hrebejk
 */
public class CycleDetectionTest {
    
    
    public CycleDetectionTest() {
        
    }
    
    @BeforeClass
    public static void init() throws IOException {
    
    }



    @Test
    public void easyCycle() throws IOException {
        System.out.println("easyCycle");

        R r1 = new R();
        r1.r = r1;

        Exception e  = null;

        try {
            Pojson.save(r1);
        }
        catch ( IllegalArgumentException ex) {
            e = ex;
        }

        assertNotNull(e);

    }

    @Test
    public void easyCycle2() throws IOException {
        System.out.println("easyCycle2");

        R r1 = new R();
        r1.r = new R();
        r1.r.r = r1;
        
        Exception e  = null;

        try {
            Pojson.save(r1);
        }
        catch ( IllegalArgumentException ex) {
            e = ex;
        }

        assertNotNull(e);

    }

    @Test
    public void twoBranchesOK() throws IOException {
        System.out.println("twoBranchesOK");

        R r1 = new R();
        R r2 = new R();
        r1.r = new R();

        r1.r = r2;
        r1.k = r2;

        Exception e  = null;

        try {
            Pojson.save(r1);
        }
        catch ( IllegalArgumentException ex) {
            e = ex;
        }

        assertNull(e);

    }

    @Test
    public void map() throws IOException {
        System.out.println("map");

        Map<String,Object> m = new HashMap<String,Object>();
        Map<String,Object> m1 = new HashMap<String,Object>();
        
        
        m.put("m1", m1 );
        m1.put("x", m);
        
        
        Exception e  = null;

        try {
            Pojson.save(m);
        }
        catch ( IllegalArgumentException ex) {
            e = ex;
        }

        assertNotNull(e);

    }

    @Test
    public void array() throws IOException {
        System.out.println("array");

        R r1 = new R();
        r1.a = new R[]{ r1 };

        Exception e  = null;

        try {
            Pojson.save(r1);
        }
        catch ( IllegalArgumentException ex) {
            e = ex;
        }

        assertNotNull(e);

    }

    @Test
    public void list() throws IOException {
        System.out.println("array");

        R r1 = new R();
        r1.l = new ArrayList<R>();
        R r2 = new R();
        r1.l.add(r2);
        r2.l = new ArrayList<R>();
        r2.l.add(r1);

        Exception e  = null;

        try {
            Pojson.save(r1);
        }
        catch ( IllegalArgumentException ex) {
            e = ex;
        }

        assertNotNull(e);

    }

    public static class R {

        R r;
        R k;

        R[] a;
        List<R> l;

    }

}
