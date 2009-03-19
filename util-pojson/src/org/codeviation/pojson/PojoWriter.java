/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.pojson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.codeviation.commons.patterns.Filter;
import org.codeviation.commons.reflect.FieldUtils;

/** Default pojson object scanner.
 *
 * XXX Add cycle detection
 * XXX Add field filter
 * XXX Make it honor all annotations
 *
 * @author Petr Hrebejk
 */
class PojoWriter {

    // private PojsonBuilder<T,E> builder;

    public <T,X,E extends Exception> T writeTo(T object, PojsonBuilder<X,E> builder) throws E {
        writeAny(object, builder);
        builder.build();
        return object;
    }
    
    private <T,E extends Exception> void writeAny(Object object, PojsonBuilder<T,E> builder) throws E {

        if ( object == null || object instanceof Void ) {
            builder.value();
        }

        // Maps, Collections and Arrays

        else if ( object instanceof Map ) {
            builder = builder.hash();
            for( Object entry : ((Map)object).entrySet() ) {
                builder = builder.field( ((Map.Entry)entry).getKey().toString() );
                writeAny( ((Map.Entry)entry).getValue(), builder );
            }
            builder = builder.up();
        }

        else if ( object instanceof Iterable ) {
            builder = builder.array();
            for( Object o : (Iterable)object ) {
                writeAny( o, builder );
            }
            builder = builder.up();
        }

        else if ( object.getClass().isArray() ) {
            builder = builder.array();
            for ( int i = 0; i < Array.getLength(object); i++) {
                writeAny( Array.get(object, i), builder );
            }
            builder = builder.up();
        }

        // Date
        
        else if ( object instanceof Date ) {
            builder = builder.value(((Date)object).getTime() );
        }

        // Primitive types

        else if ( object instanceof String ) {
            builder = builder.value((String)object);
        }

        else if ( object instanceof Character ) {
            builder = builder.value( object.toString() );
        }

        else if ( object instanceof Enum ) {
            builder = builder.value(object.toString());
        }

        else if (object instanceof Boolean ) {
            builder = builder.value(((Boolean)object).booleanValue());
        }

        else if (object instanceof Double ) {
            builder = builder.value(((Double)object).doubleValue());
        }

        else if (object instanceof Float ) {
            builder = builder.value(((Float)object).floatValue());
        }

        else if (object instanceof Long || object instanceof Integer ||
                 object instanceof Byte || object instanceof Short )  {
            builder = builder.value(((Number)object).longValue());
        }

        else {
            // Object

            // XXX add AsBean annotation
            builder = builder.hash();
            for( Field field : PojsonUtils.getFields( object.getClass()) ) { // XXX add caching
                try {
                    field.setAccessible(true);
                    Object o = field.get(object);
                    if ( !( o == null && field.isAnnotationPresent(Pojson.SkipNullValues.class))) {
                        builder = builder.field(getPojsonFieldName(field));
                        writeAny(o, builder);
                    }
                }
                catch (IllegalAccessException ex) {
                    throw new IllegalArgumentException(ex);
                }
            }
            builder = builder.up();
        }

    }

    
    // XXX move me to PojsonUtils
    public static String getPojsonFieldName(Field f) {

        String name;

        Pojson.Name na = f.getAnnotation(Pojson.Name.class);

        if (na == null) {
            name = f.getName();
        }
        else {
           name = na.value();
           name = name == null ? f.getName() : name;
        }

        Pojson.NamePrefix np = f.getDeclaringClass().getAnnotation(Pojson.NamePrefix.class);
        if ( np != null ) {
            name = np.value() + name;
        }

        return name;

    }

    

}
