/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.pojson;

/** SPI interface for both sides of pojson - reading and rendering
 *
 * @author Petr Hrebejk
 */
public interface PojsonBuilder<T,E extends Exception> {

    T build() throws E;

    PojsonBuilder<T,E> field( String name ) throws E;

    PojsonBuilder<T,E> hash() throws E;

    PojsonBuilder<T,E> array() throws E;

    PojsonBuilder<T,E> value() throws E;

    PojsonBuilder<T,E> value( String value ) throws E;

    PojsonBuilder<T,E> value( boolean value ) throws E;

    PojsonBuilder<T,E> value( long value ) throws E;

    PojsonBuilder<T,E> value( float value ) throws E;

    PojsonBuilder<T,E> value( double value ) throws E;

    PojsonBuilder<T,E> up() throws E;

}
