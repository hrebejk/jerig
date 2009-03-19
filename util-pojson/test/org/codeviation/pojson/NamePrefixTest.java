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
public class NamePrefixTest {
    
    private static String GOLDEN;

    public NamePrefixTest() {
    }

    @BeforeClass
    public static void init() throws IOException {
        GOLDEN = ClassUtils.getResourceAsString(JsonTypesTest.class, "goldenfiles/ComplexNamePrefix.txt");
    }

    @Test
    public void namePrefixSave() throws IOException {
        System.out.println("namePrefixSave");

        assertEquals( GOLDEN, Pojson.save(new NPComplex().init()));
    }


   // @Test
    public void namePrefixUnindentedSave() throws IOException {
        System.out.println("namePrefixUnindentedSave");

        Marshaller<NPComplex> m = new Marshaller<NPComplex>(null, 0);
        assertEquals( Util.removeFormating(GOLDEN), m.save(new NPComplex().init()));

    }

   // @Test
    public void namePrefixLoad() throws IOException {
        System.out.println("namePrefixLoad");

        RecordComplex c1 = new RecordComplex().init();
        String s1 = Pojson.save(c1);

        RecordComplex c2 = Pojson.load(RecordComplex.class, s1);

        assertEquals(s1, Pojson.save(c2));
        assertEquals(c1.primitives.fCharacter, c2.primitives.fCharacter );

        c2 = new RecordComplex();
        Pojson.update(c2,s1);
        assertEquals(s1, Pojson.save(c2));


    }

    
    @Pojson.StopAt(RecordComplex.class)
    @Pojson.NamePrefix("COMPLEX_")
    public static class NPComplex extends RecordComplex {

        @Override
        public NPComplex init() {
            primitives = new NPPrimitives().init();
            objects = new NPObjects().init();
            arrays = new NPArrays().init();
            return this;
        }
    }

    @Pojson.StopAt(RecordPrimitiveTypes.class)
    @Pojson.NamePrefix("PRIMITIVES_")
    public static class NPPrimitives extends RecordPrimitiveTypes {

    }

    @Pojson.StopAt(RecordObjectTypes.class)
    @Pojson.NamePrefix("OBJECTS_")
    public static class NPObjects extends RecordObjectTypes {

    }

    @Pojson.StopAt(RecordArrays.class)
    @Pojson.NamePrefix("ARRAYS_")
    public static class NPArrays extends RecordArrays {

    }



}
