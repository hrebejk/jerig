/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
public class SequenceIteratorTest {

    public SequenceIteratorTest() {
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
    public void testSingleIterator() {
        System.out.println("singleIterator");
        
        List<String> l1 = l1 = Arrays.asList( new String[] { "a", "b", "d" } );
                        
        Iterator<String> mi = Iterators.sequence(
                l1.iterator() );
        
        List<String> actual = toList(mi);
        List<String> expected = Arrays.asList( new String[] { "a", "b", "d" } );
        
        assertEquals("Should the same", expected, actual );

        assertEmpty(mi);

    }
    
    @Test
    public void testSingleElement() {
        System.out.println("singleElement");
        
        List<String> l1 = l1 = Arrays.asList( new String[] { "a" } );
                        
        Iterator<String> mi = Iterators.sequence(
                l1.iterator() );
        
        List<String> actual = toList(mi);
        List<String> expected = Arrays.asList( new String[] { "a" } );

        assertEquals("Should the same", expected, actual );

        assertEmpty(mi);

    }
    
    @Test
    public void testAllEmpty() {
        System.out.println("allEmpty");

        List<String> l1 = new ArrayList<String>();
        List<String> l2 = new ArrayList<String>();
        List<String> l3 = new ArrayList<String>();

        Iterator<String> mi = Iterators.sequence(
                l1.iterator(),
                l2.iterator(),
                l3.iterator() );

        assertEmpty(mi);

    }


    @Test
    public void testOneEmpty() {
        System.out.println("oneEmpty");

        List<String> l1 = Arrays.asList( new String[] { "a", "b", "d" } );
        List<String> l2 = Arrays.asList( new String[] {} );
        List<String> l3 = Arrays.asList( new String[] { "x", "y", "z" } );

        Iterator<String> mi = Iterators.sequence(
                l1.iterator(),
                l2.iterator(),
                l3.iterator() );

        List<String> actual = toList(mi);
        List<String> expected = Arrays.asList( new String[] { "a", "b", "d", "x", "y", "z" } );

        assertEquals("Should the same", expected, actual );

        assertEmpty(mi);

    }


    @Test
    public void testNoneEmpty() {
        System.out.println("noneEmpty");

        List<String> l1 = Arrays.asList( new String[] { "a", "b", "d" } );
        List<String> l2 = Arrays.asList( new String[] { "b", "c", "d" } );
        List<String> l3 = Arrays.asList( new String[] { "x", "y", "z" } );

        Iterator<String> mi = Iterators.sequence(
                l1.iterator(),
                l2.iterator(),
                l3.iterator() );

        List<String> actual = toList( mi );
        List<String> expected = Arrays.asList( new String[] { "a", "b", "d", "b", "c", "d", "x", "y", "z" } );

        assertEquals("Should the same", expected, actual );

        assertEmpty(mi);

    }

    @Test
    public void testNoIterators() {
        System.out.println("noiterators");

        Iterator<String> mi = Iterators.sequence();

        assertEmpty(mi);

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

}