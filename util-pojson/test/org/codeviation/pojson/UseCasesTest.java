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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.codeviation.commons.utils.CollectionsUtil;
import org.codeviation.pojson.records.RecordComplex;
import org.codeviation.pojson.records.RecordExtendsPrimitiveTypes;
import org.codeviation.pojson.records.RecordPrimitiveTypes;
import org.junit.Test;

/** This test is here to demostrate how to use pojson to read/write
 *  from/to various data sources. Streams, String, Files and URLs. It also shows
 *  how to split objects into more files etc.
 *
 * @author Petr Hrebejk
 */
public class UseCasesTest {
    
    
    public UseCasesTest() {
    }
        
    @Test
    public void stringsUntyped() throws IOException {
        System.out.println("stringsUntyped");
        
        String buffer;
        
        PojsonSave save = PojsonSave.create(); // XXX todo
        PojsonLoad load = PojsonLoad.create(); // XXX todo
        
        // Plain object
        RecordPrimitiveTypes r = new RecordPrimitiveTypes();
        buffer = save.asString(r);
        RecordPrimitiveTypes rLoaded = load.load(buffer, RecordPrimitiveTypes.class);

//        // More objects as JSON array
        RecordPrimitiveTypes r1 = new RecordPrimitiveTypes();        
        RecordPrimitiveTypes r2 = new RecordPrimitiveTypes();        
        RecordPrimitiveTypes r3 = new RecordPrimitiveTypes();                
        buffer = save.asString(r1, r2, r3);
        Iterator<RecordPrimitiveTypes> rsLoaded = load.loadArray(buffer, RecordPrimitiveTypes.class);
        ArrayList<RecordPrimitiveTypes> al = CollectionsUtil.add(new ArrayList<RecordPrimitiveTypes>(), rsLoaded);
        
        // Iterable as JSON Array
        List<RecordPrimitiveTypes> recordsIterable = new LinkedList<RecordPrimitiveTypes>();
        buffer = save.asString(recordsIterable);                        
        Iterator<RecordPrimitiveTypes> itLoaded = load.loadArray(buffer, RecordPrimitiveTypes.class);
        
        // Array as JSON array
        RecordPrimitiveTypes recordsArray[] = new RecordPrimitiveTypes[] {r, r1, r2, r3};
        buffer = save.asString((Object[])recordsArray); // Cast necessary
        Iterator<RecordPrimitiveTypes> arLoaded = load.loadArray(buffer, RecordPrimitiveTypes.class);
        
        // assertEquals(s1, s2);                
    } 
    
    
    @Test
    public void stringsTyped() throws IOException {
        System.out.println("stringsTyped");

        String buffer;
        
        PojsonSave<RecordPrimitiveTypes> save = PojsonSave.create( RecordPrimitiveTypes.class );
        PojsonLoad load = PojsonLoad.create();
        
        RecordPrimitiveTypes record = new RecordPrimitiveTypes();
        
        // Plain object
        RecordPrimitiveTypes r = new RecordPrimitiveTypes();        
        buffer = save.asString(r);                        
        RecordPrimitiveTypes rLoaded = load.load(buffer, RecordPrimitiveTypes.class);

        // More objects as JSON array
        RecordPrimitiveTypes r1 = new RecordPrimitiveTypes();
        RecordPrimitiveTypes r2 = new RecordPrimitiveTypes();
        RecordPrimitiveTypes r3 = new RecordPrimitiveTypes();
        buffer = save.asString(r1, r2, r3);
        Iterator<RecordPrimitiveTypes> rsLoaded = load.loadArray(buffer, RecordPrimitiveTypes.class);
                
        // Iterable as JSON Array
        List<RecordPrimitiveTypes> recordsIterable = new LinkedList<RecordPrimitiveTypes>();
        buffer = save.asString(recordsIterable);                        
        Iterator<RecordPrimitiveTypes> itLoaded = load.loadArray(buffer, RecordPrimitiveTypes.class);
        
        // Array as JSON array
        RecordPrimitiveTypes recordsArray[] = new RecordPrimitiveTypes[] {r, r1, r2, r3};
        buffer = save.asString(recordsArray); 
        Iterator<RecordPrimitiveTypes> arLoaded = load.loadArray(buffer, RecordPrimitiveTypes.class);
                
    }
 
    @Test
    public void variousObjects() {
        System.out.println("variousObjects");
        
        PojsonSave save = PojsonSave.create();
        PojsonLoad load = PojsonLoad.create();
        
        RecordPrimitiveTypes rpt = null;
        RecordExtendsPrimitiveTypes erpt = null;
        RecordComplex rc = null;
        RecordArrays ra = null;
        
        String buffer = save.asString(rpt, erpt, rc, ra);
        Iterator it = load.loadArray(buffer);
                
    }
    
    
}
