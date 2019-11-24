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
import java.net.URI;
import java.net.URL;

/** Contains annotations for driving the save and load process of objects and
 * convenience utility methods for saving/loading Java objects to/from JSON
 * format. In case the of storing or loading larger number of objects (which
 * may profit from caching) or if you need modify proprties of the
 * marhalling/unmarshalling process and/or stricter type checking please use
 * the {@link Marshaller}, {@link UnMarshaller} classes.

 * @author Petr Hrebejk
 */
public class Pojson {

    static final String DEFAULT_EXTENSION = ".json";
    
    private Pojson() {}

    
    /** Writes JSON representation of the object into a String. This is a
     * convenience method, which uses standard formating and does not cache
     * information about serialized classes. If you need more control of the
     * formating the marshalling process or if you want to achieve slightly better 
     * better performance please use the {@link Marshaller} class.
     *
     * @param object Object to be marshaled to JSON format.
     * @return String containing JSON representation of the object.
     */
    public static String save( Object object ) {
        return new Marshaller<>().save(object);
    }

    /** Writes JSON representation of the object into a Writer. This is a
     * convenience method, which uses standard formating and does not cache
     * information about serialized classes. If you need more control of the
     * formating the marshalling process or if you want to achieve slightly better
     * better performance please use the {@link Marshaller} class.<BR>
     * This method does NOT close the Writer after it writes to it.
     *
     * @param object Object to be marshaled to JSON format.
     * @param writer The Writer to marshall the object into.
     * @throws IOException when it can't write the object for some reason.
     */
    public static void save( Object object, Writer writer ) throws IOException {
        new Marshaller<>().save(object, writer);
    }

    /** Writes JSON representation of the object into a Stream. This is a
     * convenience method, which uses standard formating and does not cache
     * information about serialized classes. If you need more control of the
     * formating the marshalling process or if you want to achieve slightly better
     * better performance please use the {@link Marshaller} class.<BR>
     * This method does NOT close the Stream after it writes to it.
     *
     * @param object Object to be marshaled to JSON format.
     * @param outputStream The Stream to marshall the object into.
     * @throws IOException when it can't write the object for some reason.
     */
    public static void save( Object object, OutputStream outputStream ) throws IOException {
        new Marshaller<>().save(object, outputStream);
    }

    /** Writes JSON representation of the object into a File. This is a
     * convenience method, which uses standard formating and does not cache
     * information about serialized classes. If you need more control of the
     * formating the marshalling process or if you want to achieve slightly better
     * better performance please use the {@link Marshaller} class.<BR>
     * This method does NOT close the Stream after it writes to it.
     *
     * @param object Object to be marshaled to JSON format.
     * @param file The File to marshall the object into.
     * @throws IOException when it can't write the object for some reason.
     */
    public static void save( Object object, File file ) throws IOException {
        new Marshaller<>().save(object, file);
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

    public static <T> T load(Class<T> clazz, URI uri ) throws IOException{
        return new UnMarshaller().load(clazz, uri);
    }

    public static <T> T load(Class<T> clazz, URL url ) throws IOException{
        return new UnMarshaller().load(clazz, url);
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

    public static <T> T update(T object, URI uri ) throws IOException{
        return new UnMarshaller().update(object, uri);
    }

    public static <T> T update(T object, URL url ) throws IOException{
        return new UnMarshaller().update(object, url);
    }



    /** General annotation to mark classes as Pojson records. This annotation
     * may be used for other frameworks to distinguish between serializable and
     * not serializable objects. The annotation is currently not used by the
     * Pojson library itself.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Record {
        // Intentionally empty
    }

    /** Tells Pojson that given field should not be stored. Fields marked with
     * this annotation will be excluded from the marshalling process even if
     * they would be serialezed normally.
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface SuppressStoring {
        // Intentionally empty
    }

    /** Tells Pojson where to stop inspecting the class hierarchy. By default
     * Pojson only stores/loads the fields in the last class in the hierarchy.
     * E.g. it does not serialize values of any parent classes. With this annotation
     * you may ordr pojson to go deeper in the hirarachy. If used then all classes
     * from the last to the class passed as parameter into the annotation will
     * (inclusive) will be taken into account when marshalling/unmarshlling objects.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.FIELD} )
    public @interface StopAt {
        Class value() default StopAtCurrentClass.class;
    }

    interface StopAtCurrentClass {
        // Marker interface for stopping at current class
    }

    /** Tells Pojson that given should be stored under diferent name than the
     * name of given field. This annotation is good for renamining fields in
     * the resulting JSON representation. You may need to use this annotation for
     * example in cases where you need to a field to by named by a Java keyword
     * (such as package) or by some identifier which is not alowed in Java. (such
     * as identifier containig spaces or other disallowed charaters).
     *
     * @author Petr Hrebejk
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Name {
        String value();
    }
    
    /** Tells Pojson not to store fields whose value is null. I.e. fields
     * containig null will not appear in the resulting JSON representation.
     * If used on class it is valid for all fields in the class.
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD })
    public @interface SkipNullValues {
    }


    /** Tells Pojson not to complain about nonexisting fields in the class
     * when unmarshalling the data from the JSON representation. I.e. loading
     * a JSON text which contains field which do not exist in the Java class
     * will not result into a exception.
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE })
    public @interface IgnoreNonExisting {
    }

    /** Tells Pojson which fields should be used based on theirs modifiers. Works
     * toghether with the {@link ModifierNegative} annotation. To get to the final
     * set of fields all fields which match the {@link ModifierPositive} filter
     * are taken into account and then thos fields which match the
     * {@link ModifierNegative} filter are subtracted from the set.
     * <br>
     * If the annotation is not used then all fields are taken into account.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ModifierPositive {
        int[] value();
    }

    /** Tells Pojson which fields should NOT be used based on theirs modifiers.
     * Works toghether with the {@link ModifierPositive} annotation. To get to the final
     * set of fields all fields which match the {@link ModifierPositive} filter
     * are taken into account and then thos fields which match the
     * {@link ModifierNegative} filter are subtracted from the set.
     * <br>
     * If the annotation is not used then fields { {@link java.lang.reflect.Modifier.TRANSIENT},
     * and {@link java.lang.reflect.Modifier.STATIC} } are ignored by default.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ModifierNegative {
        int[] value();
    }


    // The other annotations are currently not used. -------------------------------
   
    /** Tells Pojson that given field should be stored by calling to String
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface ToString {
        // XXX Add factory for from string back to object
        //Class<Factory<Object,String>> value();
    }
    
    
    
    /** This method should be called after the object has been loaded from
     * the JSON format.
     * 
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface PostLoad {
    }
    
    /** Tells Poson that all fields in the class should have its name prefixed
     * with the given prefix.
     * 
     * XXX Currently not working
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface NamePrefix {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface InstanceClass {
        Class<?> value();
    }
    
    
        
}
