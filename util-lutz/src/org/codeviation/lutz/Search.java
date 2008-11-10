/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.lutz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;

/**
 *
 * @author phrebejk
 */
public class Search {

    // XXX Make me an iterator
    public static List<String> fieldValues(IndexReader ir, String field) throws IOException {
        
        TermEnum te = ir.terms( new Term(field, ""));
        List<String> result = new ArrayList<String>();
        
        int count = 0;
        while( te.next() ) {
            Term t = te.term();
            if ( !t.field().equals(field)) {
                break;
            }
            result.add(t.text());
        }
        return result;
    }
    
}
