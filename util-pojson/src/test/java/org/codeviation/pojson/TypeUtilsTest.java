/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codeviation.pojson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.codeviation.pojson.TypeUtils.*;
import org.codeviation.pojson.records.RecordGeneric;
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
public class TypeUtilsTest {

    public TypeUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    @Test
    public void testExp() {
        assertTrue( Collection.class.isAssignableFrom( List.class ));
        assertTrue( Iterable.class.isAssignableFrom( List.class ));

  
        assertTrue( List.class.isAssignableFrom( ArrayList.class ));
        assertTrue( Object.class.isAssignableFrom( List.class ));

        assertFalse( List.class.isAssignableFrom( Collection.class ));
        assertFalse( ArrayList.class.isAssignableFrom( List.class ));


        //RecordGeneric<String,RecordSmall>[] rga = new RecordGeneric<String,RecordSmall>[10];

//        int[][] x = new int[2][6];
//        System.out.println( "Class " +  x.getClass().getComponentType() );



    }

    @Test
    public void testIsJsonArrayType() {
        assertTrue( isJsonArrayType( Collection.class ));
        assertTrue( isJsonArrayType( Iterable.class ));
        assertTrue( isJsonArrayType( List.class ));
        assertTrue( isJsonArrayType( new Integer[0].getClass() ));
        assertTrue( isJsonArrayType( new int[0].getClass() ));
        assertTrue( isJsonArrayType( new RecordSmall[0].getClass() ));

        assertFalse( isJsonArrayType( Integer.TYPE ));
        assertFalse( isJsonArrayType( Integer.class ));
        assertFalse( isJsonArrayType( String.class ));
        assertFalse( isJsonArrayType( Object.class ));
    }

    @Test
    public void testIsMapObjectType() {
        assertTrue( isJsonMapType( Map.class ));
        
        assertFalse( isJsonMapType( RecordSmall.class ));
        assertFalse( isJsonMapType( Collection.class ));
        assertFalse( isJsonMapType( Iterable.class ));
        assertFalse( isJsonMapType( List.class ));
        assertFalse( isJsonMapType( new Integer[0].getClass() ));
        assertFalse( isJsonMapType( new int[0].getClass() ));
        assertFalse( isJsonMapType( new RecordSmall[0].getClass() ));

        assertFalse( isJsonArrayType( Integer.TYPE ));
        assertFalse( isJsonArrayType( Integer.class ));
        assertFalse( isJsonArrayType( String.class ));
    }

    List<String> lS;
    List<Integer> lI;
    Collection<RecordSmall> lRS;
    Collection<List<List<String>>> cllS;
    Map<String,RecordSmall> mRS;
    Map<String,List<List<String>>> mllS;


    @Test
    public void testJsonComponentType() throws NoSuchFieldException {


        assertEquals( Object.class, getJsonComponentType( Collection.class ));
        assertEquals( Object.class, getJsonComponentType( Iterable.class ));
        assertEquals( Object.class, getJsonComponentType( List.class ));

        assertEquals( Object.class, getJsonComponentType(new Object[0].getClass() ));
        assertEquals( Integer.class, getJsonComponentType(new Integer[0].getClass() ));
        assertEquals( Integer.TYPE, getJsonComponentType( new int[0].getClass() ));
        assertEquals( RecordSmall.class, getJsonComponentType(new RecordSmall[0].getClass() ));

        assertEquals( String.class, getJsonComponentType( getGenericFieldType("lS")));
        assertEquals( Integer.class, getJsonComponentType( getGenericFieldType("lI")));
        assertEquals( RecordSmall.class, getJsonComponentType( getGenericFieldType("lRS")));
        assertEquals( List.class, getRawType( getJsonComponentType( getGenericFieldType("cllS"))));
        assertEquals( List.class, getRawType( getJsonComponentType(
                                                getJsonComponentType( getGenericFieldType("cllS")))));
        assertEquals( String.class, getRawType( getJsonComponentType(
                                                 getJsonComponentType(
                                                    getJsonComponentType( getGenericFieldType("cllS"))))));

        assertEquals( Object.class, getJsonComponentType( Map.class ) );
        assertEquals( RecordSmall.class, getJsonComponentType( getGenericFieldType("mRS")));
        assertEquals( List.class, getRawType( getJsonComponentType( getGenericFieldType("mllS"))));
        assertEquals( List.class, getRawType( getJsonComponentType(
                                                getJsonComponentType( getGenericFieldType("mllS")))));
        assertEquals( String.class, getRawType( getJsonComponentType(
                                                 getJsonComponentType(
                                                    getJsonComponentType( getGenericFieldType("mllS"))))));


    }

    private Set<List<String>> f1;


    @Test
    public void testIsPrimitiveType() throws NoSuchFieldException {


        assertTrue( isJsonPrimitiveType( Integer.TYPE ));
        assertTrue( isJsonPrimitiveType( Boolean.TYPE ));
        assertTrue( isJsonPrimitiveType( Character.TYPE ));

        assertTrue( isJsonPrimitiveType( String.class ));
        assertTrue( isJsonPrimitiveType( Date.class ));

        assertTrue( isJsonPrimitiveType( new byte[0].getClass().getComponentType() ) );

        Field ff1 = this.getClass().getDeclaredField("f1");
        assertFalse( isJsonPrimitiveType( ff1.getType() ) );

    }

    @Test
    public void testGetRawType() throws NoSuchFieldException {

        assertEquals( Map.class, getRawType( Map.class ) );
        assertEquals( HashMap.class, getRawType( HashMap.class ) );
        assertEquals( Set.class, getRawType( Set.class ) );
        assertEquals( HashSet.class, getRawType( HashSet.class ) );

        Field ff1 = this.getClass().getDeclaredField("f1");
        assertEquals( Set.class, getRawType( ff1.getType() ) );
    }


    @Test
    public void testBuildArray() {

        int[] ia = (int[])buildArray( Integer.TYPE, Util.listOf( 1, 2, 3) );

        assertEquals(2l, ia[1]);

    }


    private Type getGenericFieldType( String name ) throws NoSuchFieldException {
        return this.getClass().getDeclaredField(name).getGenericType();
    }

    
}
