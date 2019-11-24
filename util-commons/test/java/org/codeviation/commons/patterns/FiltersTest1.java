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
package org.codeviation.commons.patterns;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Hrebejk
 */
public class FiltersTest1 {
    
    
    @Test
    public void And() {
        System.out.println("And");
        
        Filter<String> sw =  new StartsWithFilter("S");
        Filter<String> ew =  new EndsWithFilter("E");
        Filter<String> cnt = new ContainsFilter("C");
        
        Filter<String> f;
        boolean result;
        
        f = Filters.<String>And(sw,ew);
        assertEquals(true, f.accept("SAAE"));
        assertEquals(false, f.accept("SAAB"));
        assertEquals(false, f.accept("BAAE"));
        assertEquals(false, f.accept("BAAS"));
        
        f = Filters.<String>And(sw,ew, cnt);
        assertEquals(true, f.accept("SACAE"));
        assertEquals(false, f.accept("SAAE"));
        assertEquals(false, f.accept("SAAB"));
        assertEquals(false, f.accept("SACAB"));
        assertEquals(false, f.accept("BAAE"));
        assertEquals(false, f.accept("BACAE"));
        assertEquals(false, f.accept("BAAS"));
        assertEquals(false, f.accept("BACAS"));
                
        IllegalArgumentException ex = null;        
        try {
            f = Filters.And(sw);
        }
        catch( IllegalArgumentException e) {
            ex = e;
        }
        
        assertNotNull(ex);
        
    } 
    @Test
    public void Or() {
        System.out.println("And");
        
        Filter<String> sw =  new StartsWithFilter("S");
        Filter<String> ew =  new EndsWithFilter("E");
        Filter<String> cnt = new ContainsFilter("C");
        
        Filter<String> f;
        boolean result;
        
        f = Filters.<String>Or(sw,ew);
        assertEquals(true, f.accept("SAAE"));
        assertEquals(true, f.accept("SAAB"));
        assertEquals(true, f.accept("BAAE"));
        assertEquals(false, f.accept("BAAS"));
        
        f = Filters.<String>Or(sw,ew, cnt);
        assertEquals(true, f.accept("SACAE"));
        assertEquals(true, f.accept("SAAE"));
        assertEquals(true, f.accept("SAAB"));
        assertEquals(true, f.accept("SACAB"));
        assertEquals(true, f.accept("BAAE"));
        assertEquals(true, f.accept("BACAE"));
        assertEquals(true, f.accept("BACAS"));
        assertEquals(false, f.accept("BAAS"));
        
        IllegalArgumentException ex = null;
        
        try {
            f = Filters.Or(sw);
        }
        catch( IllegalArgumentException e) {
            ex = e;
        }
        
        assertNotNull(ex);
        
    } 

    @Test
    public void Not() {
        System.out.println("Not");
        
        Filter<String> sw =  new StartsWithFilter("A");
        Filter<String> f = Filters.Not(sw);
        assertEquals(false, f.accept("Ahoj"));
        assertEquals(true, f.accept("Nazdar"));
    } 

    @Test
    public void NotNull() {
        System.out.println("NotNull");
        
        Filter<String> f = Filters.<String>NotNull();        
        assertEquals(true, f.accept("Ahoj"));
        assertEquals(false, f.accept(null));
    }
    
    @Test
    public void IsIn() {
        System.out.println("NotNull");
        
        Filter<String> f = Filters.IsIn("a", "b");
        assertEquals(true, f.accept("a"));
        assertEquals(true, f.accept("b"));
        assertEquals(false, f.accept("c"));
        
        
        f = Filters.IsIn(Arrays.asList("a", "b"));
        assertEquals(true, f.accept("a"));
        assertEquals(true, f.accept("b"));
        assertEquals(false, f.accept("c"));
        
    }
    
    // Private section ---------------------------------------------------------
    
    
    private static class StartsWithFilter implements Filter<String> {

        private static String prefix;

        public StartsWithFilter(String prefix) {
            this.prefix = prefix;
        }
        
        public boolean accept(String text) {
            return text.startsWith(prefix);
        }
        
    }
    
    private static class EndsWithFilter implements Filter<String> {

        private static String suffix;

        public EndsWithFilter(String suffix) {
            this.suffix = suffix;
        }
        
        public boolean accept(String text) {
            return text.endsWith(suffix);
        }
        
    }
    
    private static class ContainsFilter implements Filter<String> {

        private static String cnt;

        public ContainsFilter(String cnt) {
            this.cnt = cnt;
        }
        
        public boolean accept(String text) {
            return text.contains(cnt);
        }
        
    }
    
    
}
