/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.strast;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.NumberTools;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.codeviation.commons.utils.FileFilters;
import org.codeviation.commons.utils.FileUtil;
import org.codeviation.lutz.Lutz;
import org.codeviation.lutz.Search;
import org.codeviation.strast.model.Frequency;

/**
 *
 * @author phrebejk
 */
public class IndexQueries {

    private Map<String,IndexReader> idxReaders = new HashMap<String,IndexReader>();

    private File root;

    public IndexQueries( File storageRoot ) {
        this.root = storageRoot;
    }

    public List<String> getIndexes() {

        File[] subFolders = root.listFiles( FileFilters.asFileFilter( FileFilters.IS_DIRECTORY ) );
        List<String> idxFolders = new ArrayList<String>();

        for (File file : subFolders) {
            if ( IndexReader.indexExists(file)) {
                idxFolders.add(file.getName());
            }
        }

        Collections.sort(idxFolders);

        return idxFolders;

    }


    // XXX make me an iterator
    public List<String> getFieldNames(String index) {
        IndexReader ir = getIndexReader(index);
        List<String> fieldNames = new ArrayList<String>();
        for( Object fn : ir.getFieldNames(IndexReader.FieldOption.ALL) ) {
            if ( fn instanceof String ) {
                fieldNames.add((String)fn);
            }
        }
        return fieldNames;
    }
    
    // XXX make me an iterator
    public List<String> getFieldValues(String index, String field, Lutz.FieldConversion conversion) throws IOException {

        conversion = conversion == null ? Lutz.FieldConversion.NONE : conversion;

        IndexReader ir = getIndexReader(index);
        List<String> values = new ArrayList<String>();
        TermEnum terms = ir.terms( new Term(field) );
        while (terms.next()) {
            final Term term = terms.term();
            if (!field.equals(term.field())) {
                break;
            }
            values.add(conversion.decode(term.text()));

        }

        return values;
    }

    // XXX make me an iterator
    public List<Frequency> getFieldValueFrequencies(String index, String field, Lutz.FieldConversion conversion) throws IOException {

        conversion = conversion == null ? Lutz.FieldConversion.NONE : conversion;

        IndexReader ir = getIndexReader(index);
        List<Frequency> values = new ArrayList<Frequency>();
        TermEnum terms = ir.terms( new Term(field));
        //while (terms.next()) {
        do {
            final Term term =  terms.term();

            if ( !field.equals( term.field() ) ) {
                break;
            }

            Frequency f = new Frequency();
            f.value = conversion.decode(term.text());
            f.frequency = terms.docFreq();

            values.add(f);
        }
        while (terms.next());
        return values;
    }





    public List<Document> fieldQuery(String index, String field, String value, Search.FieldQueryType kind ) throws IOException {
        
        IndexReader ir = getIndexReader(index);        
        return Search.fieldQuery(ir, field, value, kind );
        
    }

    public List<Document> textQuery(String index, String query) throws org.apache.lucene.queryParser.ParseException, IOException {

        IndexReader ir = getIndexReader(index);
        IndexSearcher is = new IndexSearcher(ir);
        MultiFieldQueryParser qp = new MultiFieldQueryParser(getFieldNames(index).toArray(new String[]{}), new StandardAnalyzer());
        Query q = qp.parse(query);

        Collector hc = new Collector();
        is.search( q, hc );

        List<Document> documents = new ArrayList<Document>( hc.getDocumentNumbers().size());
        for (Integer dn : hc.getDocumentNumbers()) {
            documents.add(is.doc(dn));
        }

        return documents;

    }

    private synchronized IndexReader getIndexReader(Class<?> clazz) {
        String indexName = IndexingUtils.getIndexName(clazz);

        if ( indexName == null ) {
            return null;
        }

        return getIndexReader(indexName);
    }

    private synchronized IndexReader getIndexReader(String indexName) {

        IndexReader ir = idxReaders.get(indexName);

        if ( ir == null ) {
            ir = createIndexReader(indexName);
            idxReaders.put(indexName, ir);
        }

        return ir;
    }

    private IndexReader createIndexReader(String indexName) {
        try {
            File f = FileUtil.file(root, indexName);
            IndexReader ir = IndexReader.open(f);
            return ir;
        } catch (CorruptIndexException ex) {
            throw new IllegalStateException(ex);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static class Collector extends  HitCollector {

        List<Integer> docNums = new ArrayList<Integer>();

        @Override
        public void collect(int docNr, float score) {
            docNums.add(docNr);
        }

        public List<Integer> getDocumentNumbers() {
            return docNums;
        }

    }

}
