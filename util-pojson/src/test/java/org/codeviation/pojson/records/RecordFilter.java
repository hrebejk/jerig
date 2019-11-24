/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.pojson.records;

/**
 *
 * @author phrebejk
 */
public class RecordFilter {

    public static class Default {

        public static String puSt = "public static";
        private static String prSt = "private static";
        protected static String ptSt = "protected static";
        static String paSt = "package static";

        public transient String puTr = "public transient";
        private transient  String prTr = "private transient";
        protected transient String ptTr = "protected transient";
        transient String paTr = "package transient";

        public String pu = "public";
        private String pr = "private";
        protected String pt = "protected";
        String pa = "package";

    }


}
