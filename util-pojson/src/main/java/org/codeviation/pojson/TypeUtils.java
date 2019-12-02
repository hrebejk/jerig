/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codeviation.pojson;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.codeviation.commons.reflect.ClassUtils;

/**
 *
 * @author phrebejk
 */
final class TypeUtils {

    private TypeUtils() {}

    
    public static boolean isJsonArrayType( Type type ) {

        Class rawType = getRawType(type);

        if ( rawType == Object.class ) {
            return false;
        }
        else if ( rawType.isArray() ) {
            return true;
        }
        else if ( rawType.isAssignableFrom( List.class ) ) {
            return true;
        }

        return false;
    }

     public static Type getJsonComponentType( Type type ) {

        if ( isJsonArrayType(type)) {            
            if ( type instanceof Class && ((Class)type).isArray() ) {
                return ((Class)type).getComponentType();
            }
            else if ( type instanceof ParameterizedType ) {
                ParameterizedType pt = (ParameterizedType)type;
                if ( pt.getActualTypeArguments().length == 1 ) {
                    return pt.getActualTypeArguments()[0];
                }
            }
            return Object.class;
        }
        if ( isJsonMapType(type) ) {
            if ( type instanceof ParameterizedType ) {
                ParameterizedType pt = (ParameterizedType)type;
                if ( pt.getActualTypeArguments().length == 2 ) {
                    return pt.getActualTypeArguments()[1];
                }
            }
            return Object.class;
        }

        throw new IllegalStateException( "Can't find component type of " + type);

    }

    static boolean isInstantiableList( Class clazz ) {
        if ( clazz.isInterface() ) {
            return false;
        }

        return ClassUtils.isSuperinterface(clazz, List.class);
    }

    static boolean isInstantiableMap( Class clazz ) {
        if ( clazz.isInterface() ) {
            return false;
        }

        return ClassUtils.isSuperinterface(clazz, Map.class);
    }

//    public static boolean isArrayType(Class type) {
//        return !isMapType(type)
//                type.isArray() || ClassUtils.isSuperinterface(type, Iterable.class);
//    }

    public static boolean isJsonMapType( Type type ) {

        Class rawType = getRawType(type);

        if ( ClassUtils.isSuperinterface(rawType, Map.class) ) {
            return true;
        }

        return false;

    }



    public static boolean isObjectType( Class type ) {
        return Object.class.equals(type);
    }

    public static boolean isJsonPrimitiveType( Type type ) {
        return isPrimitive( getRawType(type) );
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


    public static Class getRawType( Type type ) {

        if ( type instanceof Class ) {
            return (Class)type;
        }
        else if ( type instanceof ParameterizedType ) {
            return getRawType( ((ParameterizedType)type).getRawType() );
        }

        throw new IllegalArgumentException( "Can't find rawtype of " + type );

    }

     public static Object buildArray( Class componentType, List list ) {


        Object array = Array.newInstance( componentType, list.size() );
        int i = 0;
        for( Object e : list ) {
            Array.set(array, i++, e);
        }

        return array;
        
    }



}
