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

package org.codeviation.commons.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.codeviation.commons.patterns.Filter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.codeviation.commons.utils.CollectionsUtil.*;
import static org.junit.Assert.*;

/**
 *
 * @author phrebejk
 */
public class CollectionsUtilTest {

    public CollectionsUtilTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void removeFilter()  {
        System.out.println("removeFilter");
    
        Collection<Number> toRemove = add(new ArrayList<Number>(), 2, 4, 8, 16);
        Collection<Integer> target = add(new ArrayList<Integer>(), 1, 2, 3, 4, 5, 8);
        
        remove(target, 16, 4);        
        assertEquals(target, add(new ArrayList<Integer>(), 1, 2, 3, 5, 8));
    }
    
    // This just needs to be compilable without warnings
    public void generics() {
        
        List<Number> ln = new ArrayList<Number>();
        List<Integer> li = new ArrayList<Integer>();        
       
        ArrayList<Number> aln = new ArrayList<Number>();
        ArrayList<Integer> ali = new ArrayList<Integer>();        
       
        
        Filter<Number> fn = null;
        Filter<Integer> fi = null;
        
        // Additions
        ln = add(ln, 3, 7.5f, 3.2);
        li = add(li, 3, 5, 8);
        
        ln = add(aln, 3, 7.5f, 3.2);
        li = add(ali, 3, 5, 8);
        
        aln = add(aln, 3, 7.5f, 3.2);
        ali = add(ali, 3, 5, 8);
        
        // Removals
        remove(ln, 3.4f, 5.6, 7);
        remove(ln, fn);
        remove(li, fi);
        
    }
    
}