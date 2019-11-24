/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.utils;

import java.util.Comparator;

/** XXX maybe make public later
 *
 * @author phrebejk
 */
final class ComparationUtil {

    private ComparationUtil() {}

    public static Comparator<Comparable> comparablesComparator() {
        return new ComparablesComparator();
    }

    private static class ComparablesComparator implements Comparator<Comparable> {

        public int compare(Comparable o1, Comparable o2) {
            return o1.compareTo(o2);
        }

    }

}
