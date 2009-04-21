/*
 * FieldRecords.java
 * 
 * Created on Jul 27, 2007, 6:47:43 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.lutz;

import com.sun.org.apache.bcel.internal.generic.ASTORE;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.NumberTools;
import org.codeviation.commons.patterns.Factories;
import org.codeviation.commons.reflect.ClassUtils;
import org.codeviation.commons.reflect.FieldUtils;
import org.codeviation.lutz.Lutz.FieldIndex;


/** Indexes plain objects in lucene using reflection. Default (if not changed
 * using annotations) indexing is as follows.
 * 
 * <li>null : "null"
 * <li>boolean, java.lang.Boolean : "true", "false" 
 * <li>char, java.lan.Character : "c"
 * <li>byte, java.lang.Byte, short, java.lang.Short, int, java.lang.Integer, 
 *     long java.lang.Long : using Number tolls
 * <li>float, java.lang.Float, double, java.lang.Double, java.lang.Void : cannot index
 * 
 *
 * @author Petr Hrebejk
 */
class FieldRecord  {
    
    private Field field;
    private Collection<FieldRecord> subs;
    private String name;
    
    public String getName() {
        return name;
    }
    
    public boolean hasSubs() {
        return subs != null;
    }
    
    public Collection<FieldRecord> getSubs() {
        return subs;
    }
    
    public Analyzer getAnalyzer() {
        return getFieldAnalyzer(field);
    }
        
    /** Adds given field to a document */
    public void index( Document doc, Object object ) {
                
        if ( object == null ) {
            return;
        }

        if ( LutzUtils.isArrayType(field.getType()) ) {
            if ( isAsTokens(field) ) { // Convert array to text
                char separator = field.getAnnotation(Lutz.AsTokens.class).value();
                StringBuilder sb = new StringBuilder();
                for( Iterator it = LutzUtils.getIterator(getFieldValue( object )); it.hasNext(); ) {
                    if ( sb.length() != 0) {
                        sb.append(separator);
                    }
                    sb.append( LutzUtils.valueAsText( it.next() ) );                    
                    
                }
                org.apache.lucene.document.Field f = new org.apache.lucene.document.Field (
                        getName(),
                        LutzUtils.valueAsText( sb.toString() ),
                        getFieldStore(field).toLucene(),
                        getFieldIndex(field).toLucene());                
                doc.add(f);
            }
            else { // Add several fields for every element;
                for( Iterator it = LutzUtils.getIterator(getFieldValue( object )); it.hasNext(); ) {
                    org.apache.lucene.document.Field f = new org.apache.lucene.document.Field (
                        getName(),
                        LutzUtils.valueAsText( it.next() ),
                        getFieldStore(field).toLucene(),
                        getFieldIndex(field).toLucene());

                    doc.add(f);
                }
            }
        }
        else {
            org.apache.lucene.document.Field f = new org.apache.lucene.document.Field (
                    getName(),
                    LutzUtils.valueAsText( getFieldValue(object) ),
                    getFieldStore(field).toLucene(),
                    getFieldIndex(field).toLucene());
            doc.add(f);
        }
        
        
        // Has subs
//        if ( fr.hasSubs() ) {
//            for (FieldRecord sub : fr.getSubs()) {
//                for( Object o : sub.get(object) ) {
//                    index(doc, sub, o);
//                }
//            }
//        }
//        else {
//            // System.out.println("FR " + fr.getName() + " : " + fr.asText(object));
//            if (  fr.getStore() == null ) {
//                System.out.println( "NULL STORE" + fr.getName() );
//            }
//            Field field = new Field(fr.getName(), fr.asText(object), fr.getStore(), fr.getIndex());
//            doc.add(field);
//        }
        
               
    }
   
    static Collection<FieldRecord> forClass(Class clazz, String parentName ) {
        
        Collection<Field> fields;
        fields = FieldUtils.getDeclared(clazz, FieldUtils.modifierFilterNegative(Modifier.STATIC));
        Collection<FieldRecord> result = new ArrayList<FieldRecord>(fields.size());
        
        String namePrefix = getNamePrefix(clazz);
        if ( namePrefix == null ) {
            namePrefix = "";
        }
        
        // System.out.println( clazz.getName() + " " + namePrefix + ";");
        
        for (Field f : fields) {
            if ( f.isSynthetic() || f.isAnnotationPresent(Lutz.SuppressIndexing.class) ) {
                continue; // Rewrite to filter
            }
            f.setAccessible(true);
            
            Class type = f.getType();
            if ( type.isArray() ) {
                type = type.getComponentType();
            }
            FieldRecord fr = new FieldRecord();
            fr.field = f;
            fr.name = parentName == null ? namePrefix + f.getName() : parentName + "." + namePrefix + f.getName();
                                    
//            if ( !primitive || String) {
//
//                // System.out.println("NOT DIRECTLY INDEXED " + fr.name );
//                fr.subs = forClass( type, fr.name );
//            }
            result.add(fr);
        }

        return result;
    }
    
    private static Lutz.FieldStore getFieldStore( Field field ) {
        Lutz.Store s = field.getAnnotation(Lutz.Store.class);
        return s == null ? Lutz.FieldStore.NO : s.value();
    }
    
    private static String getNamePrefix(Class<?> clazz) {
        // XXX Add prefix into Lutz and Pojson
//        Annotation np = clazz.getAnnotation(Lutz.NamePrefix.class);
//        if ( np != null ) {
//            return ((Lutz.NamePrefix)np).value();
//        }
//        else {
//            return null;
//        }

       return null;
    }
        
    private static Lutz.FieldIndex getFieldIndex( Field field ) {

        if ( LutzUtils.isLutzPrimitive(field.getType())) {  // Primitive types
            return Lutz.FieldIndex.NOT_ANALYZED;
        }
        
        Lutz.Index i = field.getAnnotation(Lutz.Index.class);
        return i == null ? Lutz.FieldIndex.ANALYZED : i.value();
    }
    
    private static Analyzer getFieldAnalyzer(Field field) {
                
        FieldIndex fieldIndex = getFieldIndex(field);

        Lutz.Index i = field.getAnnotation(Lutz.Index.class);

        switch( fieldIndex ) {
            case NO:
            case NOT_ANALYZED:
            case NOT_ANALYZED_NO_NORMS:
                return null;
            default: // ANALYZED, ANALYZED_NO_NORMS

                Class<?> type = getFieldTypeForIndexing(field);

                if ( LutzUtils.isLutzPrimitive(type) ) { // Primitive types
                    return new KeywordAnalyzer();
                }
                else if ( i != null ) {
                    return Factories.NEW_INSTANCE.create( i.analyzer() );
                }
                else {
                    return new StandardAnalyzer();
                }
        }

    }

    private Object getFieldValue(Object o) {
        field.setAccessible(true);
        try {
            return field.get(o);
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex);
        }
        catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static Class<?> getFieldTypeForIndexing(Field field ) {

        Class<?> type = field.getType();

        if ( LutzUtils.isArrayType(type)) {
            type = LutzUtils.resolveComponentType(field);
            if ( type == null ) {
                throw new IllegalArgumentException("Don't know how to index " + field );
            }
        }

        return type;
    }

    
    private boolean isAsTokens( Field f ) {
        return f.isAnnotationPresent(Lutz.AsTokens.class);
    }
    
}
