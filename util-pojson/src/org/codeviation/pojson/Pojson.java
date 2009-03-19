/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */

package org.codeviation.pojson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;

/** Contains annotations for driving the save and load process of objects and
 * convenience utility methods for saving/loading Java objects to/from JSON
 * format. In case the of storing or loading larger number of objects (which
 * may profit from caching) or if you need modify proprties of the
 * marhalling/unmarshalling process and/or stricter type checking please use
 * the marshaller UnMarshaller classes.
 *
 *
 *  XXX Remove PojsonLoad and Pojson Save
 *  XXX Make the Load methods honor Pojson annotations
 *  XXX Make the Load methods work with StoreInfo
 *  XXX (Optionaly) clean up the handlers stuff
 *  XXX Add caches for StoreInfos where apporpriate
 *  XXX Add methods and functionality for saving to/loading from collections
 *      using an annotation
 * 
 * @author Petr Hrebejk
 */
public class Pojson {

    static final String DEFAULT_EXTENSION = ".json";
    
    private Pojson() {}

    /** Saves an object to string */
    public static String save( Object object ) {
        return new Marshaller<Object>().save(object);
    }

    public static void save( Object object, Writer writer ) throws IOException {
        new Marshaller<Object>().save(object, writer);
    }

    public static void save( Object object, OutputStream outputStream ) throws IOException {
        new Marshaller<Object>().save(object, outputStream);
    }

    public static void save( Object object, File file ) throws IOException {
        new Marshaller<Object>().save(object, file);
    }

    public static <T> T load( Class<T> clazz, String string ) {
        return new UnMarshaller().load(clazz, string);
    }

    public static <T> T load( Class<T> clazz, Reader reader ) throws IOException {
        return new UnMarshaller().load(clazz, reader);
    }

    public static <T> T load( Class<T> clazz, InputStream inputStream ) throws IOException {
        return new UnMarshaller().load(clazz, inputStream);
    }

    public static <T> T load( Class<T> clazz, File file ) throws FileNotFoundException, IOException {
        return new UnMarshaller().load(clazz, file);
    }

    public static <T> T update( T object, String string ) {
        return new UnMarshaller().update(object, string);
    }

    public static <T> T update( T object, Reader reader ) throws IOException {
        return new UnMarshaller().update(object, reader);
    }

    public static <T> T update( T object, InputStream inputStream ) throws IOException {
        return new UnMarshaller().update(object, inputStream);
    }

    public static <T> T update( T object, File file ) throws FileNotFoundException, IOException {
        return new UnMarshaller().update(object, file);
    }

    /** General annotation to mark classes as Pojson records. This annotation
     * may be used for other frameworks to distinguish between serializable and
     * not serializable objects. The annotation is currently not used in pojson.
     *
     * XXX Put a checker for this anotation to PojsonSave.
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Record {
        // Intentionally empty
    }

    /** Tells Pojson that given field should not be stored
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface SuppressStoring {
        // Intentionally empty
    }
    
    /** Tells Pojson that given should be stored under diferent name than the
     * name of given field.
     *
     * @author Petr Hrebejk
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Name {
        String value();
    }
    
    /** Tells Pojson not to store fields whose value is null. If used on class
     * it is valid for all fields in the class.
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD })
    public @interface SkipNullValues {
    }


    /** Tells Pojson not to complain about nonexisting fields in the class
     * when loading the data from the stream. I.e. having more fields in the
     * stream than in the class is fine.
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE })
    public @interface IgnoreNonExisting {
    }
    
    /** Tells Pojson how the filename of the record should be formated
     * the format string is the same as the one of printf method. If used on
     * field it means that the field will be stored in different file. See also 
     * IdPart annotation. If the Id part not used the first field is taken.
     * 
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD })
    public @interface FileNameFormat {
        String value() default "";
    }
        
    
    /** Tells Pojson that given field is part of the ID.
     * 
     * @author Petr Hrebejk
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD )
    public @interface IdPart {
        int value() default -1;        
    }
    
    
    /** Tells Pojson where to stop inspecting the class hierarchy.
     * By default only inspects current class.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.FIELD} )
    public @interface StopAt {
        Class value() default StopAtCurrentClass.class;
    }
    
    /** Tells Pojson that given field should be stored by calling to String
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ToString {
        // XXX Add factory for from string back to object
        //Class<Factory<Object,String>> value();
    }
    
    /** What fields should be included
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ModifierPositive {
        int[] value() default {Modifier.PUBLIC};
    }
    
    /** What fields should not be included
     * 
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ModifierNegative {
        int[] value() default {Modifier.TRANSIENT, Modifier.STATIC};
    }
    
    /** This method should be called after the object has been loaded from
     * the JSON format.
     * 
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface PostLoad {
    }
    
    /** Tells Poson that all fields in the class should have its name prefixed
     * with the given prefix.
     * 
     * XXX Currently not working
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface NamePrefix {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface InstanceClass {
        Class<?> value();
    }
    
    interface StopAtCurrentClass {
        // Marker interface for stopping at current class
    }
        
}
