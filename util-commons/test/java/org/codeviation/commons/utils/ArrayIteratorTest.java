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
public class ArrayIteratorTest {

    public ArrayIteratorTest() {
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
    public void testSingleElement() {
        System.out.println("singleElement");
        
        String[] array = new String[] { "a" };
                        
        Iterator<String> mi = Iterators.array( array );
                        
        List<String> actual = toList(mi);
        List<String> expected = Arrays.asList( array );

        assertEquals("Should the same", expected, actual );

        assertEmpty(mi);

    }
    
    

    @Test
    public void testEmpty() {
        System.out.println("empty");

        String[] array = new String[] {};
        Iterator<String> mi = Iterators.array( array );
        
        assertEmpty(mi);

    }


    @Test
    public void testMore() {
        System.out.println("more");

        String[] array = new String[] { "a", "b", "d" };
        Iterator<String> mi = Iterators.array( array );

        List<String> actual = toList( mi );
        List<String> expected = Arrays.asList( array );

        assertEquals("Should the same", expected, actual );

        assertEmpty(mi);

    }

    @Test
    public void testLength0() {
        System.out.println("length0");

        String[] array = new String[] { "a", "b", "d" };
        Iterator<String> mi = Iterators.array( array, 0 );


        assertEmpty(mi);

    }

    @Test
    public void testLength1() {
        System.out.println("length1");

        String[] array = new String[] { "a", "b", "d" };
        Iterator<String> mi = Iterators.array( array, 1 );

        List<String> actual = toList( mi );
        List<String> expected = Arrays.asList( array ).subList(0, 1);

        assertEquals("Should the same", expected, actual );

        assertEmpty(mi);

    }

    @Test
    public void testLength2() {
        System.out.println("length2");

        String[] array = new String[] { "a", "b", "d" };
        Iterator<String> mi = Iterators.array( array, 2 );

        List<String> actual = toList( mi );
        List<String> expected = Arrays.asList( array ).subList(0, 2);

        assertEquals("Should the same", expected, actual );

        assertEmpty(mi);

    }

    @Test
    public void testLength3() {
        System.out.println("length3");

        String[] array = new String[] { "a", "b", "d" };
        Iterator<String> mi = Iterators.array( array, 3 );

        List<String> actual = toList( mi );
        List<String> expected = Arrays.asList( array );

        assertEquals("Should the same", expected, actual );

        assertEmpty(mi);

    }

    @Test
    public void testLength4() {
        System.out.println("length4");

        String[] array = new String[] { "a", "b", "d" };
        Iterator<String> mi = Iterators.array( array, 4 );

        List<String> actual = toList( mi );
        List<String> expected = Arrays.asList( array );

        assertEquals("Should the same", expected, actual );

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