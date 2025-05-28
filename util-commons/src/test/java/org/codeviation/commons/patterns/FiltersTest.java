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
import java.util.function.Predicate;
// import org.codeviation.commons.patterns.Filters; // No longer needed
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Petr Hrebejk
 */
public class FiltersTest {
    
    
    @Test
    public void And() {
        System.out.println("And");
        
        Predicate<String> sw =  new StartsWithFilter("S");
        Predicate<String> ew =  new EndsWithFilter("E");
        Predicate<String> cnt = new ContainsFilter("C");
        
        Predicate<String> f;
        boolean result;
        
                
        f = sw.and(ew); // Filters.And(sw,ew)
        assertEquals(true, f.test("SAAE"));
        assertEquals(false, f.test("SAAB"));
        assertEquals(false, f.test("BAAE"));
        assertEquals(false, f.test("BAAS"));
        
        f = sw.and(ew).and(cnt); // Filters.And(sw,ew, cnt)
        assertEquals(true, f.test("SACAE"));
        assertEquals(false, f.test("SAAE"));
        assertEquals(false, f.test("SAAB"));
        assertEquals(false, f.test("SACAB"));
        assertEquals(false, f.test("BAAE"));
        assertEquals(false, f.test("BACAE"));
        assertEquals(false, f.test("BAAS"));
        assertEquals(false, f.test("BACAS"));
                
        IllegalArgumentException ex = null;        
        try {
            // f = Filters.And(sw); // This was testing for minimum 2 args, Predicate.and doesn't have this restriction directly.
            // The original Filters.And threw an IllegalArgumentException if filters.length < 2.
            // This specific test case for Filters.And(sw) might need to be rethought or removed
            // as Predicate.and() with a single predicate is just the predicate itself.
            // For now, I'll comment out this specific test part. If Filters.And is to be removed, this test is moot.
            // If the test was "at least two filters needed", it's implicitly handled by using .and() which requires two.
            // For a single predicate, one would just use the predicate directly.
            // f = sw; //  This would be the equivalent if only one was passed.
            // This test was for the Filters.And method itself, which we are removing.
            // So, this specific exception test is no longer applicable.
        }
        catch( IllegalArgumentException e) {
            ex = e;
        }
        
        // assertNotNull(ex); // This assertion is no longer valid as the tested condition was for the old Filters.And() method.
        
    } 
    
    @Test
    public void Or() {
        System.out.println("Or");
        
        Predicate<String> sw =  new StartsWithFilter("S");
        Predicate<String> ew =  new EndsWithFilter("E");
        Predicate<String> cnt = new ContainsFilter("C");
        
        Predicate<String> f;
        boolean result;
        
        f = sw.or(ew); // Filters.Or(sw,ew)
        assertEquals(true, f.test("SAAE"));
        assertEquals(true, f.test("SAAB"));
        assertEquals(true, f.test("BAAE"));
        assertEquals(false, f.test("BAAS"));
        
        f = sw.or(ew).or(cnt); // Filters.Or(sw,ew, cnt)
        assertEquals(true, f.test("SACAE"));
        assertEquals(true, f.test("SAAE"));
        assertEquals(true, f.test("SAAB"));
        assertEquals(true, f.test("SACAB"));
        assertEquals(true, f.test("BAAE"));
        assertEquals(true, f.test("BACAE"));
        assertEquals(true, f.test("BACAS"));
        assertEquals(false, f.test("BAAS"));
        
        IllegalArgumentException ex = null;
        
        try {
            // f = Filters.Or(sw); // Similar to .And(), this test for the Filters class method is no longer directly applicable.
            // f = sw; // would be the equivalent
            // This test was for the Filters.Or method itself.
        }
        catch( IllegalArgumentException e) {
            ex = e;
        }
        
        // assertNotNull(ex); // This assertion is no longer valid as the tested condition was for the old Filters.Or() method.
        
    } 

    @Test
    public void Not() {
        System.out.println("Not");
        
        Predicate<String> sw =  new StartsWithFilter("A");
        Predicate<String> f = sw.negate(); // Filters.Not(sw)
        assertEquals(false, f.test("Ahoj"));
        assertEquals(true, f.test("Nazdar"));
    } 

    @Test
    public void NotNull() {
        System.out.println("NotNull");
        
        Predicate<String> f = java.util.Objects::nonNull; // Filters.NotNull()
        assertEquals(true, f.test("Ahoj"));
        assertEquals(false, f.test(null));
    }
    
    @Test
    public void IsIn() {
        System.out.println("IsIn");
        
        Predicate<String> f = x -> Arrays.asList("a", "b").contains(x); // Filters.IsIn("a", "b")
        assertEquals(true, f.test("a"));
        assertEquals(true, f.test("b"));
        assertEquals(false, f.test("c"));
        
        
        // This can be Collection::contains if the collection is stable and defined elsewhere.
        // Here, Arrays.asList("a", "b") creates a new list each time.
        // For direct replacement:
        Predicate<String> f2 = x -> Arrays.asList("a", "b").contains(x); // Filters.IsIn(Arrays.asList("a", "b"))
        // Or, if the list is a field or effectively final variable `myList`: myList::contains
        assertEquals(true, f2.test("a"));
        assertEquals(true, f2.test("b"));
        assertEquals(false, f2.test("c"));
        
    }
    
    @Test
    public void IncludeExclude() {
        System.out.println("IncludeExclude");
        
        Predicate<String> sw =  new StartsWithFilter("S");
        Predicate<String> ew =  new EndsWithFilter("E");
        
        Predicate<String> f = sw.and(ew.negate()); // Filters.IncludeExclude(sw, ew)
        boolean result;
        
        assertEquals(false, f.test("SAAE"));
        assertEquals(true, f.test("SAAB"));
        assertEquals(false, f.test("BAAE"));
        assertEquals(false, f.test("BAAS"));
        
    }
    
    // Private section ---------------------------------------------------------
    
    
    private static class StartsWithFilter implements Predicate<String> { 

        private String prefix; // Made non-static

        public StartsWithFilter(String prefix) {
            this.prefix = prefix;
        }
        
        public boolean test(String text) { 
            if (text == null) return false; 
            return text.startsWith(prefix);
        }
        
    }
    
    private static class EndsWithFilter implements Predicate<String> { 

        private String suffix; // Made non-static

        public EndsWithFilter(String suffix) {
            this.suffix = suffix;
        }
        
        public boolean test(String text) { 
            if (text == null) return false; 
            return text.endsWith(suffix);
        }
        
    }
    
    private static class ContainsFilter implements Predicate<String> { 

        private String cnt; // Made non-static

        public ContainsFilter(String cnt) {
            this.cnt = cnt;
        }
        
        public boolean test(String text) { 
            if (text == null) return false; 
            return text.contains(cnt);
        }
        
    }
    
    
}
