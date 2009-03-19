/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.pojson;

/**
 *
 * @author phrebejk
 */
public class IgnoringBuilder<T,E extends Exception> implements PojsonBuilder<T,E> {

    private PojsonBuilder<T,E> parent;
    private int depth = 0;

    public IgnoringBuilder(PojsonBuilder<T,E> parent) {
        this.parent = parent;
    }

    public T build() throws E {
        return null;
    }

    public PojsonBuilder<T, E> field(String name) throws E {
        return this;
    }

    public PojsonBuilder<T, E> hash() throws E {
        depth++;
        return this;
    }

    public PojsonBuilder<T, E> array() throws E {
        depth++;
        return this;
    }

    public PojsonBuilder<T, E> value() throws E {
        return this;
    }

    public PojsonBuilder<T, E> value(String value) throws E {
        return this;
    }

    public PojsonBuilder<T, E> value(boolean value) throws E {
        return this;
    }

    public PojsonBuilder<T, E> value(long value) throws E {
        return this;
    }

    public PojsonBuilder<T, E> value(float value) throws E {
        return this;
    }

    public PojsonBuilder<T, E> value(double value) throws E {
        return this;
    }

    public PojsonBuilder<T, E> up() throws E {
        depth--;
        return depth == 0 ? parent : this;
    }

}
