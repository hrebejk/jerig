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

import org.codeviation.pojson.records.RecordComplex;
import java.io.IOException;
import org.codeviation.commons.reflect.ClassUtils;
import org.codeviation.pojson.records.RecordSmall;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Hrebejk
 */
public class ComplexTest {

    private static String GOLDEN;
    
    public ComplexTest() {
    }

    @BeforeClass
    public static void init() throws IOException {
        GOLDEN = ClassUtils.getResourceAsString(JsonTypesTest.class, "goldenfiles/Complex.txt");
    }
    
    @Test
    public void complexSave() throws IOException {
        System.out.println("complexSave");
        
        assertEquals( GOLDEN, Pojson.save(new RecordComplex().init()));
    } 

    
    @Test
    public void complexUnindentedSave() throws IOException {
        System.out.println("complexUnindentedSave");
        
        Marshaller<RecordComplex> m = new Marshaller<RecordComplex>(null, 0);
        assertEquals( Util.removeFormating(GOLDEN), m.save(new RecordComplex().init()));
        
    }

    @Test
    public void complexLoad() throws IOException {
        System.out.println("complexLoad");

        RecordComplex c1 = new RecordComplex().init();
        String s1 = Pojson.save(c1);

        RecordComplex c2 = Pojson.load(RecordComplex.class, s1);

        assertEquals(s1, Pojson.save(c2));
        assertEquals(c1.primitives.fCharacter, c2.primitives.fCharacter );

        c2 = new RecordComplex();
        Pojson.update(c2,s1);
        assertEquals(s1, Pojson.save(c2));


    }


     @Test
     public void deepLoad() throws IOException {
        System.out.println("deepLoad");

        RecordSmall c1 = new RecordSmall( 1, "A",
                new RecordSmall( 2 , "B",
                    new RecordSmall(3, "C",
                        new RecordSmall( 4, "D",
                            new RecordSmall( 5, "E",
                                new RecordSmall(6, "F"))))));
        String s1 = Pojson.save(c1);

        RecordSmall c2 = Pojson.load(RecordSmall.class, s1);
        
        assertEquals(s1, Pojson.save(c2));

        c2 = new RecordSmall();
        Pojson.update(c2, s1);
        assertEquals(s1, Pojson.save(c2));
    }

    
}
