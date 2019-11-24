/*
 * Objecto change this template, choose Objectools | Objectemplates
 * and open the template in the editor.
 */

package org.codeviation.pojson;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.codeviation.commons.patterns.Factories;
import org.codeviation.commons.reflect.ClassUtils;

/**
 *
 * @author Petr Hrebejk
 */
class PojoBuilder<T> implements PojsonBuilder<T,RuntimeException> {

    private Map<Class<?>,Map<String,Field>> fieldsCache = new HashMap<Class<?>, Map<String,Field>>();

    private String fieldName;
    
    private Class<?> componentType;

    private PojoBuilder<T> parent;

    private T result;
    private Object object;
    private List<Object> array;

    private PojoBuilder( PojoBuilder<T> parent, Object object ) {
        this.parent = parent;
        this.object = object;
    }

    private PojoBuilder( PojoBuilder<T> parent, Object object, Class<?> componentType ) {
        this( parent, object );
        this.componentType = componentType;
    }

    public static <T> PojsonBuilder<T,RuntimeException> create(T object) {
        PojoBuilder<T> pb = new PojoBuilder<T>( null, object );
        pb.result = object;

        if ( object.getClass().isArray() ) {
            pb.array = new ArrayList<Object>();
            pb.object = pb.array;
            pb.componentType = object.getClass().getComponentType();
        }

        return pb;
    }

    @SuppressWarnings("unchecked")
    public T build() throws RuntimeException {

        if ( Object.class.equals(result.getClass())) {
            return  (T) object;
        }

        if (result.getClass().isArray() ) {
            result = (T) array.toArray( (T[])result );
        }

        return result;
    }

    public PojsonBuilder<T, RuntimeException> field(String name) throws RuntimeException {
        fieldName = name;
        return this;
    }

    public PojsonBuilder<T, RuntimeException> hash() throws RuntimeException {

        if ( parent == null && fieldName == null ) {

            if ( isObjectType(object.getClass() ) ) {
                object = new LinkedHashMap();
            }

            parent = this;
            return this;    // call to hash on root
        }
        else if ( fieldName != null ) { // call to hash in an object field

            if ( isMapType(object.getClass())) {
                Object ni = new LinkedHashMap();
                set(ni);
                return new PojoBuilder<T>(this, ni);
            }

            Field field = getField( object, fieldName );

            if ( field == null ) {
                return new IgnoringBuilder<T,RuntimeException>(this).hash();
            }

            Class<?> fieldType = getFieldType(field);

            if ( isPrimitive(fieldType)) {
                throw new IllegalStateException("Object to primitive field?" + field);
            }
            else if (isMapType(fieldType) || isObjectType(fieldType)) {
                Object ni = new LinkedHashMap();
                set(ni);
                return new PojoBuilder<T>(this, ni);
            }
            else if (isArrayType(fieldType)) {
                throw new IllegalStateException("Hash into array type " + field );
            }
            else {
                Object ni = Factories.NEW_INSTANCE.create(fieldType);
                set(ni);
                return new PojoBuilder<T>(this, ni);
            }
        }
        else { // Call to hash in array

            if ( componentType == null ) { // Array does not know it's type and we are creating a hash
                Object ni = new LinkedHashMap();
                set(ni);
                return new PojoBuilder<T>(this, ni);
            }
            else if ( isMapType(componentType)) {
                Object ni = new LinkedHashMap();
                set(ni);
                return new PojoBuilder<T>(this, ni);
            }
            else if ( isPrimitive(componentType) ) {
                throw new IllegalStateException( "Hash in primitive array");
            }
            else if ( isArrayType(componentType) ) {
                array = new ArrayList<Object>();
                return new PojoBuilder<T>(this, array, null /** XXX wrong **/);
            }
            else {
                Object ni = Factories.NEW_INSTANCE.create(componentType);
                set(ni);
                return new PojoBuilder<T>(this, ni);
            }
        }
    }

    public PojsonBuilder<T, RuntimeException> array() throws RuntimeException {

        if ( parent == null && fieldName == null ) {  // call to array on root

            if ( isObjectType(object.getClass() ) ) {
                object = array = new LinkedList();
            }

            parent = this;
            return this;
        }
        else if ( fieldName != null ) { // call to array in an object field

            if ( isMapType(object.getClass())) {
                Object ni = new LinkedList();
                set(ni);
                return new PojoBuilder<T>(this, ni);
            }

            Field field = getField( object, fieldName );

            if ( field == null ) {
                return new IgnoringBuilder<T,RuntimeException>(this).array();
            }

            Class<?> fieldType = getFieldType(field);

            if ( isPrimitive(fieldType) ) {
                throw new IllegalStateException("Array to primitive field?" + field);
            }
            else if ( isMapType(fieldType)) {
                throw new IllegalStateException("Array to map field?" + field);
            }
            else if ( isArrayType(fieldType)) {
                array = new LinkedList<Object>();
                return new PojoBuilder<T>(this, array, resolveComponentType(field));
            }
            else if ( isObjectType(fieldType)) {
                array = new LinkedList<Object>();
                return new PojoBuilder<T>(this, array, null);
            }
            else {
                throw new IllegalStateException("Array to object field?" + field);
            }
        }
        else {  // call to array in array

            if ( componentType == null ) { // Array does not know it's type and we are creating an array
                Object no = new LinkedList();
            }
            else if ( isPrimitive(componentType) ) {
                throw new IllegalStateException( "Array into array of primitives?");
            }
            else if ( isMapType(componentType)) {
                throw new IllegalStateException( "Array into array of maps?");
            }
            else if ( isArrayType(componentType) ) {
                return new PojoBuilder<T>(this, new ArrayList<Object>(), componentType.getComponentType());
            }
            else {
                throw new IllegalStateException( "Array into array of objects?");
            }

        }

        return null;
    }

