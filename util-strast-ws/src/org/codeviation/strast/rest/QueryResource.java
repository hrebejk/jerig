/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.strast.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.codeviation.lutz.Lutz;
import org.codeviation.lutz.Search;
import org.codeviation.strast.IndexQueries;
import org.codeviation.strast.IndexingStorage;
import org.codeviation.strast.model.Frequency;

/**
 *
 * @author phrebejk
 */
public class QueryResource {

    IndexingStorage storage;

    public QueryResource(IndexingStorage storage) {
        this.storage = storage;
    }

    @GET
    @Path("indexes")
    public List<String> getStorageIndexes() {
        return storage.getQueries().getIndexes();
    }

    @GET
    @Path("fields/{index}")
    public List<String> getIndexFields(@PathParam("index") String index) {
        IndexQueries iq = storage.getQueries();
        return iq.getFieldNames(index);
    }

    
    @GET
    @Path("lucene/{index}")
    public List<Map<String,Object>> getTermValues(@PathParam("index") String index,
                                      @QueryParam("query") String query) throws ParseException, IOException {
        
        IndexQueries iq = storage.getQueries();

        List<Document> docs = iq.textQuery(index, query);
        List<Map<String,Object>> r = new ArrayList<Map<String,Object>>(docs.size());
        for( Document d : docs ) {
            r.add( Lutz.toMap(d) );
        }

        return r;

    }


    @GET
    @Path("field/values/{index}/{field}")
    public List<String> getTermValues(@PathParam("index") String index,
                                      @PathParam("field") String field,
                                      @QueryParam("convert") Lutz.FieldConversion conversion ) throws IOException {
        IndexQueries iq = storage.getQueries();
        return iq.getFieldValues(index, field, conversion);

    }

    @GET
    @Path("field/frequencies/{index}/{field}")
    public List<Frequency> getTermFrequecies(@PathParam("index") String index,
                                      @PathParam("field") String field,
                                      @QueryParam("convert") Lutz.FieldConversion conversion ) throws IOException {
        IndexQueries iq = storage.getQueries();
        return iq.getFieldValueFrequencies(index, field, conversion);

    }

    @GET
    @Path("field/pattern/{index}/{field}")
    public List<Map<String,Object>> getPatternQuery(@PathParam("index") String index,
                                 @PathParam("field") String field,
                                 @QueryParam("value") String value,
                                 @QueryParam("kind") Search.FieldQueryType kind ) throws IOException {

        IndexQueries iq = storage.getQueries();

        iq.fieldQuery(index, field, value, kind);

        List<Document> docs = iq.fieldQuery(index, field, value, kind);
        List<Map<String,Object>> r = new ArrayList<Map<String,Object>>(docs.size());

        for( Document d : docs ) {
            r.add( Lutz.toMap(d) );
        }

        return r;
    }


}
