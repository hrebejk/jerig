/*
 * AnalyzerFactory.java
 * 
 * Created on Sep 25, 2007, 3:34:45 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.lutz;

import org.apache.lucene.analysis.Analyzer;
import org.codeviation.commons.patterns.Factory;

/**
 *
 * @author phrebejk
 */
class AnalyzerFactory implements Factory<Analyzer,Class<?>> {

    public Analyzer create(Class<?> clazz) {
        IndexInfo ii = IndexInfo.forClass(clazz);
        return ii.getAnalyzer();
    }

}
