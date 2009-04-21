/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.lutz;

import java.util.Date;
import java.util.List;

/**
 *
 * @author phrebejk
 */
public class Records {

    public static class Primitve {

        public boolean bln;

        public int intgr;

        public long lng;

        public short shrt;

        public byte bt;

        public char chr;

    }

    public static class Object {

        public Boolean bln;

        public Integer intgr;

        public Long lng;

        public Short shrt;

        public Byte bt;

        public Character chr;
        
        public Date dt;

        public String strng;
    }

    public static class Array {

        public String[] strng;

        public  int[] intgr;

    }

    public static class ArrayAsTokens {

        @Lutz.AsTokens( ':' )
        public String[] strng;

        @Lutz.AsTokens
        public int[] intgr;

    }

    public static class Suppres {

        @Lutz.SuppressIndexing
        public int intgr;

        public String strng;

    }

    public static class Lst {

        public List<String> strng;

        public List<Integer> intgr;

    }

    public static class LstAsTokens {

        @Lutz.AsTokens( ':' )
        public List<String> strng;

        @Lutz.AsTokens
        public List<Integer> intgr;

    }

    public static class WithSubs {

        public String strng;

        public WithSubs ws1;
        
        public WithSubs ws2;

    }

}
