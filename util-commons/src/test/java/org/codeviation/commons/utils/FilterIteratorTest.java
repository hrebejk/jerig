/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.utils;

import java.util.Iterator;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
// import org.codeviation.commons.patterns.Filters; // Filters.IsIn will be replaced by a lambda
import java.util.ArrayList;
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
public class FilterIteratorTest {

    public FilterIteratorTest() {
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
    public void testEmpty() {
        System.out.println("hasNext");

        Iterator<String> fi = Iterators.filter( new ArrayList<String>().iterator(), s -> Arrays.asList("a", "b").contains(s) );

        MergingIteratorTest.assertEmpty(fi);
    }

    @Test
    public void testLastNo() {
        System.out.println("lastNo");

        Iterator<String> fi = Iterators.filter(
                Arrays.asList(new String[] {"a", "f", "a", "b", "m" } ).iterator(),
                s -> Arrays.asList("a", "b").contains(s) );

        List<String> actual = MergingIteratorTest.toList(fi);

        assertEquals("values: ", Arrays.asList(new String[] {"a", "a", "b"} ), actual);

        MergingIteratorTest.assertEmpty(fi);
    }


    @Test
    public void testLasYes() {
        System.out.println("lastYes");

        Iterator<String> fi = Iterators.filter(
                Arrays.asList(new String[] {"a", "f", "a", "b", "m" } ).iterator(),
                s -> Arrays.asList("f", "b", "m").contains(s) );

        List<String> actual = MergingIteratorTest.toList(fi);

        assertEquals("values: ", Arrays.asList(new String[] {"f", "b", "m"} ), actual);

        MergingIteratorTest.assertEmpty(fi);
    }

}