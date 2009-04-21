/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.lutz;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumberTools;
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
public class DocumentFactoryTest {

    public DocumentFactoryTest() {
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
     * Test of create method, of class DocumentFactory.
     */
    @Test
    public void testSimpleRecord() {
        System.out.println("simpleRecord");

        Records.Primitve r = new Records.Primitve();
        r.bln = true;
        r.intgr = 100;
        r.lng = 200;
        r.shrt = 300;
        r.bt = 56;
        r.chr = 'h';

        DocumentFactory<Records.Primitve> df = new DocumentFactory<Records.Primitve>(Records.Primitve.class);
        Document doc = df.create(r);

        assertEquals("true", doc.getField("bln").stringValue() );
        assertEquals(NumberTools.longToString(r.intgr), doc.getField("intgr").stringValue() );
        assertEquals(NumberTools.longToString(r.lng), doc.getField("lng").stringValue() );
        assertEquals(NumberTools.longToString(r.shrt), doc.getField("shrt").stringValue() );
        assertEquals(NumberTools.longToString(r.bt), doc.getField("bt").stringValue() );
        assertEquals("h", doc.getField("chr").stringValue() );
        
    }

    /**
     * Test of create method, of class DocumentFactory.
     */
    @Test
    public void testObjectRecord() {
        System.out.println("objectRecord");

        Date now = new Date();

        Records.Object r = new Records.Object();
        r.bln = true;
        r.intgr = 100;
        r.lng = 200l;
        r.shrt = 300;
        r.bt = 56;
        r.chr = 'h';
        r.dt = now;
        r.strng = "ahoj";

        DocumentFactory<Records.Object> df = new DocumentFactory<Records.Object>(Records.Object.class);
        Document doc = df.create(r);

        assertEquals("true", doc.getField("bln").stringValue() );
        assertEquals(NumberTools.longToString(r.intgr), doc.getField("intgr").stringValue() );
        assertEquals(NumberTools.longToString(r.lng), doc.getField("lng").stringValue() );
        assertEquals(NumberTools.longToString(r.shrt), doc.getField("shrt").stringValue() );
        assertEquals(NumberTools.longToString(r.bt), doc.getField("bt").stringValue() );
        assertEquals("h", doc.getField("chr").stringValue() );
        assertEquals(DateTools.dateToString(now, DateTools.Resolution.MILLISECOND), doc.getField("dt").stringValue() );
        assertEquals("ahoj", doc.getField("strng").stringValue() );

    }

    @Test
    public void testSuppresIndexing() {
        System.out.println("supresIndexing");

        Records.Suppres r = new Records.Suppres();
        r.intgr = 100;
        r.strng = "ahoj";

        DocumentFactory<Records.Suppres> df = new DocumentFactory<Records.Suppres>(Records.Suppres.class);
        Document doc = df.create(r);

        @SuppressWarnings("unchecked")
        List<Field> fields = (List<Field>)doc.getFields();

        assertEquals( 1, fields.size() );
        assertEquals( "strng", fields.get(0).name());

    }

    @Test
    public void testArrayRecord() {
        System.out.println("arrayRecord");


        Records.Array r = new Records.Array();
        r.intgr = new int[] { 100, 200, 300 };
        r.strng = new String[] { "ahoj", "nazdar", "cau" };

        DocumentFactory<Records.Array> df = new DocumentFactory<Records.Array>(Records.Array.class);
        Document doc = df.create(r);

        Set<String> exp;
        Set<String> act;

        exp = CollectionsUtil.hashSet( new String []  {
            NumberTools.longToString(100),
            NumberTools.longToString(200),
            NumberTools.longToString(300) } );
        act = CollectionsUtil.hashSet( doc.getValues("intgr") );

        assertEquals( exp, act );

        exp = CollectionsUtil.hashSet( r.strng);
        act = CollectionsUtil.hashSet( doc.getValues("strng") );
        
        assertEquals( exp, act );

    }

    @Test
    public void testArrayRecordAsTokens() {
        System.out.println("arrayRecordTokens");


        Records.ArrayAsTokens r = new Records.ArrayAsTokens();
        r.intgr = new int[] { 100, 200, 300 };
        r.strng = new String[] { "ahoj", "nazdar", "cau" };

        DocumentFactory<Records.ArrayAsTokens> df = new DocumentFactory<Records.ArrayAsTokens>(Records.ArrayAsTokens.class);
        Document doc = df.create(r);

        assertEquals(  
            NumberTools.longToString(100) + " " +
            NumberTools.longToString(200) + " " +
            NumberTools.longToString(300),
            doc.getField("intgr").stringValue()
        );

        assertEquals(  "ahoj:nazdar:cau",
            doc.getField("strng").stringValue() );

    }


    @Test
    public void testListRecord() {
        System.out.println("listRecord");


        Records.Lst r = new Records.Lst();
        r.intgr = CollectionsUtil.arrayList( 100, 200, 300);
        r.strng = CollectionsUtil.arrayList(  "ahoj", "nazdar", "cau" );

        DocumentFactory<Records.Lst> df = new DocumentFactory<Records.Lst>(Records.Lst.class);
        Document doc = df.create(r);

        Set<String> exp;
        Set<String> act;

        exp = CollectionsUtil.hashSet( new String []  {
            NumberTools.longToString(100),
            NumberTools.longToString(200),
            NumberTools.longToString(300) } );
        act = CollectionsUtil.hashSet( doc.getValues("intgr") );

        assertEquals( exp, act );

        exp = new HashSet<String>(r.strng);
        act = CollectionsUtil.hashSet( doc.getValues("strng") );

        assertEquals( exp, act );

    }

    @Test
    public void testListRecordAsTokens() {
        System.out.println("arrayRecordTokens");


        Records.LstAsTokens r = new Records.LstAsTokens();
        r.intgr = CollectionsUtil.arrayList( 100, 200, 300);
        r.strng = CollectionsUtil.arrayList(  "ahoj", "nazdar", "cau" );

        DocumentFactory<Records.LstAsTokens> df = new DocumentFactory<Records.LstAsTokens>(Records.LstAsTokens.class);
        Document doc = df.create(r);

        assertEquals(
            NumberTools.longToString(100) + " " +
            NumberTools.longToString(200) + " " +
            NumberTools.longToString(300),
            doc.getField("intgr").stringValue()
        );

        assertEquals(  "ahoj:nazdar:cau",
            doc.getField("strng").stringValue() );

    }


    /**
     * Test of create method, of class DocumentFactory.
     */
    @Test
    public void testWithSubsRecord() {
        System.out.println("withSubs");

        Records.WithSubs ws = new Records.WithSubs();

        ws.strng = "nazdar";
        ws.ws1 = new Records.WithSubs();
        ws.ws1.strng = "cau";

        DocumentFactory<Records.WithSubs> df = new DocumentFactory<Records.WithSubs>(Records.WithSubs.class);
        Document doc = df.create(ws);

        assertEquals("nazdar", doc.getField("strng").stringValue() );
        
    }

}