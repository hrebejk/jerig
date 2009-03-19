/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.pojson;

/**
 *
 * @author phrebejk
 */
public interface JsonBuilder<T,E extends Exception> {

    public T build() throws E;

    public static interface Root<T, E extends Exception> extends JsonBuilder<T,E> {

        public Hash<T,E> hash() throws E;

        public Array<T,E> array() throws E;

    }

    public static interface Hash<T,E extends Exception> extends JsonBuilder<T,E> {

//        /** Adds field of type String*/
//        Hash<T,E> field(String name, String value) throws E;
//
//        /** Adds field of type String*/
//        Hash<T,E> field(String name, boolean value) throws E;
//
//        Hash<T,E> field(String name, long value) throws E;
//
//        Hash<T,E> field(String name, double value) throws E;

        Value<Hash<T,E>,T,E> f(String name) throws E;



    }

    public static interface Array<T,E extends Exception> extends JsonBuilder<T,E> {
        
//        Array<T,E> value(String value) throws E;
//
//        Array<T,E> value(boolean value) throws E;
//
//        Array<T,E> value(long value) throws E;
//
//        Array<T,E> value(double value) throws E;
        
        Value<Array<T,E>, T,E> e() throws E;

    }

    public static interface Value<R extends JsonBuilder<T,E>,T,E extends Exception> extends JsonBuilder<T,E> {
        
        R v() throws E;
        
        R v(String value) throws E;

        R v(boolean value) throws E;

        R v(long value) throws E;

        R v(double value) throws E;
        
        R v( Hash<T,E> builder ) throws E;
        
        R v( Array<T,E> builder ) throws E;


    }


}
