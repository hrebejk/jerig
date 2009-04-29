/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.strast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.codeviation.commons.patterns.Factory;
import org.codeviation.pojson.Marshaller;
import org.codeviation.pojson.UnMarshaller;

/**
 * @author Petr Hrebejk
 */
class ObjectStorage implements Storage {

    private Store store;
    private Marshaller<Object> save;
    private UnMarshaller load;
    private Factory<String,Object> nameFactory;

    public ObjectStorage(Store store) throws IOException {
        this.store = store;
        this.save = new Marshaller<Object>();
        this.load = new UnMarshaller();
        this.nameFactory = new ObjectNameFactory<Object>();
    }

    public <T> T get(Class<T> clazz, String... path) {

        try {
            InputStream is = store.getInputStream(path);
            return load.load(clazz, is);
        }        
        catch (IOException ex) {
            throw new IllegalStateException(ex);
        }

    }

    public boolean put(Object o, String... path) {
        
        if ( path == null || o.getClass().isAnnotationPresent(Strast.SuppressStoring.class)) { // Not a Record do not store
            return false;
        }

        OutputStream os = null;
        try {
            os = store.getOutputStream(path);
            save.save(o, os);
            return true;
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        finally {
            if ( os != null ) {
                try {
                    os.close();
                }
                catch (IOException ex) {
                    throw new IllegalStateException(ex);
                }
            }
        }

    }

    public void close() {
        // Does nothing
    }

    public boolean delete(String... path) {
        store.delete(path);
        return false;
    }

    public Store getStore() {
        return store;
    }

}
