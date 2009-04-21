/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.strast;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.LockObtainFailedException;
import org.codeviation.commons.patterns.Factory;
import org.codeviation.commons.utils.FileUtil;
import org.codeviation.lutz.Lutz;

/**
 *
 * @author Petr Hrebejk
 */
public class IndexingStorage implements Storage {

    public static final String PATH_ID = "strast.path";
    public static final char FILE_SEPARATOR = '/';

    private Storage dataStorage;
    private File root;

    private IndexQueries queries;
    private Map<String,IndexWriter> idxWriters = new HashMap<String,IndexWriter>();
    private Map<Class<?>, Factory<Document,Object>> docFactories= new HashMap<Class<?>, Factory<Document,Object>>();

    public IndexingStorage(Storage dataStorage, File indexesRoot) throws IOException  {
        this.dataStorage = dataStorage == null ? new NullStorage() : dataStorage;
        this.root = indexesRoot;

        FileUtil.assureDirectory(indexesRoot);
    }

    public <T> T get(Class<T> clazz, String... path) {
        return dataStorage.get(clazz, path);
    }

    public boolean put(Object o, String... path) {
        index(o, path);
        return dataStorage.put(o, path);
    }

    public boolean delete(String... path) {
        // XXX delete from index
        return dataStorage.delete(path);
    }

    public void close() {
        for (IndexWriter indexWriter : idxWriters.values()) {
            try {
                indexWriter.commit();
                indexWriter.optimize();
                indexWriter.close();
            }
            catch (CorruptIndexException ex) {
                throw new IllegalStateException(ex);
            }
            catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    public Store getStore() {
        return dataStorage.getStore();
    }

    public synchronized IndexQueries getQueries() {

        if ( queries == null ) {
            queries = new IndexQueries(root);
        }

        return queries;
    }


    private void index(Object o, String... path) {

        IndexWriter iw = getIndexWriter(o.getClass());

        if ( iw != null) {
            try {
                final Document document = getDocumentFactory(o.getClass()).create(o);
                document.add(new Field(
                                    PATH_ID,
                                    FileUtil.path( FILE_SEPARATOR, path),
                                    org.apache.lucene.document.Field.Store.YES,
                                    org.apache.lucene.document.Field.Index.NO ));
                iw.addDocument(document);
            }
            catch (CorruptIndexException ex) {
                throw new IllegalStateException(ex);
            }
            catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    private synchronized IndexWriter getIndexWriter(Class<?> clazz) {

        String indexName = IndexingUtils.getIndexName(clazz);

        if ( indexName == null ) {
            return null;
        }

        IndexWriter iw = idxWriters.get(indexName);

        if ( iw == null ) {
            iw = createIndexWriter(indexName, Lutz.ANALYZER_FACTORY.create(clazz));
            idxWriters.put(indexName, iw);
        }

        return iw;

    }

    private IndexWriter createIndexWriter(String indexName, Analyzer analyzer) {
        try {
            File f = FileUtil.file(root, indexName);
            IndexWriter iw = new IndexWriter(f, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
            return iw;
        } catch (CorruptIndexException ex) {
            throw new IllegalStateException(ex);
        } catch (LockObtainFailedException ex) {
            throw new IllegalStateException(ex);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized Factory<Document,Object> getDocumentFactory(Class<? extends Object> clazz) {

        Factory<Document,Object> df = docFactories.get(clazz);

        if ( df == null) {
            df = (Factory<Document, Object>) Lutz.documentFactory(clazz);
            docFactories.put(clazz, df);
        }

        return df;

    }

}
