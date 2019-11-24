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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phrebejk
 */
public class ShinglerTest {

    private static long seed;
    private static List<Numbers> randomList;
    int SIZE = 40;
    int P = 3;
    int M = (int)Math.pow(2, 30);
        
    
    public ShinglerTest() {
    }

    @BeforeClass
    public static void init() {
        seed = System.currentTimeMillis();
        randomList = generateRandom(seed, 100);
    }
    
    // Shows why we need to add 1 to ordinals
    @Test 
    public void enumOrdinals() {
        System.out.println("enumOrdinals");
        assertEquals( Numbers.ONE.ordinal(), 0);
    }
    
    // ^ is XOR not power!
    @Test 
    public void squares() {
        System.out.println("sqares");
                
        assertEquals( 27, (int)Math.pow(3, 3));
        assertEquals( 9,  (int)Math.pow(3, 2));
        assertEquals( 3,  (int)Math.pow(3, 1));
        assertEquals( 1,  (int)Math.pow(3, 0));
        
    }
    
    
    @Test
    public void testTheTest() {
        System.out.println("testTheTest");
        
        int size = 4;
        int p = 3;
        int m = 16;
        
        List<Numbers> l = Arrays.asList( Numbers.SEVEN, Numbers.SIX, Numbers.FIVE, Numbers.FOUR, Numbers.THREE, Numbers.TWO, Numbers.ONE );

        long exp = 7 * (p * p * p) + 6 * (p * p) + 5 * (p) + 4 * (1); 
        assertEquals(exp % m, computePolynom(l.subList(0, 0 + size), p, m));

        exp = 6 * (p * p * p) + 5 * (p * p) + 4 * (p) + 3 * (1);
        assertEquals(exp % m, computePolynom(l.subList(1, 1 + size), p, m));
        
        exp = 5 * (p * p * p) + 4 * (p * p) + 3 * (p) + 2 * (1);
        assertEquals(exp % m, computePolynom(l.subList(2, 2 + size), p, m));

        exp = 4 * (p * p * p) + 3 * (p * p) + 2 * (p) + 1 * (1);
        assertEquals(exp % m, computePolynom(l.subList(3, 3 + size), p, m));
        
    }
    
    @Test 
    public void randomFirst() {
        System.out.println("randomFirst seed: " + seed );
        
        Shingler<Numbers> shingler = new Shingler<Numbers>(Numbers.class, SIZE, P, M);
        
        for( int i = 0; i < randomList.size() - SIZE; i++ ) {
            long exp = computePolynom(randomList.subList(i, i + SIZE), P, M);
            //System.out.println(i + "  " + exp );
            assertEquals("index " + i + " ",  exp, shingler.computeFirst(randomList.subList(i, i + SIZE)));
        }
        
    }
    
    
    @Test 
    public void randomNext() {
        System.out.println("randomNext seed: " + seed );
        
        Shingler<Numbers> shingler = new Shingler<Numbers>(Numbers.class, SIZE, P, M);
        
        long exp = computePolynom(randomList.subList(0, SIZE), P, M);
        long first = shingler.computeFirst(randomList.subList(0, SIZE));
        assertEquals(exp, first);
        
        
        for( int i = 1; i < randomList.size() - SIZE; i++ ) {
            exp = computePolynom(randomList.subList(i, i + SIZE), P, M);
            long next = shingler.computeNext(first, randomList.get(i - 1), randomList.get(i - 1  + SIZE) );
            assertEquals(exp, next);
            first = next;
        }
        
    }
    
    @Test
    public void computeRandom() {
        System.out.println("computeRandom - seed:" + seed);
        
        Shingler<Numbers> shingler = new Shingler<Numbers>(Numbers.class, SIZE, P, M);
        
        List<Long> exp = new ArrayList<Long>(randomList.size());
        for( int i = 0; i < randomList.size() - SIZE + 1; i++ ) {
           long l = computePolynom(randomList.subList(i, i + SIZE), P, M);
           exp.add(l);
        }        
        
        List<Long> got = shingler.compute(randomList);
        
        
        assertEquals(exp.size(), got.size() );
        assertEquals(exp, got );
    } 
    
    @Test
    public void computeFirst() {
        System.out.println("compute");
    
        int size = 3;
        int p = 3;
        int m = 3;
        
        Shingler<Numbers> shingler = new Shingler<Numbers>(Numbers.class, size, p, m);
        List<Numbers> l = Arrays.asList( Numbers.FIVE, Numbers.FOUR, Numbers.THREE, Numbers.TWO, Numbers.ONE );
                            
        long exp = 5 * (p * p) + 4 * (p) + 3 * (1); 
        assertEquals(exp % 3, shingler.computeFirst(l.subList(0, 3)));
    } 
    
    @Test
    public void computeNext() {
        System.out.println("computeNext");
    
        int size = 3;
        int p = 3;
        int m = (int)Math.pow(2, 30);
        
        Shingler<Numbers> shingler = new Shingler<Numbers>(Numbers.class, size, p, m);
        List<Numbers> l = Arrays.asList( Numbers.FIVE, Numbers.FOUR, Numbers.THREE, Numbers.TWO, Numbers.ONE );
                            
        long first = shingler.computeFirst(l.subList(0, size));
        assertEquals(computePolynom(l.subList(0,size), p, m), first);
        
        long exp = 4 * (p * p) + 3 * (p) + 2 * (1);
        long next = shingler.computeNext(first, Numbers.FIVE, Numbers.TWO);
        assertEquals(exp % m, next);
        
        next = shingler.computeNext(next, Numbers.FOUR, Numbers.ONE);
        exp = 3 * (p * p) + 2 * (p) + 1 * (1); 
        assertEquals(exp % m, next);
    }
    
    private static enum Numbers {
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN;
    }
    
    private static List<Numbers> generateRandom( long seed, int length ) {
        Numbers values[] = Numbers.values();
        Random r = new Random(seed);
        List<Numbers> result = new ArrayList<Numbers>(length);
        
        for (int i = 0; i < length; i++) {
            result.add(values[r.nextInt(values.length)]);
        }
        
        return result;
    }
    
    private static long computePolynom(List<Numbers> list, int p, int m) {
                        
        BigInteger bigP = BigInteger.valueOf(p);
        BigInteger s = BigInteger.valueOf(0);
        
        for( int i = 0; i < list.size(); i++ ) {
            BigInteger value = BigInteger.valueOf(list.get(i).ordinal() + 1);
            s = s.add(value.multiply(bigP.pow(list.size() - i - 1)));
        }
        
        
        return s.mod(BigInteger.valueOf(m)).longValue();
        
//        long s = 0;
//                
//        for( int i = 0; i < list.size(); i++ ) {
//            int value = list.get(i).ordinal() + 1;
//            s += value * (long)Math.pow( p, list.size() - i -1);
//        }
//        
//        return s % m ;
    }

}