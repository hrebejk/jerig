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
import java.util.List;
import java.util.Map;
import org.codeviation.commons.reflect.ClassUtils;
import org.codeviation.pojson.records.RecordSmall;
import org.codeviation.pojson.records.RecordUntyped;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Hrebejk
 */
public class UntypedTest {
    
    private static String GOLDEN;
    
    public UntypedTest() {
    }
    
    
    @BeforeClass
    public static void init() throws IOException {
        GOLDEN = ClassUtils.getResourceAsString(JsonTypesTest.class, "goldenfiles/Untyped.txt");
    }

    @Test
    public void saveUntyped() throws IOException {

        System.out.println("saveUntyped");

        List<RecordSmall> l = new ArrayList<RecordSmall>();
        l.add(new RecordSmall(1, "A"));
        l.add(new RecordSmall(2, "B"));
        l.add(new RecordSmall(3, "C"));

        RecordSmall rs = new RecordSmall(4, "D");
        RecordUntyped iru = new RecordUntyped("ahoj", rs);

        RecordUntyped ru = new RecordUntyped(l, iru);

        String s = Pojson.save(ru);
        
        assertEquals(GOLDEN, s);

    }


    @Test
    public void loadUntyped() throws IOException {
        System.out.println("loadUntyped");

        RecordUntyped ru = Pojson.load(RecordUntyped.class, GOLDEN);

        assertTrue(ru.o1 instanceof List );
        assertTrue(ru.o2 instanceof Map );
        
    }

}
