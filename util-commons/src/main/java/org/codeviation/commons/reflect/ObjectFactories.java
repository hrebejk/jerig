/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.reflect;

import java.lang.reflect.Field;
import java.util.Map;
import org.codeviation.commons.patterns.Factories;
import org.codeviation.commons.patterns.Factory;

/**
 *
 * @author phrebejk
 */
final class ObjectFactories {

    private ObjectFactories() {}

    public static <T> Factory<T,Map<? extends Object, ? extends Object>> collection2Object(Class<T> clazz) {
        return new CollectionToObject( clazz );
    }

    private static class CollectionToObject<T> implements Factory<T,Map<? extends Object,? extends Object>> {

        private Class<T> clazz;

        public CollectionToObject(Class<T> clazz) {
            this.clazz = clazz;
        }

        public T create(Map<? extends Object, ? extends Object> map) {
            T object = Factories.NEW_INSTANCE.create(clazz);

            Map<String, Field> fields = FieldUtils.getAll(clazz, Object.class, null);

            for (Map.Entry<? extends Object, ? extends Object> entry : map.entrySet()) {
                Field field = fields.get(entry.getKey().toString());

                if ( field != null ) {
                    setField( field, object, entry.getValue());
                }
                else {
                    throw new IllegalArgumentException(new NoSuchFieldException(entry.getKey().toString()));
                }

            }

            return object;
        }

        private void setField( Field field, Object object, Object value ) {

            Class<?> ft = field.getType();
            Class<?> vt = value.getClass();

            Object o = null;

            if ( value == null ) {
                o = null;
            }
            else if ( ft.isPrimitive() && ClassUtils.primitive2Object(ft).isAssignableFrom(vt) ) {
                o = value;
            }
            else if ( ft.isAssignableFrom(vt) ) {
                o = value;
            }
            else if ( value instanceof Map ) {
                Factory f = collection2Object(ft);
                o = f.create((Map) value);
            }
            else {
                throw new IllegalArgumentException("Can't handle field " + field.getName() + " and value " + value);
            }

            try {
                field.setAccessible(true);
                field.set(object, o);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(ex);
            } catch (IllegalAccessException ex) {
                throw new IllegalArgumentException(ex);
            }

        }

    }

}
