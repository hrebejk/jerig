/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.lutz;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.NumberTools;
import org.codeviation.commons.reflect.ClassUtils;

/** Various methods for working with types and fields in lutz.
 *
 * XXX Some of the methods copied from Pojson.
 *
 * @author Petr Hrebejk
 */
class LutzUtils {

    private LutzUtils() {
        // Utility class
    };

    public static boolean isArrayType(Class type) {
        return type.isArray() || ClassUtils.isSuperinterface(type, Iterable.class);
    }



//    public static boolean isMapType(Class type) {
//        return ClassUtils.isSuperinterface(type, Map.class);
//    }

    public static boolean isLutzPrimitive(Class type) {

        if ( type == Integer.TYPE || type == Integer.class ||
             type == Long.TYPE || type == Long.class ||
             type == Short.TYPE || type == Short.class ||
             type == Byte.TYPE || type == Byte.class ||
             type == Boolean.TYPE || type == Boolean.class ||
             type == Character.TYPE || type == Character.class ||
             type == Date.class ||
             type.isEnum() ) {
            return true;
        }
        else {
            return false;
        }
    }

    public static String valueAsText( Object value ) {

        Class<?> type = value.getClass();

        if ( type == Integer.TYPE || type == Integer.class ||
             type == Long.TYPE || type == Long.class ||
             type == Short.TYPE || type == Short.class ||
             type == Byte.TYPE || type == Byte.class ) {
            return NumberTools.longToString(Long.valueOf(((Number)value).longValue()));
        }
        else if ( type == Boolean.TYPE || type == Boolean.class ) {
            return ((Boolean)value).booleanValue() ? "true" : "false";
        }
        else if ( type == Character.TYPE || type == Character.class ) {
            return ((Character)value).toString() ;
        }
        else if ( type == Date.class ) {
            return DateTools.dateToString((Date)value, DateTools.Resolution.MILLISECOND); //XXX
        }
        else if ( type.isEnum() ) {
            return value.toString();
        }
        else if ( type == String.class ) {
            return (String)value;
        }
        else {
            throw new IllegalArgumentException( "Can't convert value " + value);
        }

    }

    public static Class<?> resolveComponentType(Field f) {

        if (f.getType().isArray() ) {
            return f.getType().getComponentType();
        }
//        else if ( ClassUtils.isSuperinterface(f.getType(), Map.class ) ) {
//            Type gt = f.getGenericType();
//            if (gt instanceof ParameterizedType) {
//                ParameterizedType pt = (ParameterizedType)gt;
//                return (Class<?>)pt.getActualTypeArguments()[1];
//            }
//        }
        else if ( ClassUtils.isSuperinterface(f.getType(), Iterable.class ) ) {
            Type gt = f.getGenericType();
            if (gt instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType)gt;
                return (Class<?>)pt.getActualTypeArguments()[0];
            }
        }

        return null;

    }

    public static Iterator getIterator(Object object) {

        if (object.getClass().isArray() ) {
            return new ReflectiveArrayIterator(object);
        }
        else if (object instanceof Iterable ) {
            return ((Iterable)object).iterator();
        }
        else {
            return null;
        }

    }

    private static class ReflectiveArrayIterator implements Iterator {

        private Object a;
        private int len;
        private int ci = 0;

        ReflectiveArrayIterator(Object a) {
            this.a = a;
            this.len = Array.getLength(a);
        }


        public boolean hasNext() {
            return  ci < len;
        }

        public Object next() {
            ci++;
            return Array.get(a, ci - 1);
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

}
