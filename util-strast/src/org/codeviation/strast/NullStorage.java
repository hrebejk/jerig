/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.strast;

/** Does nothing always reports success.
 *
 * @author Petr Hrebejk
 */
class NullStorage implements Storage {

    public <T> T get(Class<T> clazz, String... path) {
        return null;
    }

    public boolean put(Object o, String... path) {
        return false;
    }

    public boolean delete(String... path) {
        return true;
    }

    public void close() {
        // Does nothing;
    }

    public Store getStore() {
        return null;
    }

}
