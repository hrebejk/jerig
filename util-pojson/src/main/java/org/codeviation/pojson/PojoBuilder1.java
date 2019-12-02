/*
 * Objecto change this template, choose Objectools | Objectemplates
 * and open the template in the editor.
 */
package org.codeviation.pojson;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.codeviation.commons.patterns.Factories;
import org.codeviation.commons.reflect.ClassUtils;
import static org.codeviation.pojson.TypeUtils.*;

/**
 *
 * @author Petr Hrebejk
 */
class PojoBuilder1<T> {

    public static <T> PojsonBuilder<T, RuntimeException> create(T object) {

        return new RootBuilder( object );

    }

    private static interface StackableBuilder<T> extends PojsonBuilder<T, RuntimeException> {

        void consume( Object o );

        Map<Class<?>,Map<String,Field>> getFieldsCache();

    }

    private static class RootBuilder<T> implements StackableBuilder<T> {

        private T result;

        private Class type;

        private final Map<Class<?>,Map<String,Field>> fieldsCache = new HashMap<>();

        RootBuilder( T object ) {
            this.result = object;
            this.type = object.getClass();
        }

        @Override
        public Map<Class<?>,Map<String,Field>> getFieldsCache() {
            return fieldsCache;
        }

        @Override
        public void consume(Object o) {
            result = (T)o;
        }

