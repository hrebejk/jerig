/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.strast;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author phrebejk
 */
public interface Store {

    public InputStream getInputStream( String... path );

    public OutputStream getOutputStream( String... path );

    public boolean delete( String... path );

    public String[] list(String... path);

}
