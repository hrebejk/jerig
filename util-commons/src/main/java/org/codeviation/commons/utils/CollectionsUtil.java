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

// import org.codeviation.commons.patterns.*; // Filter will be replaced by Predicate
import java.util.function.Predicate; // Added for Predicate
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/** Convenience methods for working with collections.
 *
 * @author Petr Hrebejk
 */
public class CollectionsUtil {
        
    private CollectionsUtil() {}
    
    public static <T> HashSet<T> hashSet(T... params) {
        HashSet<T> s = new HashSet<T>(params.length);
        for (T p : params) {
            s.add(p);
        }
        return s;
    }
    
    public static <T> HashSet<T> hashSetIf(Collection<? extends T> original,  Predicate<T> filter) { // Renamed and Predicate
        HashSet<T> s = new HashSet<T>(original.size());
        for (T p : original) {
            if ( filter.test(p)) { // filter.test
                s.add(p);
            }
        }
        return s;
    }
    
    public static <T,Q extends T> ArrayList<T> arrayList(Q... params) {
        ArrayList<T> s = new ArrayList<T>(params.length);
        for (T p : params) {
            s.add(p);
        }
        return s;
    }
    
    public static <T,Q extends T> ArrayList<T> arrayListIf(Collection<Q> original,  Predicate<T> filter) { // Renamed and Predicate
        ArrayList<T> s = new ArrayList<T>(original.size());
        for (T p : original) {
            if ( filter.test(p)) { // filter.test
                s.add(p);
            }
        }
        return s;
    }

    public static <V> V get( Map m, Class<V> clazz, Object key ) {
        Object o = m.get(key);
        return clazz.isInstance(o) ? clazz.cast(o) : null;
    }
    
    public static <T, C extends Collection<T>, Q extends T> C add(C target, Q... params) {
        for (T p : params) {
            target.add(p);
        }
        
        return target;
    }
    
    public static <T, C extends Collection<T>, Q extends T> C add(C target, Iterator<Q> it) {
        while (it.hasNext()) {
            target.add(it.next());
        }
        
        return target;
    }

    public static <T, C extends Collection<T>, Q extends T> C add(C target, Iterable<Q> it) {
        return add(target, it.iterator());
    }
    
    public static <T, C extends Collection<T>, Q extends Collection<? extends T>> C addIf(C target, Q source, Predicate<? super T> filter) { // Renamed and Predicate
        for (T t : source) {
            if (filter.test(t)) { // filter.test
                target.add(t);
            }
        }

        return target;
    }
        
    public static <T, Q extends T> void remove(Collection<T> target, Q... params) {
        for (T p : params) {
            target.remove(p);
        }
    }
    
    public static <T> void remove(Collection<T> target, Iterator<? extends T> it) {
        while (it.hasNext()) {
            target.remove(it.next());
        }
    }
    
    // Note: The original signature was Collection<Q> remove(Collection<Q> target, Filter<? super T> filter)
    // This could lead to issues if Q is a subtype of T, and filter operates on T.
    // Changing to Predicate<? super Q> makes more sense for Collection<Q> target.
    // Also, removing from a collection while iterating over it with an external iterator
    // is dangerous and can lead to ConcurrentModificationException.
    // The correct way is to use Iterator.remove() or Collection.removeIf().
    // For now, I will rename and change to Predicate, but this method is problematic.
    public static <Q> Collection<Q> removeIf(Collection<Q> target, Predicate<? super Q> filter) { // Renamed and Predicate
        // This implementation is problematic and prone to ConcurrentModificationException.
        // It should be refactored to use target.removeIf(filter) if the Java version allows,
        // or an iterator with iterator.remove().
        Iterator<Q> it = target.iterator();
        while (it.hasNext()) {
            if (filter.test(it.next())) {
                it.remove();
            }
        }
        return target;
    }

    
    public static <T, R extends Collection<T>, Q extends T> R filterCollection(R target, Iterable<Q> it, Predicate<Q> filter) { // Renamed and Predicate
        
        for (Q t : it) {
            if ( filter.test(t)) { // filter.test
                target.add(t);
            }
        }

        return target;
    }
        
}
