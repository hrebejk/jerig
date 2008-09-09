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

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phrebejk
 */
public class CachesTest {
    
    public CachesTest() {
    }

    @Test
    public void permanent() {
        System.out.println("permanent");
        
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "jedna");
        map.put(2, "dve");
        map.put(3, "tri");
        map.put(4, "ctyri");
        map.put(5, "pet");
                
        Factory<String , Integer> producer = Factories.fromMap(map);
        Cache<String,Integer> cache = Caches.permanent(producer);
        
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            assertEquals(entry.getValue(), cache.create(entry.getKey()));
        }
        
        Caches.Statistics stats = Caches.STATISTICS_FACTORY.create(cache);
        assertEquals(5l, stats.misses);
        assertEquals(5l, stats.queries);
        
        cache.clear();
        
        assertEquals("jedna", cache.create(1));
        stats = Caches.STATISTICS_FACTORY.create(cache);
        assertEquals(1l, stats.misses);
        assertEquals(1l, stats.queries);
        
        assertEquals("jedna", cache.create(1));
        stats = Caches.STATISTICS_FACTORY.create(cache);
        assertEquals(1l, stats.misses);
        assertEquals(2l, stats.queries);
        
        assertEquals("ctyri", cache.create(4));
        stats = Caches.STATISTICS_FACTORY.create(cache);
        assertEquals(2l, stats.misses);
        assertEquals(3l, stats.queries);
        
        assertEquals("ctyri", cache.create(4));
        stats = Caches.STATISTICS_FACTORY.create(cache);
        assertEquals(2l, stats.misses);
        assertEquals(4l, stats.queries);
        
        assertEquals("jedna", cache.create(1));
        stats = Caches.STATISTICS_FACTORY.create(cache);
        assertEquals(2l, stats.misses);
        assertEquals(5l, stats.queries);
        
    } 
    
}
