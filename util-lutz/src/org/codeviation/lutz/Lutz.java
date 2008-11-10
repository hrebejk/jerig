/*
 * Lutz.java
 * 
 * Created on Aug 8, 2007, 8:59:18 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.lutz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.codeviation.commons.patterns.Factory;

/** Contains annotations for the Lutz indexing engine.
 *
 * @author Petr Hrebejk
 */
public final class Lutz {

    public static final Factory<Analyzer,Class<?>> ANALYZER_FACTORY = new AnalyzerFactory();
    
    /** Creates a factory which will procuce lucene documents from objects
     *  of given class.
     * @param clazz 
     * @return
     */
    public static final <T> Factory<Document,T> documentFactory(Class<T> clazz) {
        return new DocumentFactory<T>(clazz);
    }
    
    /**
     * Adds fields in given class to given analyzer. Convenience method for
     * more complicated indexes.
     * @param clazz
     * @return
     */
    public static void addToAnalyzer(PerFieldAnalyzerWrapper analyzer, Class... classes) {
        
        for( Class<?> clazz : classes ) {
            IndexInfo ii = IndexInfo.forClass(clazz);
            ii.addToAnalyzer(analyzer);            
        }
        
    }
    
    /**
     * Merges src document into dest document
     * @param dest The Documento to merge to.
     * @param src The Document to be merged
     */ 
    public static void mergeDocuments(Document dest, Document src) {
        
        for( Object f : src.getFields() ) {
            dest.add((Field)f);
        }
        
    }
    
    
    /** Tells Lutz that given field should be not only indexed but also
     * stored in the Lucene index. Notice that not stored fields are not 
     * available in the Lucene document. Therefore make sure that at least
     * ID of the document is stored.
     *
     * @author Petr Hrebejk
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Store {
        // Itentionaly empty
    }
            
    /** Tells Lutz that given field should not be tokenized and that the
     * KeywordAnalyzer shoul be used
     *
     * @author Petr Hrebejk
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Untokenized {
        // Itentionaly empty
    }
            
    /** Tells Lutz that given field should not be indexed.
     *
     * @author Petr Hrebejk
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface SuppressIndexing {
        // Intentionally empty
    }
    
    /** Tells Lutz that all fields in the class should have its name prefixed
     * with the given prefix.
     *
     * @author Petr Hrebejk
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface NamePrefix {
        String value();
    }
       
    /** Tells Lutz which analyzer to use for given field.
     *
     * @author Petr Hrebejk
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface LuceneAnalyzer {
        Class<? extends Analyzer> value();
    }
        
}
