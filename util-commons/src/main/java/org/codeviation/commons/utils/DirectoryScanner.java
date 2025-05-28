/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.utils;

import java.io.File;
import java.util.function.Predicate;

/** XXX Needs a nicer API.
 *
 * @author phrebejk
 */
public abstract class DirectoryScanner {

    private File directory;

    private String[] relPath;

    private Predicate<File> filter;

    public DirectoryScanner(File directory) {
        if ( !directory.isDirectory() ) {
            throw new IllegalArgumentException( directory.getAbsolutePath() + " must be a directory.");
        }
        this.directory = directory;
        this.relPath = new String[0];
    }

    public DirectoryScanner( File directory, Predicate<File> filter) {
        this(directory);
        this.filter = filter;
    }

    public void scan() {
        scan(directory);
    }

    protected void file(File file) {}; // Called for every file

    protected void directory(File file) {}; // Called for every directory

    protected void directoryEnd(File directory) {};    // Called when dorectory scanning finishes

    protected String[] getRelativePath() {
        return relPath;
    }

    private void scan( File f ) {

        if ( filter != null && !filter.test(f) ) {
            return;
        }

        if ( f.isFile() ) {
            file(f);
        }

        if ( f.isDirectory() ) {
            directory(f);

            File[] files = f.listFiles();
            if ( files != null ) {
                for (File file : files) {
                    String[] oldRelPath = relPath;
                    relPath = ArrayUtil.union(relPath, file.getName());
                    scan(file);
                    relPath = oldRelPath;
                }
            }
            directoryEnd(f);
        }

    }
}
