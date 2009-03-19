/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.pojson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codeviation.pojson.JsonBuilder.Array;
import org.codeviation.pojson.JsonBuilder.Hash;
import org.codeviation.pojson.JsonBuilder.Value;
import org.codeviation.pojson.Parser.Error;

/**
 *
 * @author phrebejk
 */
public class CollectionsBuilder implements JsonBuilder<Object,RuntimeException>,
                                           JsonBuilder.Root<Object,RuntimeException>,
                                           JsonBuilder.Hash<Object,RuntimeException>,
                                           JsonBuilder.Array<Object, RuntimeException> {

    private Map<String,Object> map;
    private List<Object> list;

    private CollectionsBuilder(Map<String, Object> map, List<Object> list) {
        this.map = map;
        this.list = list;
    }

    public static JsonBuilder.Root<Object,RuntimeException> create() {
        return new CollectionsBuilder( null, null );
    }

    public Object build() throws RuntimeException {
        return map == null ? list : map;
    }

    public Hash<Object, RuntimeException> hash() throws RuntimeException {
        return new CollectionsBuilder(new HashMap<String, Object>(), null);
    }

    public Array<Object, RuntimeException> array() throws RuntimeException {
        return new CollectionsBuilder(null, new ArrayList<Object>());
    }

    public Value<Hash<Object, RuntimeException>, Object, RuntimeException> f(String name) throws RuntimeException {
        return new CollectionsValue<Hash<Object, RuntimeException>>(this, name);
    }

    public Value<Array<Object, RuntimeException>, Object, RuntimeException> e() throws RuntimeException {
        return new CollectionsValue<Array<Object, RuntimeException>>(this, null);
    }


    private void add(String fname, Object o) {
            if ( fname == null ) {
                list.add(o);
            }
            else {
                map.put(fname, o);
            }
        }
    
    private class CollectionsValue<R extends JsonBuilder<Object,RuntimeException> > implements Value<R,Object,RuntimeException> {
    
        private R parent;
        private String fname;

        
        private CollectionsValue(R parent, String fname) {
            this.parent = parent;
            this.fname = fname;
        }

        public R v() throws RuntimeException {
            ((CollectionsBuilder)parent).add(fname, null);
            return parent;
        }

        public R v(String value) throws RuntimeException {
            ((CollectionsBuilder)parent).add(fname, value);
            return parent;
        }

        public R v(boolean value) throws RuntimeException {
            ((CollectionsBuilder)parent).add(fname, value);
            return parent;
        }

        public R v(long value) throws RuntimeException {
            ((CollectionsBuilder)parent).add(fname, value);
            return parent;
        }

        public R v(double value) throws RuntimeException {
            ((CollectionsBuilder)parent).add(fname, value);
            return parent;
        }

        public R v(Hash<Object, RuntimeException> builder) throws RuntimeException {
            ((CollectionsBuilder)parent).add(fname, builder.build());
            return parent;
        }

        public R v(Array<Object, RuntimeException> builder) throws RuntimeException {
            ((CollectionsBuilder)parent).add(fname, builder.build());
            return parent;
        }

        public Object build() throws RuntimeException {
            throw new IllegalStateException();
        }

    }


}
