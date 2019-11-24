/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.utils;

import java.io.File;
import java.net.URL;
import org.codeviation.commons.patterns.Factory;
import org.codeviation.commons.patterns.Filter;
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
public class FileUtilTest {

    public FileUtilTest() {
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

//    /**
//     * Test of asString method, of class FileUtil.
//     */
//    @Test
//    public void testAsString() throws Exception {
//        System.out.println("asString");
//        File f = null;
//        String expResult = "";
//        String result = FileUtil.asString(f);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of fromString method, of class FileUtil.
//     */
//    @Test
//    public void testFromString() throws Exception {
//        System.out.println("fromString");
//        File f = null;
//        String[] strings = null;
//        FileUtil.fromString(f, strings);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of fromLines method, of class FileUtil.
//     */
//    @Test
//    public void testFromLines() throws Exception {
//        System.out.println("fromLines");
//        File f = null;
//        String[] strings = null;
//        FileUtil.fromLines(f, strings);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of fileFactory method, of class FileUtil.
//     */
//    @Test
//    public void testFileFactory_File() {
//        System.out.println("fileFactory");
//        File root = null;
//        Factory<File, String> expResult = null;
//        Factory<File, String> result = FileUtil.fileFactory(root);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of fileFactory method, of class FileUtil.
//     */
//    @Test
//    public void testFileFactory_File_Filter() {
//        System.out.println("fileFactory");
//        File root = null;
//        Filter<File> filter = null;
//        Factory<File, String> expResult = null;
//        Factory<File, String> result = FileUtil.fileFactory(root, filter);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of path method, of class FileUtil.
//     */
//    @Test
//    public void testPath_StringArr() {
//        System.out.println("path");
//        String[] pathElements = null;
//        String expResult = "";
//        String result = FileUtil.path(pathElements);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of path method, of class FileUtil.
//     */
//    @Test
//    public void testPath_char_StringArr() {
//        System.out.println("path");
//        char separator = ' ';
//        String[] pathElements = null;
//        String expResult = "";
//        String result = FileUtil.path(separator, pathElements);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of resolve method, of class FileUtil.
//     */
//    @Test
//    public void testResolve_String_String() {
//        System.out.println("resolve");
//        String parent = "";
//        String path = "";
//        File expResult = null;
//        File result = FileUtil.resolve(parent, path);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of resolve method, of class FileUtil.
//     */
//    @Test
//    public void testResolve_File_String() {
//        System.out.println("resolve");
//        File parent = null;
//        String path = "";
//        File expResult = null;
//        File result = FileUtil.resolve(parent, path);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of resolve method, of class FileUtil.
//     */
//    @Test
//    public void testResolve_File_File() {
//        System.out.println("resolve");
//        File parent = null;
//        File path = null;
//        File expResult = null;
//        File result = FileUtil.resolve(parent, path);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of nameNoExt method, of class FileUtil.
     */
    @Test
    public void testNameNoExt() {
        System.out.println("nameNoExt");
        String name = "a.b.c";
        assertEquals("a", FileUtil.nameNoExt(name));

        name = "a.b";
        assertEquals("a", FileUtil.nameNoExt(name));

        name = null;
        assertNull(FileUtil.nameNoExt(name));

        name = "a.";
        assertEquals("a", FileUtil.nameNoExt(name));

        name = ".x";
        assertEquals("", FileUtil.nameNoExt(name));

    }



    /**
     * Test of extension method, of class FileUtil.
     */
    @Test
    public void testExtensions() {
        String name = "a.b.c";
        assertEquals("b.c", FileUtil.extensions(name));

        name = "a.b";
        assertEquals("b", FileUtil.extensions(name));

        name = null;
        assertNull(FileUtil.extensions(name));

        name = "";
        assertEquals("", FileUtil.extensions(name));

        name = "a.";
        assertEquals("", FileUtil.extensions(name));

        name = ".x";
        assertEquals("x", FileUtil.extensions(name));
    }

    /**
     * Test of extension method, of class FileUtil.
     */
    @Test
    public void testExtension() {
        String name = "a.b.c";
        assertEquals("c", FileUtil.extension(name));

        name = "a.b";
        assertEquals("b", FileUtil.extension(name));

        name = null;
        assertNull(FileUtil.extension(name));

        name = "";
        assertEquals("", FileUtil.extension(name));

        name = "a.";
        assertEquals("", FileUtil.extension(name));

        name = ".x";
        assertEquals("x", FileUtil.extension(name));
    }

    /**
     * Test of toURL method, of class FileUtil.
     */
//    @Test
//    public void testToURL() throws Exception {
//        System.out.println("toURL");
//        File file = null;
//        URL expResult = null;
//        URL result = FileUtil.toURL(file);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}