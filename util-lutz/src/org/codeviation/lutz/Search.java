/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.lutz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.document.FieldSelectorResult;
import org.apache.lucene.document.SetBasedFieldSelector;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;

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

//    public List<Documents> textQuery(IndexReader ir, String defaultField, String query ) {
//
//        IndexSearcher is = new IndexSearcher(ir);
//        QueryParser qp = new QueryParser()
//        is.
//
//
//    }



    public static enum FieldQueryType {
        EXACT,
        EXACT_CASE_INSENSITIVE,
        PREFIX,
        PREFIX_CASE_INSENSITIVE,
        CAMEL_CASE,
        CAMEL_CASE_CASE_INSENSITIVE,
        REGEXP,
        REGEXP_CASE_INSENSITIVE;
    }


    // called under LuceneIndexManager.readAccess
    public static List<Document> fieldQuery (
            final IndexReader in,
            final String fieldName,
            final String value,
            final FieldQueryType kind,
            final String... fieldsToLoad
    ) throws IOException {

        final List<Document> result = new LinkedList<Document>();
        final Set<Term> toSearch = new TreeSet<Term> (new TermComparator());

        switch (kind) {
            case EXACT:
                {
                    toSearch.add(new Term (fieldName,value));
                    break;
                }
            case EXACT_CASE_INSENSITIVE:
                {
                    toSearch.add(new Term (fieldName,value.toLowerCase()));
                    break;
                }
            case PREFIX:
                if (value.length() == 0) {
                    //Special case (all) handle in different way
                    emptyPrefixSearch(in, fieldsToLoad, result);
                    return result;
                }
                else {
                    final Term nameTerm = new Term (fieldName, value);
                    prefixSearch(nameTerm, in, toSearch);
                    break;
                }
            case PREFIX_CASE_INSENSITIVE:
                if (value.length() == 0) {
                    //Special case (all) handle in different way
                    emptyPrefixSearch(in, fieldsToLoad, result);
                    return result;
                }
                else {
                    final Term nameTerm = new Term (fieldName,value.toLowerCase());     //XXX: I18N, Locale
                    prefixSearch(nameTerm, in, toSearch);
                    break;
                }
            case CAMEL_CASE:
                if (value.length() == 0) {
                    throw new IllegalArgumentException ();
                }
                {
                    StringBuilder sb = new StringBuilder();
                    String prefix = null;
                    int lastIndex = 0;
                    int index;
                    do {
                        index = findNextUpper(value, lastIndex + 1);
                        String token = value.substring(lastIndex, index == -1 ? value.length(): index);
                        if ( lastIndex == 0 ) {
                            prefix = token;
                        }
                        sb.append(token);
                        sb.append( index != -1 ?  "[\\p{javaLowerCase}\\p{Digit}_\\$]*" : ".*"); // NOI18N
                        lastIndex = index;
                    }
                    while(index != -1);

                    final Pattern pattern = Pattern.compile(sb.toString());
                    regExpSearch(pattern, new Term (fieldName,prefix),in,toSearch);
                }
                break;
            case REGEXP_CASE_INSENSITIVE:
                if (value.length() == 0) {
                    throw new IllegalArgumentException ();
                }
                else {
                    final Pattern pattern = Pattern.compile(value,Pattern.CASE_INSENSITIVE);
                    if (Character.isJavaIdentifierStart(value.charAt(0))) {
                        regExpSearch(pattern, new Term (fieldName, value.toLowerCase()), in, toSearch);      //XXX: Locale
                    }
                    else {
                        regExpSearch(pattern, new Term (fieldName,""), in, toSearch);      //NOI18N
                    }
                    break;
                }
            case REGEXP:
                if (value.length() == 0) {
                    throw new IllegalArgumentException ();
                } else {
                    final Pattern pattern = Pattern.compile(value);
                    if (Character.isJavaIdentifierStart(value.charAt(0))) {
                        regExpSearch(pattern, new Term (fieldName, value), in, toSearch);
                    }
                    else {
                        regExpSearch(pattern, new Term(fieldName,""), in, toSearch);             //NOI18N
                    }
                    break;
                }
            case CAMEL_CASE_CASE_INSENSITIVE:
                if (value.length() == 0) {
                    //Special case (all) handle in different way
                    emptyPrefixSearch(in, fieldsToLoad, result);
                    return result;
                }
                else {
                    final Term nameTerm = new Term(fieldName,value.toLowerCase());     //XXX: I18N, Locale
                    prefixSearch(nameTerm, in, toSearch);
                    StringBuilder sb = new StringBuilder();
                    String prefix = null;
                    int lastIndex = 0;
                    int index;
                    do {
                        index = findNextUpper(value, lastIndex + 1);
                        String token = value.substring(lastIndex, index == -1 ? value.length(): index);
                        if ( lastIndex == 0 ) {
                            prefix = token;
                        }
                        sb.append(token);
                        sb.append( index != -1 ?  "[\\p{javaLowerCase}\\p{Digit}_\\$]*" : ".*"); // NOI18N
                        lastIndex = index;
                    }
                    while(index != -1);
                    final Pattern pattern = Pattern.compile(sb.toString());
                    regExpSearch(pattern,new Term (fieldName, prefix),in,toSearch);
                    break;
                }
            default:
                throw new UnsupportedOperationException (kind.toString());
        }
        TermDocs tds = in.termDocs();
        final Iterator<Term> it = toSearch.iterator();
        Set<Integer> docNums = new TreeSet<Integer>();
        int[] docs = new int[25];
        int[] freq = new int [25];
        int len;
        while (it.hasNext()) {
            tds.seek(it.next());
            while ((len = tds.read(docs, freq))>0) {
                for (int i = 0; i < len; i++) {
                    docNums.add (docs[i]);
                }
                if (len < docs.length) {
                    break;
                }
            }
        }
        final FieldSelector selector = selector(fieldsToLoad);
        for (Integer docNum : docNums) {
            final Document doc = in.document(docNum, selector);
            result.add(doc);
        }
        return result;
    }


    private static void emptyPrefixSearch (final IndexReader in, final String[] fieldsToLoad, final List<? super Document> result) throws IOException {
        final int bound = in.maxDoc();
        for (int i=0; i<bound; i++) {
            if (!in.isDeleted(i)) {
                final Document doc = in.document(i, selector(fieldsToLoad));
                if (doc != null) {
                    result.add(doc);
                }
            }
        }
    }

    private static void prefixSearch (final Term valueTerm, final IndexReader in, final Set<? super Term> toSearch) throws IOException {
        final Object prefixField = valueTerm.field(); // It's Object only to silence the stupid hint
        final String name = valueTerm.text();
        final TermEnum en = in.terms(valueTerm);
        try {
            do {
                Term term = en.term();
                if (term != null && prefixField == term.field() && term.text().startsWith(name)) {
                    toSearch.add (term);
                }
                else {
                    break;
                }
            } while (en.next());
        } finally {
            en.close();
        }
    }

    private static void regExpSearch (final Pattern pattern, Term startTerm, final IndexReader in, final Set< ? super Term> toSearch) throws IOException {
        final String startText = startTerm.text();
        String startPrefix;
        if (startText.length() > 0) {
            final StringBuilder startBuilder = new StringBuilder ();
            startBuilder.append(startText.charAt(0));
            for (int i=1; i<startText.length(); i++) {
                char c = startText.charAt(i);
                if (!Character.isJavaIdentifierPart(c)) {
                    break;
                }
                startBuilder.append(c);
            }
            startPrefix = startBuilder.toString();
            startTerm = new Term (startTerm.field(),startPrefix);
        }
        else {
            startPrefix=startText;
        }
        final Object camelField = startTerm.field(); // It's Object only to silence the stupid hint
        final TermEnum en = in.terms(startTerm);
        try {
            do {
                Term term = en.term();
                if (term != null && camelField == term.field() && term.text().startsWith(startPrefix)) {
                    final Matcher m = pattern.matcher(term.text());
                    if (m.matches()) {
                        toSearch.add (term);
                    }
                }
                else {
                    break;
                }
            } while (en.next());
        } finally {
            en.close();
        }
    }

    private static int findNextUpper(String text, int offset ) {

        for( int i = offset; i < text.length(); i++ ) {
            if ( Character.isUpperCase(text.charAt(i)) ) {
                return i;
            }
        }
        return -1;
    }

    // when fields == null load all fields
    static FieldSelector selector (String... fieldNames) {
        if (fieldNames != null && fieldNames.length > 0) {
            final Set<String> fields = new HashSet<String>(Arrays.asList(fieldNames));
            final FieldSelector selector = new SetBasedFieldSelector(fields,
                    Collections.<String>emptySet());
            return selector;
        } else {
            return ALL;
        }
    }

    private static final FieldSelector ALL = new FieldSelector() {
        public FieldSelectorResult accept(String arg0) {
            return FieldSelectorResult.LOAD;
        }
    };

    private static class TermComparator implements Comparator<Term> {

        public int compare (Term t1, Term t2) {
            int ret = t1.field().compareTo(t2.field());
            if (ret == 0) {
                ret = t1.text().compareTo(t2.text());
            }
            return ret;
        }
    }
}
