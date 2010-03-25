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

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.codeviation.commons.patterns.Factory;

/**
 *
 * @author phrebejk
 */
public class Iterators {

    private Iterators() {}

    public static <T> Iterator<T> array( T[] array ) {
        return new ArrayIterator<T>(array);
    }

    public static <T> Iterator<T> sequence(Iterator<? extends T>... iterators) {
        return new SequenceIterator<T>(iterators);
    }

    public static <T,P> Iterator<T> translating(Iterator<P> iterator, Factory<T,P> factory) {
        return new TranslatingIterator<T, P>(iterator, factory);
    }
    
    public static <T,P> Iterator<T> translating(Iterable<P> iterable, Factory<T,P> factory) {
        return new TranslatingIterator<T, P>(iterable.iterator(), factory);
    }

    public static <P> Iterator<P> enumeration(Enumeration<P> enumeration ) {
        return new EnumerationIterator<P>(enumeration);
    }
    
    private static class TranslatingIterator<T,P> implements Iterator<T> {

        private Iterator<P> iterator;
        private Factory <T,P> factory;

        public TranslatingIterator(Iterator<P> iterator, Factory<T,P> factory) {
            this.iterator = iterator;
            this.factory = factory;
        }
                        
        public boolean hasNext() {
            return iterator.hasNext();
        }

        public T next() {
            return factory.create(iterator.next());
        }

        public void remove() {
            iterator.remove();
        }
        
    }

    private static class EnumerationIterator<P> implements Iterator<P> {

        private Enumeration<P> enumeration;


        public EnumerationIterator(Enumeration<P> enumeration ) {
            this.enumeration = enumeration;
        }

        public boolean hasNext() {
            return enumeration.hasMoreElements();
        }

        public P next() {
            return enumeration.nextElement();
        }

        public void remove() {
            throw new UnsupportedOperationException("Enumeration iterator does not support removals.");
        }

    }

    private static class SequenceIterator<T> implements Iterator<T> {

        private Iterator<Iterator<? extends T>> itit;
        private Iterator<? extends T> current;

        public SequenceIterator(Iterator<? extends T>... its) {

            if ( its == null ) {
                throw new NullPointerException();
            }

            if ( its.length > 0 ) {
                this.itit = Arrays.asList(its).iterator();
                this.current = its[0];
            }
            else {
                this.itit = Collections.EMPTY_LIST.iterator();
                this.current = Collections.EMPTY_LIST.iterator();
            }
        }

        public boolean hasNext() {
            if ( current.hasNext() ) {
                return true;
            }

            if ( itit.hasNext() ) {
                current = itit.next();
                return hasNext();
            }

            return false;
        }

        public T next() {
            if ( !hasNext() ) {
                throw new NoSuchElementException();
            }

            return current.next();

        }

        public void remove() {
            current.remove();
        }

    }

     private static class ArrayIterator<T> implements Iterator<T> {

        private T[] array;
        private int size = 0;
        private int index = 0;

        public ArrayIterator(T[] array) {
            this( array, array.length );
        }

        public ArrayIterator(T[] array, int size) {
            this.array = array;
            this.size = size;
        }

        public boolean hasNext() {
            return index < size;
        }

        public T next() {
            if ( !hasNext() ) {
                throw new NoSuchElementException();
            }
            return array[index++];
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
    
}
