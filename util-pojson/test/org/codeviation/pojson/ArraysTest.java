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
import org.codeviation.commons.reflect.ClassUtils;
import org.codeviation.commons.utils.CollectionsUtil;
import org.codeviation.pojson.records.RecordSmall;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Hrebejk
 */
public class ArraysTest {
    
    private static String GOLDEN;
    
    public ArraysTest() {
    }

    @BeforeClass
    public static void init() throws IOException {
        GOLDEN = ClassUtils.getResourceAsString(JsonTypesTest.class, "goldenfiles/Arrays.txt");
    }
    
    @Test
    public void arraysSave() throws IOException {
        System.out.println("arraysSave");
        assertEquals( GOLDEN, Pojson.save(new RecordArrays().init()));
    }
    
    @Test
    public void arraysUnindentedSave() throws IOException {
        System.out.println("arraysUnindentedSave");
        
        Marshaller<RecordArrays> m = new Marshaller<RecordArrays>(null, 0);
        assertEquals( Util.removeFormating(GOLDEN), m.save(new RecordArrays().init()));
    }
    
    @Test
    public void arraysPureUnindentedSave() throws IOException {
        System.out.println("arraysPureUnindentedSave");
        
        Marshaller<Object> m = new Marshaller<Object>(null, 0);
        assertEquals( "[1,2,3]", m.save(new int[]{1,2,3}));
        assertEquals( "[1,2,3]", m.save(CollectionsUtil.arrayList(1,2,3)));
                
    }

    @Test
    public void arraysMultidimensionalSave() throws IOException {
        System.out.println("arraysSaveMultiDimensional");

        Marshaller<Object> m = new Marshaller<Object>(null, 0);
        assertEquals( "[[1,2,3],[1,2,3]]", m.save(new int[][]{ { 1,2,3 }, { 1, 2, 3} }));
        assertEquals( "[[1,2,3],[1,2,3]]", m.save( CollectionsUtil.arrayList(
                                                       CollectionsUtil.arrayList(1,2,3),
                                                       CollectionsUtil.arrayList(1,2,3))));
    }


     @Test
    public void arraysLoad() throws IOException {
        System.out.println("arraysLoad");


        RecordArrays a1 = new RecordArrays().init();
        String s1 = Pojson.save(a1);

        RecordArrays a2 = Pojson.load( RecordArrays.class, s1);
        assertEquals(s1, Pojson.save(a2));

        a2 = new RecordArrays();
        Pojson.update(a2,s1);
        assertEquals(s1, Pojson.save(a2));

    }
    

    @Test
    public void arraysIntegerLoad() throws IOException {
        System.out.println("arraysIntegerLoad");
                
        Integer[] i1 = new Integer[] {1,2,3,900000};
        String s1 = Pojson.save(i1);

        Integer[] i2 = Pojson.load(Integer[].class, s1);
        String s2 = Pojson.save(i2);

        assertEquals( s1, s2 );

        // XXX update

    }

    @Test
    public void arraysLong() throws IOException {
        System.out.println("arraysLong");

        
        Long[] l1 = new Long[] {1l,2l,3l,900000l};
        String s1 = Pojson.save(l1);

        Long[] l2 = Pojson.load(Long[].class, s1);
        assertEquals( s1, Pojson.save(l2) );
        
        Long[] l3 = Pojson.update(new Long[0], s1);

        assertEquals( s1, Pojson.save(l3) );

    }

    @Test
    public void arraysFloatLoad() throws IOException {
        System.out.println("arraysFloat");

        

        Float[] f1 = new Float[] {1.0f,2.0f,3.0f,900000.783293289f};
        String s1 = Pojson.save(f1);

        Float[] f2 = Pojson.load(Float[].class, s1);
        assertEquals( s1, Pojson.save(f2) );

        Float[] f3 = Pojson.update(new Float[0], s1);

        assertEquals( s1, Pojson.save(f3) );


    }

    @Test
    public void arraysDoubleLoad() throws IOException {
        System.out.println("arraysDoubleLoad");

        Double[] d1 = new Double[] {1.0,2.0,3.0,900000.783293289};
        String s1 = Pojson.save(d1);

        Double[] d2 = Pojson.load(Double[].class, s1);
        assertEquals( s1, Pojson.save(d2) );

        Double[] d3 = Pojson.update(new Double[0], s1);
        assertEquals( s1, Pojson.save(d3) );

    }

    @Test
    public void arraysObjectLoad() throws IOException {
        System.out.println("arraysObjectLoad");

        RecordSmall[] d1 = new RecordSmall[] {
            new RecordSmall(1, "A"),
            new RecordSmall(2, "B"),
            new RecordSmall(3, "C")
        };
        String s1 = Pojson.save(d1);

        System.out.println(s1);



        RecordSmall[] d2 = Pojson.load(RecordSmall[].class, s1);
        String s2 = Pojson.save(d2);

        RecordSmall[] d3 = Pojson.update(new RecordSmall[0], s1);
        assertEquals( s1, Pojson.save(d3) );

        assertEquals( s1, s2 );

    }

    @Test
    public void arraysMultidimensionalLoad() throws IOException {
        System.out.println("arraysMultiDimensionalLoad");


        String s = Pojson.save(new int[][]{ { 1,2,3 }, { 1, 2, 3} });

        Integer[][] i1 = Pojson.load(Integer[][].class, s);

        assertEquals(s, Pojson.save(i1));
    }

}
