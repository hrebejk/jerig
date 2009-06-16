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

package org.codeviation.commons.reflect;

import static org.codeviation.commons.reflect.RecordsNameFactory.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Hrebejk
 */
public class ObjectNameFactoryTest {
            
    @Test
    public void def()  {
        System.out.println("default");

        ObjectNameFactory onf = new ObjectNameFactory();
        Default record = new Default();

        assertEquals( "Default", onf.create(record) );
    }

    @Test
    public void differentId()  {
        System.out.println("differentId");

        ObjectNameFactory onf = new ObjectNameFactory(".json");
        DifferentId record = new DifferentId();

        assertEquals( "nazdar.json", onf.create(record) );
    }

    @Test
    public void multiId()  {
        System.out.println("multiId");

        ObjectNameFactory onf = new ObjectNameFactory(".ext");
        MultiId record = new MultiId();

        assertEquals( "1nazdar.ext", onf.create(record) );
    }


    @Test
    public void extension()  {
        System.out.println("extension");

        ObjectNameFactory onf = new ObjectNameFactory();
        Extension record = new Extension();

        assertEquals( "file.extension", onf.create(record) );
    }

    @Test
    public void parmetrized()  {
        System.out.println("parmetrized");

        ObjectNameFactory onf = new ObjectNameFactory();
        Parametrized record = new Parametrized();

        assertEquals( "1.nazdar", onf.create(record) );
    }

    @Test
    public void parmetrizedOrdered()  {
        System.out.println("parmetrizedOrdered");

        ObjectNameFactory onf = new ObjectNameFactory();
        ParametrizedOrdered record = new ParametrizedOrdered();

        assertEquals( "nazdar-1.ext", onf.create(record) );

    }

    @Test
    public void nullId()  {
        System.out.println("nullId");

        ObjectNameFactory onf = new ObjectNameFactory(".json");
        NullId record = new NullId();

        assertEquals( "1null.json", onf.create(record) );

    }

    @Test
    public void nullIdFormated()  {
        System.out.println("nullIdFormated");

        ObjectNameFactory onf = new ObjectNameFactory();
        NullIdFormated record = new NullIdFormated();

        assertEquals( "1-null.ext", onf.create(record) );

    }

    
}
