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
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumberTools;
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

    /** Converts document to map
     */
    public static Map<String,Object> toMap(Document doc) {

        Map<String,Object> r = new HashMap<String,Object>();

        Set<String> fieldNames = getFieldNames(doc);

        for (String name : fieldNames) {
            String values[] = doc.getValues(name);
            if ( values != null && values.length > 0 ) {
                r.put( name, values.length == 1 ? values[0] : values);
            }
        }

        return r;
    }

    public static Set<String> getFieldNames( Document doc ) {
        Set<String> r = new HashSet<String>();

        for( Object of : doc.getFields() ) {
            r.add( ((Field)of).name() );
        }

        return r;
    }

    
    /** Tells Lutz that given field should be not only indexed but also
     * stored in the Lucene index. Notice that not stored fields are not 
     * available in the Lucene document. Therefore make sure that at least
     * ID of the document is stored.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Store {
        FieldStore value() default FieldStore.YES;
    }
            
    /** Tells Lutz whether and how to index this fiels.
     * KeywordAnalyzer shoul be used
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Index {
        FieldIndex value() default FieldIndex.ANALYZED;
        Class<? extends Analyzer> analyzer() default StandardAnalyzer.class;
    }
            
    /** Tells Lutz to ignore this field
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface SuppressIndexing {
        // Intentionally empty
    }
    
    /** Tells Lutz that all fields in the class should have its name prefixed
     * with the given prefix.
     */
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.TYPE)
//    public @interface NamePrefix {
//        String value();
//    }

    /** If used on array ir Iterable tells Lutz that all values of the array
     * should be put into one field as tokens. Parameter will be used as
     * separator betweeen the tokens, defauld vaalue is space.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface AsTokens {
        char value( ) default ' ';
    }
       
    /** Tells Lutz which analyzer to use for given field.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface LuceneAnalyzer {
        Class<? extends Analyzer> value();
    }

    public static enum FieldStore {
        YES,
        NO,
        COMPRESS;

        public Field.Store toLucene() {

            switch( this ) {
                case YES:
                    return Field.Store.YES;
                case COMPRESS:
                    return Field.Store.COMPRESS;
                default:
                    return Field.Store.NO;

            }

        }
    }

    public static enum FieldIndex {

        ANALYZED,
        ANALYZED_NO_NORMS,
        NO,
        NOT_ANALYZED,
        NOT_ANALYZED_NO_NORMS;

        public Field.Index toLucene() {

            switch( this ) {
                case ANALYZED:
                    return Field.Index.ANALYZED;
                case ANALYZED_NO_NORMS:
                    return Field.Index.ANALYZED_NO_NORMS;
                case NOT_ANALYZED:
                    return Field.Index.NOT_ANALYZED;
                case NOT_ANALYZED_NO_NORMS:
                    return Field.Index.NOT_ANALYZED_NO_NORMS;
                default:
                    return Field.Index.NO;
            }

        }
    }

    public static enum FieldConversion {

        NONE,
        LONG,
        DATE,
        DATE_YEAR,
        DATE_MONTH,
        DATE_DAY,
        DATE_HOUR,
        DATE_MINUTE,
        DATE_SECOND,
        DATE_MILISECOND;

        public DateTools.Resolution getResolution() {
            
            switch (this) {
                
                case DATE_YEAR:
                    return DateTools.Resolution.YEAR;                    
                case DATE_MONTH:
                    return DateTools.Resolution.MONTH;
                case DATE_DAY:
                    return DateTools.Resolution.DAY;
                case DATE_HOUR:
                    return DateTools.Resolution.HOUR;
                case DATE_MINUTE:
                    return DateTools.Resolution.MINUTE;
                case DATE_SECOND:
                    return DateTools.Resolution.SECOND;                                
                default:
                    return DateTools.Resolution.MILLISECOND;                                    
            }
            
            
        }

        public String encode(String text)  {
            switch( this ) {
                case NONE:
                    return text;
                case LONG:
                    return NumberTools.longToString( Long.parseLong(text) );
                default:
                    return DateTools.timeToString( Long.parseLong(text), getResolution() );
            }
        }

        public String decode(String text) {

            switch( this ) {
                case NONE:
                    return text;
                case LONG:
                    return Long.toString( NumberTools.stringToLong(text) );
                default:
                    try {
                        return Long.toString(DateTools.stringToTime(text));
                    }
                    catch (ParseException ex) {
                        return text;
                    }
            }

        }

    }

}
