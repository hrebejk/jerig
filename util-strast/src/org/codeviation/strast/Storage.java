/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.strast;

/**
 *
 * @author phrebejk
 */
public interface Storage {

    public Store getStore();

    public <T> T get(Class<T> clazz, String... path);

    public boolean put(Object o, String... path);

    public boolean delete(String... path);

    public void close();
    
}
