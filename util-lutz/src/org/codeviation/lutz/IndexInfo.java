/*
 * IndexInfo.java
 *
 * Created on Aug 10, 2007, 5:34:10 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.lutz;

import java.util.Collection;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

/** Info about Lucene index created by reflectively inspecting a class
 *
 * @author Petr Hrebejk
 */
class IndexInfo<T> {
    
    private Collection<FieldRecord> fields;
    private PerFieldAnalyzerWrapper analyzer;

    private IndexInfo(Class<T> clazz) {
        this.fields = FieldRecord.forClass(clazz, null);
        this.analyzer = new PerFieldAnalyzerWrapper( new StandardAnalyzer());
        addToAnalyzer(analyzer, fields);
    }

    public static <T> IndexInfo<T> forClass(Class<T> clazz) {
        return new IndexInfo<T>(clazz);
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    // Package private section--------------------------------------------------
    
    Collection<FieldRecord> getFields() {
        return fields;
    }
    
    void addToAnalyzer(PerFieldAnalyzerWrapper analyzer) {
        addToAnalyzer(analyzer, fields);
    }
        
    // Private section ---------------------------------------------------------
    
    private static void addToAnalyzer(PerFieldAnalyzerWrapper analyzer, Collection<FieldRecord> fields) {

        for (FieldRecord fieldRecord : fields) {
            if (fieldRecord.hasSubs()) {
                addToAnalyzer(analyzer, fieldRecord.getSubs());
            } else {
                Analyzer a = fieldRecord.getAnalyzer();
                if (a != null) {
                    analyzer.addAnalyzer(fieldRecord.getName(), a);
                }
            }
        }
    }
}
