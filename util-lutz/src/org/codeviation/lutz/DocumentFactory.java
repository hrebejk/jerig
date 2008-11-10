/*
 * Indexer.java
 * 
 * Created on Jul 27, 2007, 6:25:28 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.lutz;

import java.util.Collection;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.codeviation.commons.patterns.Factory;

/**
 *
 * @author Petr Hrebejk
 */
class DocumentFactory<T> implements Factory<Document,T> {
    
    private IndexInfo<T> indexInfo;
    
    public DocumentFactory(Class<T> clazz) {
        this.indexInfo = IndexInfo.forClass(clazz);
    }
    
    public Document create(T object) {
        Document doc = new Document();

        for (FieldRecord fr : indexInfo.getFields()) {
            Collection<Object> objects = fr.get(object);
            if( objects == null ) {
                index(doc, fr, null);
            }
            else {
                for( Object o : objects ) {
                    index(doc, fr, o );
                }
            }
        }
        return doc;                
    }
            
    // Private section ---------------------------------------------------------
    
    private void index(Document doc, FieldRecord fr, Object object) {
                        
        if ( object == null ) {
            return;
        }
        
        // Has subs
        if ( object != null && fr.hasSubs() ) {
            for (FieldRecord sub : fr.getSubs()) {
                for( Object o : sub.get(object) ) {
                    index(doc, sub, o);
                }
            }
        }
        else {
            // System.out.println("FR " + fr.getName() + " : " + fr.asText(object));
            if (  fr.getStore() == null ) {
                System.out.println( "NULL STORE" + fr.getName() );
            }
            Field field = new Field(fr.getName(), fr.asText(object), fr.getStore(), fr.getIndex());
            doc.add(field);
        }
    }
    
}


    