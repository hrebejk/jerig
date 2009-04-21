/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.strast;

/**
 *
 * @author phrebejk
 */
class IndexingUtils {

    private IndexingUtils() {}

    public static String getIndexName(Class<?> clazz) {
        Strast.Index ia = clazz.getAnnotation(Strast.Index.class);
        return ia == null ? null : ia.value();
    }
    

}
