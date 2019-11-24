/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.pojson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/** Default pojson object scanner.
 *
 * XXX Add field filter
 *
 * @author Petr Hrebejk
 */
class PojoWriter {

    private Map<Class<?>,Collection<Field>> fieldCache = new HashMap<Class<?>, Collection<Field>>();

    private Map<Object,Object> cycleDetector = new IdentityHashMap<Object, Object>();

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
            checkCyclic(object);
            builder = builder.hash();
            for( Object entry : ((Map)object).entrySet() ) {
                builder = builder.field( ((Map.Entry)entry).getKey().toString() );
                writeAny( ((Map.Entry)entry).getValue(), builder );
            }
            builder = builder.up();
            cycleDetector.remove(object);
        }

        else if ( object instanceof Iterable ) {
            checkCyclic(object);
            builder = builder.array();
            for( Object o : (Iterable)object ) {
                writeAny( o, builder );
            }
            builder = builder.up();
            cycleDetector.remove(object);
        }

        else if ( object.getClass().isArray() ) {
            checkCyclic(object);
            builder = builder.array();
            for ( int i = 0; i < Array.getLength(object); i++) {
                writeAny( Array.get(object, i), builder );
            }
            builder = builder.up();
            cycleDetector.remove(object);
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
            builder = builder.value(((Enum)object).name());
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

        else if ( object instanceof URI || object instanceof URL ) {
            builder = builder.value(object.toString());
        }

        else {
            // Object

            checkCyclic(object);

            builder = builder.hash();

            boolean isClassSkipNulls = object.getClass().isAnnotationPresent(Pojson.SkipNullValues.class);

            for( Field field : getFields( object.getClass()) ) {
                try {
                    field.setAccessible(true);
                    Object o = field.get(object);
                    if ( !( o == null &&
                            ( field.isAnnotationPresent(Pojson.SkipNullValues.class) || isClassSkipNulls )
                        )) {
                        builder = builder.field(PojsonUtils.getPojsonFieldName(field));
                        writeAny(o, builder);
                    }
                }
                catch (IllegalAccessException ex) {
                    throw new IllegalArgumentException(ex);
                }
            }
            builder = builder.up();
            cycleDetector.remove(object);
        }

    }

    private synchronized Collection<Field> getFields(Class<?> clazz) {
        Collection<Field> fields = fieldCache.get(clazz);
        if ( fields == null ) {
            fields = PojsonUtils.getFields( clazz ).values();
            fieldCache.put(clazz, fields);
        }
        return fields;
    }

    private boolean checkCyclic(Object object) {

        if ( cycleDetector.get(object) != null ) {
            throw new IllegalArgumentException("Object cycle detected.");
        }
        else {
            cycleDetector.put(object, object);
            return false;
        }

    }


 
}
