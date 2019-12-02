/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.pojson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import org.codeviation.commons.patterns.Factories;
import org.codeviation.commons.utils.UrlUriUtil;

/**
 *
 * @author phrebejk
 */
public class UnMarshaller {

    public <T> T load( Class<T> clazz, String string ) {
        try {
            return load(clazz, new StringReader(string));
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public <T> T load( Class<T> clazz, Reader reader ) throws IOException {
        T object = Factories.NEW_INSTANCE.create( clazz );
        return update( object, reader );
    }

    public <T> T load( Class<T> clazz, InputStream inputStream ) throws IOException {
        return load( clazz, new InputStreamReader(inputStream) );
    }

    public <T> T load( Class<T> clazz, File file ) throws FileNotFoundException, IOException {
        return load( clazz, new FileReader(file) );
    }

    public <T> T load(Class<T> clazz, URI uri ) throws IOException{
        return load( clazz, UrlUriUtil.getInputStream(uri));
    }

    public <T> T load(Class<T> clazz, URL url ) throws IOException{
        return load( clazz, UrlUriUtil.getInputStream(url));
    }

    public <T> T update( T object, String string ) {
        try {
            return update(object, new StringReader(string));
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public <T> T update( T object, Reader reader ) throws IOException {
        PojsonBuilder<T,RuntimeException> pb = PojoBuilder1.create(object);
        Parser2.parse(reader, pb);
        return pb.build();
    }

    public <T> T update( T object, InputStream inputStream ) throws IOException {
        return update( object, new InputStreamReader(inputStream) );
    }

    public <T> T update( T object, File file ) throws FileNotFoundException, IOException {
        return update( object, new FileReader(file) );
    }

    public <T> T update(T object, URI uri ) throws IOException{
        return update( object, UrlUriUtil.getInputStream(uri));
    }

    public <T> T update(T object, URL url ) throws IOException{
        return update( object, UrlUriUtil.getInputStream(url) );
    }


}
