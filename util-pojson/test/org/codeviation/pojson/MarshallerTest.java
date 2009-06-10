/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.pojson;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import org.codeviation.commons.patterns.Filter;
import org.codeviation.commons.reflect.ClassUtils;
import org.codeviation.pojson.records.RecordSmall;
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
public class MarshallerTest {


    private static String GOLDEN;
    private static String GOLDEN_L1;
    private static String GOLDEN_S1;

    public MarshallerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        GOLDEN = ClassUtils.getResourceAsString(MarshallerTest.class, "goldenfiles/RecordSmall.txt");
        GOLDEN_L1 = ClassUtils.getResourceAsString(MarshallerTest.class, "goldenfiles/RecordSmallIndentLevel1.txt");
        GOLDEN_S1 = ClassUtils.getResourceAsString(MarshallerTest.class, "goldenfiles/RecordSmallIndentSpace.txt");
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

//    /**
//     * Test of save method, of class Marshaller.
//     */
//    @Test
//    public void testSave_null() {
//        System.out.println("save");
//        T object = null;
//        Marshaller instance = new Marshaller();
//        String expResult = "";
//        String result = instance.save(object);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of save method, of class Marshaller.
//     */
//    @Test
//    public void testSave_null_Writer() throws Exception {
//        System.out.println("save");
//        T object = null;
//        Writer writer = null;
//        Marshaller instance = new Marshaller();
//        instance.save(object, writer);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of save method, of class Marshaller.
//     */
//    @Test
//    public void testSave_null_OutputStream() throws Exception {
//        System.out.println("save");
//        T object = null;
//        OutputStream outputStream = null;
//        Marshaller instance = new Marshaller();
//        instance.save(object, outputStream);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of save method, of class Marshaller.
//     */
//    @Test
//    public void testSave_null_File() throws Exception {
//        System.out.println("save");
//        T object = null;
//        File file = null;
//        Marshaller instance = new Marshaller();
//        instance.save(object, file);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of setFieldFilter method, of class Marshaller.
     */
//    @Test
//    public void testSetFieldFilter() {
//        System.out.println("setFieldFilter");
//        Filter<String> fieldFilter = null;
//        Marshaller instance = new Marshaller();
//        instance.setFieldFilter(fieldFilter);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of setIndentation method, of class Marshaller.
     */
    @Test
    public void testSetIndentation() {
        System.out.println("setIndentation");

        Marshaller<RecordSmall> m = new Marshaller<RecordSmall>();
        assertEquals(GOLDEN, m.save( new RecordSmall(10, "ahoj")));

        m.setIndentation(" ");
        assertEquals(GOLDEN_S1, m.save( new RecordSmall(10, "ahoj")));

    }

    /**
     * Test of setIndentLevel method, of class Marshaller.
     */
    @Test
    public void testSetIndentLevel() {
        System.out.println("setIndentLevel");

        Marshaller<RecordSmall> m = new Marshaller<RecordSmall>();
        assertEquals(GOLDEN, m.save( new RecordSmall(10, "ahoj")));

        m.setIndentLevel(1);
        // System.out.println(m.save( new RecordSmall(10, "ahoj")));

        assertEquals(GOLDEN_L1, m.save( new RecordSmall(10, "ahoj")));
    }

}