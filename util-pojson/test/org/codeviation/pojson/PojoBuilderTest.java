/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.pojson;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.codeviation.pojson.records.RecordArrays;
import org.codeviation.pojson.records.RecordComplex;
import org.codeviation.pojson.records.RecordObjectTypes;
import org.codeviation.pojson.records.RecordPrimitiveTypes;
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
public class PojoBuilderTest {

    public PojoBuilderTest() {
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
    public void testBasicHash() throws Exception {
        System.out.println("basicHash");

        PojsonBuilder<P1,RuntimeException> pbr = PojoBuilder.create( new P1() );

        P1 p1 = pbr.hash().
            field("s1").value("nazdar").
            field("s2").value("cau").
            field("p2").hash().
                field("s3").value("zdur").
                field("s4").value("vitame vas").
                field("i5").array().
                    value(1).
                    value(2).
                    value(3).
                    up().
                field("p1").hash().
                    field("s1").value("bud zdrav").
                    field("s2").value("dobry den").
                    up().
                up().
            up().
            build();


        // System.out.println("o " + o);

        assertEquals("nazdar", p1.s1);
        assertEquals("cau", p1.s2);
        assertEquals("zdur", p1.p2.s3);
        assertEquals("vitame vas", p1.p2.s4);
        assertEquals(1, p1.p2.i5[0]);
        assertEquals(2, p1.p2.i5[1]);
        assertEquals(3, p1.p2.i5[2]);
        assertEquals("bud zdrav", p1.p2.p1.s1);
        assertEquals("dobry den", p1.p2.p1.s2);

    }


    @Test
    public void testBasicStringArray() throws Exception {
        System.out.println("basicArray");

        PojsonBuilder<String[],RuntimeException> pbr = PojoBuilder.create( new String[0] );

        String[] p1 = pbr.array().
            value("nazdar").
            value("cau").
            value("zdur").
            value("vitame vas").
            up().
            build();


        // System.out.println("o " + o);

        assertEquals("nazdar", p1[0]);
        assertEquals("cau", p1[1]);
        assertEquals("zdur", p1[2]);
        assertEquals("vitame vas", p1[3]);

    }

    
   

    @Test
    public void testResolveComponentType() throws NoSuchFieldException {
        System.out.println("resolveComponentType");

        GenericTypes gt = new GenericTypes();

        assertEquals( Integer.TYPE, PojoBuilder.resolveComponentType( gt.getField("ints")));
        assertEquals( Integer.class, PojoBuilder.resolveComponentType( gt.getField("Ints")));
        assertEquals( String.class, PojoBuilder.resolveComponentType( gt.getField("strings")));
        assertEquals( Reference.class, PojoBuilder.resolveComponentType( gt.getField("references")));
        assertEquals( Integer.class, PojoBuilder.resolveComponentType( gt.getField("lints")));
        assertEquals( Long.class, PojoBuilder.resolveComponentType( gt.getField("msl")));

    }


        /**
     * Test of build method, of class FormatingJsonBuilder.
     */
//    @Test
//    public void testSimple() throws Exception {
//        System.out.println("simple");
//
//        StringWriter sw = new StringWriter();
//        JsonBuilder.Root<Object,RuntimeException> cbr = CollectionsBuilder.create();
//
//        Object o = cbr.array().
//            e().v("nazdar").
//            e().v("nazdar").
//            e().v( cbr.array().e().v(1).
//                              e().v(3.5).
//                              e().v(false) )
//
//            .build();
//
//
//        System.out.println(sw.toString());
//        System.out.println("");
//        System.out.println("");
//
//
//        o = cbr.hash().
//            f("a").v("nazdar").
//            f("c").v("nazdar").
//            f("d").v( cbr.array().
//                              e().v(1).
//                              e().v(3.5).
//                              e().v(false) )
//            .f("x").v( cbr.hash().
//                              f("A").v("a").
//                              f("B").v(true).
//                              f("C").v( cbr.hash().
//                                              f("m").v(4) ) )
//            .build();
//
//
//
//
//        System.out.println(sw.toString());
//
//
////        Void expResult = null;
////        Void result = instance.build();
////        assertEquals(expResult, result);
//
//
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }


//        @Test
//        public void testPojo() throws Exception {
//            System.out.println("pojo");
//
//            PojsonBuilder<P1,RuntimeException> pbr = PojoBuilder.create( new P1() );
//
//            Object o = pbr.hash().
//                f("s1").v("nazdar").
//                f("s2").v("cau").
//                f("p2").v( pbr.hash().
//                    f("s3").v("zdur").
//                    f("s4").v("vitame vas").
//                    f("p1").v( pbr.hash().
//                        f("s1").v("bud zdrav").
//                        f("s2").v("dobry den")))
//                .build();
//
//
//            System.out.println("o " + o);
//        }


    public static class P1 {
        public String s1;
        public String s2;
        public P2 p2;
    }

    public static class P2 {
        public String s3;
        public String s4;
        public int[] i5;
        public P1 p1;
    }


    public static class GenericTypes {

        public int[] ints;
        public Integer[] Ints;

        public String[] strings;
        public Reference[] references;

        public List<String> lString;
        public List<Integer> lints;

        public Map<String, Long> msl;

        

        Field getField(String name) throws NoSuchFieldException {
            return this.getClass().getField(name);
        }

    }

}