/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.utils;


import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.util.zip.ZipOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author phrebejk
 */
public class ZipUtil {

    private static final int BUFFER_SIZE = 2048;

    /**
     * Zips file or folder to given output stream
     *
     * XXX Add file filter and recursive flag
     *
     */



    static public void zip(File file, OutputStream os) throws IOException {
        zip( file, os, true );
    }
    
    static public void zip(File file, OutputStream os, boolean addRoot) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);

        if (!addRoot && file.isDirectory()) {
            for( File f : file.listFiles() ) {
                add( "", f, zos);
            }
        }
        else {
            add("", file, zos);
        }

        zos.flush();
        zos.close();

    }

    /** Unzips given InputStream into a folder
     *
     * @param is
     * @param target
     * @throws java.io.IOException
     */
    public static void unzip( InputStream is, File target ) throws IOException {

        ZipInputStream zis = new ZipInputStream(is);

        byte buffer[] = new byte[BUFFER_SIZE];

        ZipEntry ze = zis.getNextEntry();
        while( ze != null ) {
            System.out.println("" + ze.getName() + ":" + " " + ze.getSize() );

            if ( ze.isDirectory() ) {
                new File(target, ze.getName()).mkdirs();
            }
            else {
                File f = new File(target, ze.getName());
                if ( !f.getParentFile().isDirectory() ) {
                    f.getParentFile().mkdirs();
                }
                f.createNewFile();
                OutputStream os = new FileOutputStream(f);
                int read = -1;
                do {
                    read = zis.read(buffer);
                    if ( read != -1 ) {
                        os.write(buffer, 0, read);
                        os.flush();
                    }
                }
                while( read != -1 );
                os.close();
            }

            zis.closeEntry();
            ze = zis.getNextEntry();
        }

    }

    private static void add(String path, File file, ZipOutputStream zip) throws IOException {

        path = path + "/" + file.getName();

        if ( file.isFile() ) {
            zip.putNextEntry( new ZipEntry(path));
            StreamUtil.copy(new FileInputStream(file), zip);
        }
        else if ( file.isDirectory() ) {
            File files[] = file.listFiles();
            if ( files == null || files.length == 0 ) {
                zip.putNextEntry(new ZipEntry(path + "/"));
            }
            else {
                for( File f : files ) {
                    add( path, f, zip);
                }
            }
        }        
    }

}