    public PojsonBuilder<T, RuntimeException> value() throws RuntimeException {
        set( null );
        return this;
    }

    public PojsonBuilder<T, RuntimeException> value(String value) throws RuntimeException {
        set(value);
        return this;
    }

    public PojsonBuilder<T, RuntimeException> value(boolean value) throws RuntimeException {
        set(value);
        return this;
    }

    public PojsonBuilder<T, RuntimeException> value(long value) throws RuntimeException {
        set(value);
        return this;
    }

    public PojsonBuilder<T, RuntimeException> value(float value) throws RuntimeException {
        set(value);
        return this;
    }

    public PojsonBuilder<T, RuntimeException> value(double value) throws RuntimeException {
        set(value);
        return this;
    }

    public PojsonBuilder<T, RuntimeException> up() throws RuntimeException {
        if ( parent != null) {
            if (parent.fieldName != null) {
                parent.set(parent.array);
            } else if (componentType != null && parent != this) {
                parent.set(object);
            }
        }
        if ( parent == this) {
            parent = null;
        }
        return parent == null ? this : parent;
    }

    @SuppressWarnings("unchecked")
    private void set(Object value) {
        try {
            if ( fieldName != null ) {          // Setting field in object or map
                if ( object instanceof Map ) {
                    ((Map)object).put(fieldName, value);
                }
                else {
                    Field field = getField(object, fieldName);
                    if ( field != null ) {
                        field.setAccessible(true);
                        field.set(object, JsonUtils.fromJSON(field.getType(), value));
                    }
                }
            }
            else { // Setting object in array
                ((List<Object>)object).add( componentType == null ?
                    value :
                    JsonUtils.fromJSON(componentType, value) );
            }
        }
        catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        }
        finally {
            fieldName = null;
        }
    }

    
    private Field getField(Object object, String name) {
        Map<String,Field> fields = getFields(object.getClass());
        Field f = fields.get(name);
        if ( f == null ) {
            if ( object.getClass().isAnnotationPresent(Pojson.IgnoreNonExisting.class ) ) {
                return null;
            }
            else {
                throw new IllegalArgumentException( new NoSuchFieldException("Field " + name + " not found in class " + object.getClass().getName() + "."));
            }
        }

        return f;

    }

    private static Class<?> getFieldType( Field field ) {
        // XXX add resolution of Annotations
        return field.getType();
    }

    public static Class<?> resolveComponentType(Field f) {

        if (f.getType().isArray() ) {
            return f.getType().getComponentType();
        }
        else if ( ClassUtils.isSuperinterface(f.getType(), Map.class ) ) {
            Type gt = f.getGenericType();
            if (gt instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType)gt;
                return (Class<?>)pt.getActualTypeArguments()[1];
            }
        }
        else if ( ClassUtils.isSuperinterface(f.getType(), Iterable.class ) ) {
            Type gt = f.getGenericType();
            if (gt instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType)gt;
                return (Class<?>)pt.getActualTypeArguments()[0];
            }
        }

        return null;

    }

    private static boolean isPrimitive(Class type) {

        if ( type.isPrimitive() ) {
            return true;
        }

        if ( type.isEnum() ) {
            return true;
        }

        if ( Boolean.class.equals(type) ||
             Byte.class.equals(type) ||
             Short.class.equals(type) ||
             Character.class.equals(type) ||
             Integer.class.equals(type) ||
             Long.class.equals(type) ||
             Float.class.equals(type) ||
             Double.class.equals(type) ||
             Void.class.equals(type) ||
             String.class.equals(type) ||
             URL.class.equals(type) ||
             URI.class.equals(type) ) {
            return true;
        }

         if ( Date.class.equals(type) || ClassUtils.isSuperclass(type, Date.class)) {
            return true;
         }

        return false;
    }

    private boolean isArrayType(Class type) {
        return type.isArray() || ClassUtils.isSuperinterface(type, Iterable.class);
    }

    private boolean isMapType(Class type) {
        return ClassUtils.isSuperinterface(type, Map.class);
    }

    private boolean isObjectType( Class type ) {
        return Object.class.equals(type);
    }

    private synchronized Map<String,Field> getFields(Class<?> clazz) {

        Map<String,Field> fields = fieldsCache.get(clazz);
        if ( fields == null ) {
            fields = new HashMap<String, Field>();
            for (Field f : PojsonUtils.getFields(clazz).values()) {
                fields.put(PojsonUtils.getPojsonFieldName(f), f);
            }
        }
        return fields;
    }

}
