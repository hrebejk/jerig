/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.strast;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.codeviation.commons.patterns.Factory;
import org.codeviation.commons.utils.ArrayUtil;

/** Factory methods for the storages.
 *
 * @author Petr Hrebejk
 */
public class Strast {

    static final String DATA_FOLDER  = "data"; // XXX Make configurable with storage buidler
    static final String INDEX_FOLDER  = "indexes"; // XXX Make configurable with storage buidler

    public static Store createStore(File root) throws IOException {
        return new FileStore( root );
    }

    public static Storage createObjectStorage(File storageRoot) throws IOException {
        return new ObjectStorage( createStore( new File(storageRoot, DATA_FOLDER) ) );
    }

    public static IndexingStorage createIndexingStorage(File storageRoot) throws IOException {
        return new IndexingStorage( createObjectStorage(storageRoot), new File( storageRoot, INDEX_FOLDER));
    }

    public static Factory<String, Object> createObjectNameFactory() {
        return new ObjectNameFactory<Object>();
    }


    public static String[] path( String textPath ) {
        return textPath.split("/");
    }

    public static String[] path( String[] path, String... addition ) {
        return ArrayUtil.union(path, addition);
    }
    

    /** Tells Strast what index to store given document to. If this annotation
     * is ommited objects of the class will not indexed.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Index {
        String value();
    }

    /** Tells Strast what objects to create and put into indexes, when storing
     *  given object. Classes put as parameters into the annotation will be
     *  scanned for contructor taking annotated class as parameter. If such
     *  contructor exists it will be invoked and resulting object will be added
     *  into approptiate index.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface IndexEntries {
        Class[] value();
    }


    /** Tells Strast how the filename of the record should be formated
     * the format string is the same as the one of printf method. If used on
     * field it means that the field will be stored in different file. See also
     * IdPart annotation. If the Id part not used the first field is taken.
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD })
    public @interface NameFormat {
        String value() default "";
    }


    /** Tells Strast that given field is part of the ID.
     *
     * @author Petr Hrebejk
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD )
    public @interface IdPart {
        int value() default -1;
    }

    public static void main(String... args) {
        System.out.println("Temporary");
    }

}
