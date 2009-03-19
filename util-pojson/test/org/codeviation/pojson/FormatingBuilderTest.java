/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.pojson;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import org.codeviation.commons.reflect.ClassUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Hrebejk
 */
public class FormatingBuilderTest {

    public FormatingBuilderTest() {
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
    public void arrays() throws Exception {
        System.out.println("arrays");

        StringWriter sw = new StringWriter();
        PojsonBuilder pb = FormatingBuilder.create(sw, null, 0);

        pb.array().
            value("nazdar").
            value("nazdar").
            array().
                value(1).
                value(3.5).
                value(false).
                up().
            array().
                up().
            up().
        build();

        assertEquals("[\"nazdar\",\"nazdar\",[1,3.5,false],[]]", sw.toString());

    }


    @Test
    public void testHashes() throws Exception {
        System.out.println("hashes");

        StringWriter sw = new StringWriter();
        FormatingBuilder pb = FormatingBuilder.create(sw, null, 0);
        pb.hash().
            field("a").value("nazdar").
            field("c").value("nazdar").
            field("d").array().
                value(1).
                value(3.5).
                value(false).
                hash().
                    field("x").value("X").
                    field("y").hash().up().
                    up().
                up().
            up().
        build();

        assertEquals("{\"a\":\"nazdar\",\"c\":\"nazdar\",\"d\":[1,3.5,false,{\"x\":\"X\",\"y\":{}}]}", sw.toString());
    }
   
}