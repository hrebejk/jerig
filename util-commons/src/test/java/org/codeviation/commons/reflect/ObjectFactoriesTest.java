/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.reflect;

import java.util.HashMap;
import java.util.Map;
import org.codeviation.commons.patterns.Factory;
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
public class ObjectFactoriesTest {

    public ObjectFactoriesTest() {
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

    /**
     * Test of collection2Object method, of class ObjectFactories.
     */
    @Test
    public void testCollection2Object() {
        System.out.println("collection2Object");
        
        Factory<Record,Map<? extends Object,? extends Object>> f = ObjectFactories.collection2Object(Record.class);

        Map<String,Object> r1 = new HashMap<String, Object>();
        Map<String,Object> r2 = new HashMap<String, Object>();

        r1.put("str", "ahoj");
        r1.put("i", 1);
        r1.put("l", (long)2);
        r1.put("f", 3.3f);
        r1.put("d", 4.4);
        r1.put("s", (short)5);
        r1.put("b", (byte)6);
        r1.put("bool", true);

        r1.put("r", r2);
        
        r2.put("str", "nazdar");

        Record r = f.create(r1);

        assertEquals(r.str, "ahoj");
        assertEquals(r.r.str, "nazdar");

    }

    private static class Record {

        private String str;
        
        private int i;
        private long l;
        private float f;
        private double d;
        private short s;
        private byte b;
        private boolean bool;

        private Record r;

    }


}