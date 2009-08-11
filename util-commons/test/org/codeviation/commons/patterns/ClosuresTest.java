/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.codeviation.commons.utils.CollectionsUtil;
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
public class ClosuresTest {

    public ClosuresTest() {
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
    public void collectionAvg() {
        System.out.println("collectionAvg");

        List<Integer> l = CollectionsUtil.add(new ArrayList<Integer>(6), 1, 2, 3, 4, 5 ,6);
        Closure<Integer,RuntimeException> closure = Closures.collectionClosure(l);
        
        Double avgNumber = closure.apply(new NumberAverageProcessor());
        Double avgInteger = closure.apply(new IntegerAverageProcessor());
      
        assertEquals(3.5, avgNumber, 0.0000001);
        assertEquals(3.5, avgInteger, 0.0000001);
    }

    // Just to make sure
    private void usage() throws IOException { // should not compile without throwing IOException
        throwingClosure().apply(new NumberAverageProcessor());
    }

    private static Closure<Number,IOException> throwingClosure() {
        return null;
    }


    private static class NumberAverageProcessor implements Closure.Processor<Double,Number> {

        private long count = 0l;
        private double sum = 0l;
        
        public boolean processItem(Number param) {
            count++;
            sum += param.doubleValue();
            return true;
        }

        public Double getResult() {
            return (double)sum / (double)count;
        }

    }

    private static class IntegerAverageProcessor implements Closure.Processor<Double,Integer> {

        private long count = 0l;
        private long sum = 0l;

        public boolean processItem(Integer param) {
            count++;
            sum += param;
            return true;
        }

        public Double getResult() {
            return (double)sum / (double)count;
        }

    }
}