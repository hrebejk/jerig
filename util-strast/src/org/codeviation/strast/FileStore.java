/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.strast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.codeviation.commons.utils.FileUtil;
import org.codeviation.strast.model.FileDescription;

/**
 *
 * @author phrebejk
 */
class FileStore implements Store {

    private File root;

    public FileStore(File root) throws IOException {
        this.root = root;

        FileUtil.assureDirectory(root);

        if ( !root.canWrite() ) {
            throw new IOException( "Can't write to directory " + root + "!");
        }
    }

    public OutputStream getOutputStream(String... path) {
        File f = FileUtil.file(root, path);
        try {
            File p = f.getParentFile();
            FileUtil.assureDirectory(p);
            f.createNewFile();
            return f.isFile() && f.canWrite() ? new FileOutputStream(f) : null;
        }
        catch( FileNotFoundException ex) {
            throw new IllegalStateException( ex );
        }
        catch( IOException ex ) {
            throw new IllegalStateException( ex );
        }
    }

    public InputStream getInputStream(String... path) {
        File f = FileUtil.file(root, path);
        try {
            return f.isFile() && f.canRead() ? new FileInputStream(f) : null;
        }
        catch( FileNotFoundException ex) {
            throw new IllegalStateException( ex );
        }
    }

    public boolean delete(String... path) {
        File f = FileUtil.file(root, path);
        return f.exists() && f.delete();
    }

    public String[] list(String... path) {
        File f = FileUtil.file(root, path);

        if ( f.isDirectory() && f.canRead() ) {
            return f.list();
        }

        return null;
    }

//    private FileDescription createFileDescription(File f) {
//
//        f.isDirectory();
//        f.isFile();
//        f.isHidden();
//        f.length();
//        f.lastModified();
//        f.list();
//    }

}
