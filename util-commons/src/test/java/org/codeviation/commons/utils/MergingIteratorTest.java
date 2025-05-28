/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phrebejk
 */
public class MergingIteratorTest {

    private static final Comparator<String> S_CMP = new StringComparator();

    public MergingIteratorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    
    @Test
    public void testAllEmpty() {
        System.out.println("allEmpty");
        
        List<String> l1 = new ArrayList<String>();
        List<String> l2 = new ArrayList<String>();
        List<String> l3 = new ArrayList<String>();
                        
        Iterator<String> mi = Iterators.merging(
                S_CMP,
                l1.iterator(),
                l2.iterator(),
                l3.iterator() );

        Iterator<String> mia = Iterators.merging(
                l1.iterator(),
                l2.iterator(),
                l3.iterator() );
        
        assertEmpty(mi);
        assertEmpty(mia);

    }


    @Test
    public void testOneEmpty() {
        System.out.println("oneEmpty");

        List<String> l1 = Arrays.asList( new String[] { "a", "b", "d" } );
        List<String> l2 = Arrays.asList( new String[] {} );
        List<String> l3 = Arrays.asList( new String[] { "x", "y", "z" } );

        Iterator<String> mi = Iterators.merging(
                S_CMP,
                l1.iterator(),
                l2.iterator(),
                l3.iterator() );
        Iterator<String> mia = Iterators.merging(
                l1.iterator(),
                l2.iterator(),
                l3.iterator() );

        List<String> actual = toList(mi);
        List<String> actuala = toList(mia);
        List<String> expected = mergeAndSort(S_CMP, l1, l2, l3);
        
        assertEquals("Should the same", expected, actual );
        assertEquals("Should the same", expected, actuala );

        assertEmpty(mi);
        assertEmpty(mia);

    }


    @Test
    public void testNoneEmpty() {
        System.out.println("noneEmpty");

        List<String> l1 = Arrays.asList( new String[] { "a", "b", "d" } );
        List<String> l2 = Arrays.asList( new String[] { "b", "c", "d" } );
        List<String> l3 = Arrays.asList( new String[] { "x", "y", "z" } );

        Iterator<String> mi = Iterators.merging(
                S_CMP,
                l1.iterator(),
                l2.iterator(),
                l3.iterator() );
        Iterator<String> mia = Iterators.merging(
                l1.iterator(),
                l2.iterator(),
                l3.iterator() );

        List<String> actual = toList( mi );
        List<String> actuala = toList( mia );
        List<String> expected = mergeAndSort(S_CMP, l1, l2, l3);
        
        assertEquals("Should the same", expected, actual );
        assertEquals("Should the same", expected, actuala );

        assertEmpty(mi);
        assertEmpty(mia);

    }

    @Test
    public void testBadSorting() {
        System.out.println("badSorting");

        List<String> l1 = Arrays.asList( new String[] { "a", "b", "d" } );
        List<String> l2 = Arrays.asList( new String[] { "b", "a", "d" } );
        List<String> l3 = Arrays.asList( new String[] { "x", "y", "z" } );

        Iterator<String> mi = Iterators.merging(
                S_CMP,
                l1.iterator(),
                l2.iterator(),
                l3.iterator() );

        Iterator<String> mia = Iterators.merging(
                l1.iterator(),
                l2.iterator(),
                l3.iterator() );


        IllegalStateException ise = null;
        try {
            List<String> actual = toList( mi );
        }
        catch (IllegalStateException ex) {
            ise = ex;
        }

        IllegalStateException isea = null;
        try {
            List<String> actuala = toList( mia );
        }
        catch (IllegalStateException ex) {
            isea = ex;
        }

        assertNotNull("Throw IllegalStateException", ise );
        assertNotNull("Throw IllegalStateException", isea );

        assertEquals("Shoud be correct message", "Iterator 1 bad sorting at element : a", ise.getMessage() );
        assertEquals("Shoud be correct message", "Iterator 1 bad sorting at element : a", isea.getMessage() );

    }

    public static void assertEmpty( Iterator mi ) {

        assertFalse("Should be empty", mi.hasNext() );

        NoSuchElementException nsee = null;
        try {
            mi.next();
        }
        catch( NoSuchElementException ex ) {
            nsee = ex;
        }

        assertNotNull("Should throw exception", nsee );

    }

    public static <T> List<T> toList( Iterator<T> iterator ) {
        List<T> r = new ArrayList();
        while( iterator.hasNext() ) {
            r.add(iterator.next());
        }
        return r;
    }

    private static <T> List<T> mergeAndSort(Comparator<T> comparator, Collection<T>... cols) {
        List<T> r = new ArrayList<T>();
        for (Collection<T> col : cols) {
            r.addAll(col);
        }
        Collections.sort(r, comparator);
        return r;
    }

    public static class StringComparator implements Comparator<String> {

        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }

    }

}