        @Override
        public T build() throws RuntimeException {
            return result;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> field(String name) throws RuntimeException {
            throw new IllegalStateException( getErrorMessage() );
        }

        @Override
        public PojsonBuilder<T, RuntimeException> hash() throws RuntimeException {
            if ( isJsonArrayType(type) ) {
                throw new IllegalStateException( getErrorMessage() );
            }
            else if ( ClassUtils.isSuperinterface(type, Map.class) ) {
                return new MapBuilder( this, (Map)result );
            }
//            else if ( isJsonMapType(type) ) {
//                return new MapBuilder( this, type );
//            }
            else if ( type == Object.class ) {
                return new MapBuilder( this, new LinkedHashMap() );
            }
            else {
                return new ObjectBuilder( this, result );
            }

        }

        @Override
        public PojsonBuilder<T, RuntimeException> array() throws RuntimeException {
            if ( ClassUtils.isSuperinterface(type, List.class) ) {
                return new ArrayBuilder( this, (List)result );
            }
            else if ( isJsonArrayType(type) ) {
                return new ArrayBuilder( this, type );
            }
            else if ( type == Object.class ) {
                return new ArrayBuilder( this, new LinkedList() );
            }
            throw new IllegalStateException( getErrorMessage() );
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value() throws RuntimeException {
            throw new IllegalStateException( getErrorMessage() );
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(String value) throws RuntimeException {
            throw new IllegalStateException( getErrorMessage() );
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(boolean value) throws RuntimeException {
            throw new IllegalStateException( getErrorMessage() );
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(long value) throws RuntimeException {
            throw new IllegalStateException( getErrorMessage() );
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(float value) throws RuntimeException {
            throw new IllegalStateException( getErrorMessage() );
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(double value) throws RuntimeException {
            throw new IllegalStateException( getErrorMessage() );
        }

        @Override
        public PojsonBuilder<T, RuntimeException> up() throws RuntimeException {
            throw new IllegalStateException( getErrorMessage() );
        }

        private String getErrorMessage() {
            return isJsonArrayType(type) ?
                    "Awaiting array" : "Awaiting object";
        }

    }

    private static class ArrayBuilder<T> implements StackableBuilder<T> {

        private final Type type;

        private final Type componentType;

        private final StackableBuilder<T> parent;

        private final List<Object> result;

        ArrayBuilder( StackableBuilder<T> parent, Type type ) {
            this.parent = parent;
            this.type = type;
            this.componentType = getJsonComponentType(type);
            this.result = new LinkedList<>();
        }

        ArrayBuilder( StackableBuilder<T> parent, List result ) {
            this.parent = parent;
            this.type = result.getClass();
            this.componentType = Object.class;
            this.result = result;
        }

        @Override
        public Map<Class<?>,Map<String,Field>> getFieldsCache() {
            return parent.getFieldsCache();
        }

        @Override
        public PojsonBuilder<T, RuntimeException> field(String name) throws RuntimeException {
            throw new IllegalStateException( "Field in array" );
        }

        @Override
        public PojsonBuilder<T, RuntimeException> hash() throws RuntimeException {

            if (isJsonArrayType(componentType)) {
                throw new IllegalStateException("Hash in array of array?");
            }
            else if ( isJsonPrimitiveType(componentType) ) {
                throw new IllegalStateException("Hash in array of primitives?");
            }
            else if ( isJsonMapType(componentType) ) {
                return new MapBuilder(this, componentType);
            }
            else if ( Object.class == componentType ) {
                return new MapBuilder(this, new LinkedHashMap() );
            }
            
            return new ObjectBuilder( this, componentType );
            
        }

        @Override
        public PojsonBuilder<T, RuntimeException> array() throws RuntimeException {

            // call to array in array

            if ( isJsonPrimitiveType(componentType) ) {
                throw new IllegalStateException("Array into array of primitives?");
            }
            else if (isJsonMapType(componentType) ) {
                throw new IllegalStateException("Array into array of maps?");
            }
            else if (isJsonArrayType(componentType)) {
                return new ArrayBuilder(this, componentType);
            }
            else if ( Object.class == componentType ) {
                return new ArrayBuilder(this, new LinkedList() );
            }

            throw new IllegalStateException("Array into array of objects?");

        }

        @Override
        public PojsonBuilder<T, RuntimeException> value() throws RuntimeException {
            add( null );
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(String value) throws RuntimeException {
            add(value);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(boolean value) throws RuntimeException {
            add(value);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(long value) throws RuntimeException {
            add(value);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(float value) throws RuntimeException {
            add(value);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(double value) throws RuntimeException {
            add(value);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> up() throws RuntimeException {

            // XXX JsonUtils.fromJSON(componentType, value) )

            if (parent != null) {
                parent.consume( build() );
                return parent;
            }

            throw new IllegalStateException( "Should not happen");
        }

        @Override
        public void consume( Object object ) {
            add(object);
        }

        @Override
        public T build() {

            if ( getRawType(type).isArray() ) {
                return (T) TypeUtils.buildArray(getRawType(componentType), result);
            }

            return (T)result;
        }

        private void add( Object value ) {
            result.add(  JsonUtils.fromJSON(getRawType(componentType), value) );
        }

    }

    private static class MapBuilder<T> implements StackableBuilder<T> {

        // private final Type type;

        private final Type componentType;

        private final StackableBuilder<T> parent;

        private final Map map;

        private String fieldName;

        MapBuilder( StackableBuilder<T> parent, Type type ) {
            this.parent = parent;
            this.componentType = getJsonComponentType(type);
            this.map = new LinkedHashMap();
        }

        MapBuilder( StackableBuilder<T> parent, Map result ) {
            this.parent = parent;
            this.map = result;
            this.componentType = Object.class;
        }

        @Override
        public Map<Class<?>,Map<String,Field>> getFieldsCache() {
            return parent.getFieldsCache();
        }

        @Override
        public PojsonBuilder<T, RuntimeException> field(String name) throws RuntimeException {
            if ( fieldName != null ) {
                throw new IllegalStateException();
            }
            fieldName = name;
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> hash() throws RuntimeException {


            if (fieldName == null) {
                throw new IllegalStateException();
            }
            if (isJsonArrayType(componentType)) {
                throw new IllegalStateException("Hash in hash of arrays?");
            }
            else if ( isJsonPrimitiveType(componentType) ) {
                throw new IllegalStateException("Hash in hash of primitives?");
            }
            else if ( isJsonMapType(componentType) ) {
                return new MapBuilder( this, componentType );
            }
            else if ( Object.class == componentType ) {
                return new MapBuilder(this, new LinkedHashMap() );
            }

            return new ObjectBuilder( this, componentType );

        }

        @Override
        public PojsonBuilder<T, RuntimeException> array() throws RuntimeException {

            if (fieldName == null) {
                throw new IllegalStateException();
            }
            if (isJsonMapType(componentType)) {
                throw new IllegalStateException("Array in hash of hashes?");
            }
            else if ( isJsonPrimitiveType(componentType) ) {
                throw new IllegalStateException("Array in hash of primitives?");
            }
            else if ( isJsonArrayType(componentType) ) {
                return new ArrayBuilder( this, componentType );
            }
            else if ( Object.class == componentType ) {
                return new ArrayBuilder(this, new LinkedList() );
            }

            throw new IllegalStateException( "Array in hash of objects?");

        }

        @Override
        public PojsonBuilder<T, RuntimeException> value() throws RuntimeException {
            set(null);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(String value) throws RuntimeException {
            set(value);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(boolean value) throws RuntimeException {
            set(value);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(long value) throws RuntimeException {
            set(value);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(float value) throws RuntimeException {
            set(value);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(double value) throws RuntimeException {
            set(value);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> up() throws RuntimeException {
            if (parent != null) {
                parent.consume( build() );
                return parent;
            }
            throw new IllegalStateException();
        }
        
        @Override
        public void consume( Object o ) {
            set(o);
        }

        @Override
        public T build() {
            return (T) map;
        }

        void set( Object value ) {
            try {
                if ( fieldName == null ) {
                    throw new IllegalStateException();
                }

                map.put(fieldName, JsonUtils.fromJSON(getRawType(componentType), value) );
            }
            finally {
                fieldName = null;
            }

        }
    }


    private static class ObjectBuilder<T> implements StackableBuilder<T> {

        private final Object object;
        private final StackableBuilder<T> parent;

        private String fieldName;

        ObjectBuilder( StackableBuilder<T> parent, Type type ) {
            this.parent = parent;
            this.object = Factories.NEW_INSTANCE.create(getRawType(type) );
        }

        ObjectBuilder( StackableBuilder<T> parent, Object result ) {
            this.parent = parent;
            this.object = result;
        }

        @Override
        public Map<Class<?>,Map<String,Field>> getFieldsCache() {
            return parent.getFieldsCache();
        }

        @Override
        public PojsonBuilder<T, RuntimeException> field(String name) throws RuntimeException {
            if ( fieldName != null ) {
                throw new IllegalStateException();
            }
            fieldName = name;
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> hash() throws RuntimeException {

            if ( fieldName == null) {
                throw new IllegalStateException();
            } 
                
            Field field = getField(object, fieldName);

            if (field == null) {
                fieldName = null;
                return new IgnoringBuilder<>(this).hash();
            }


            Class ft = field.getType();

            if ( ft == Object.class ) {
                return new MapBuilder( this, new LinkedHashMap() );
            }
            
            if( isInstantiableMap( ft ) ) {
                return new MapBuilder( this, (Map)Factories.NEW_INSTANCE.create( ft ) );
            }

            Type gft = field.getGenericType();

            if ( isJsonArrayType(gft) ) {
                throw new IllegalStateException();
            }
            if ( isJsonMapType(gft) ) {
                return new MapBuilder( this, gft );
            }

            return new ObjectBuilder( this, gft );

        }

        @Override
        public PojsonBuilder<T, RuntimeException> array() throws RuntimeException {

            if ( fieldName == null) {
                throw new IllegalStateException();
            }

            Field field = getField(object, fieldName);

            if (field == null) {
                return new IgnoringBuilder<>(this).hash();
            }

            Class ft = field.getType();

            if ( ft == Object.class ) {
                return new ArrayBuilder( this, new LinkedList() );
            }

            if( isInstantiableList( ft ) ) {
                return new ArrayBuilder( this, (List)Factories.NEW_INSTANCE.create( ft ) );
            }

            Type gft = field.getGenericType();

            if ( !isJsonArrayType( gft ) ) {
                throw new IllegalStateException();
            }

            return new ArrayBuilder( this, gft );
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value() throws RuntimeException {
            set(null);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(String value) throws RuntimeException {
            set(value);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(boolean value) throws RuntimeException {
            set(value);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(long value) throws RuntimeException {
            set(value);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(float value) throws RuntimeException {
            set(value);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> value(double value) throws RuntimeException {
            set(value);
            return this;
        }

        @Override
        public PojsonBuilder<T, RuntimeException> up() throws RuntimeException {
            if (parent != null) {
                parent.consume( build () );
                return parent;
            }
            throw new IllegalStateException( "Should not happen");
        }

        @Override
        public void consume( Object object ) {
            set( object );
        }
        
        @Override
        public T build() {
          return (T) object; 
        }

        @SuppressWarnings("unchecked")
        private void set(Object value) {
            try {
                if (fieldName == null) {
                    throw new IllegalStateException();
                }

                Field field = getField(object, fieldName);
                if (field != null) {
                    field.setAccessible(true);
                    field.set(object, JsonUtils.fromJSON(field.getType(), value));
                }
            }
            catch( IllegalAccessException ex ) {
                throw new IllegalArgumentException(ex);
            }
            finally {
                fieldName = null;
            }
            
        }

        private Field getField(Object object, String name) {
            Map<String, Field> fields = getFields(object.getClass());
            Field f = fields.get(name);
            if (f == null) {
                if (object.getClass().isAnnotationPresent(Pojson.IgnoreNonExisting.class)) {
                    return null;
                } else {
                    throw new IllegalArgumentException(new NoSuchFieldException("Field " + name + " not found in class " + object.getClass().getName() + "."));
                }
            }

            return f;
        }

        private synchronized Map<String,Field> getFields(Class<?> clazz) {

            Map<String,Field> fields = getFieldsCache().get(clazz);
            if ( fields == null ) {
                fields = new HashMap<>();
                for (Field f : PojsonUtils.getFields(clazz).values()) {
                    fields.put(PojsonUtils.getPojsonFieldName(f), f);
                }
            }
            return fields;

        }

    }

    

    
//    private static boolean isPrimitive(Class type) {
//
//        if (type.isPrimitive()) {
//            return true;
//        }
//
//        if (type.isEnum()) {
//            return true;
//        }
//
//        if (Boolean.class.equals(type)
//                || Byte.class.equals(type)
//                || Short.class.equals(type)
//                || Character.class.equals(type)
//                || Integer.class.equals(type)
//                || Long.class.equals(type)
//                || Float.class.equals(type)
//                || Double.class.equals(type)
//                || Void.class.equals(type)
//                || String.class.equals(type)
//                || URL.class.equals(type)
//                || URI.class.equals(type)) {
//            return true;
//        }
//
//        if (Date.class.equals(type) || ClassUtils.isSuperclass(type, Date.class)) {
//            return true;
//        }
//
//        return false;
//    }
//
//    private boolean isArrayType(Class type) {
//        return type.isArray() || ClassUtils.isSuperinterface(type, Iterable.class);
//    }
//
//    private boolean isMapType(Class type) {
//        return ClassUtils.isSuperinterface(type, Map.class);
//    }
//
//    private boolean isObjectType(Class type) {
//        return Object.class.equals(type);
//    }
//
//    private synchronized Map<String, Field> getFields(Class<?> clazz) {
//
//        Map<String, Field> fields = fieldsCache.get(clazz);
//        if (fields == null) {
//            fields = new HashMap<>();
//            for (Field f : PojsonUtils.getFields(clazz).values()) {
//                fields.put(PojsonUtils.getPojsonFieldName(f), f);
//            }
//        }
//        return fields;
//    }

}


