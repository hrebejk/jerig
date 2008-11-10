/*
 * FieldRecords.java
 * 
 * Created on Jul 27, 2007, 6:47:43 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.lutz;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.DateTools;
import static org.apache.lucene.document.Field.Store;
import static org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.NumberTools;
import org.codeviation.commons.reflect.FieldUtils;
import org.codeviation.lutz.Lutz.NamePrefix;


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
    
    private Analyzer analyzer;
    private Field field;
    private Collection<FieldRecord> subs;
    private String name;
    private Store store;
    private Index index;
    
    public String getName() {
        return name;
    }
    
    public Class getType() {
        return field.getType();
    }
    
    public boolean hasSubs() {
        return subs != null;
    }
    
    public Collection<FieldRecord> getSubs() {
        return subs;
    }
    
    public Analyzer getAnalyzer() {
        return analyzer;
    }
    
    public Store getStore() {
        return store;
    }
    
    public Index getIndex() {
        return index;
    }
        
    public Collection<Object> get(Object o) {
        try {
            if ( field.getType().isArray() ) {
                Object array = field.get(o);
                if ( array == null ) {
                    return null;
                }
                int len = Array.getLength(array);
                List<Object> result = new ArrayList<Object>(len);

                for( int i = 0; i <len; i++) {
                    result.add(Array.get(array, i) );
                }
                return result;
            }
            else {            
                return Collections.singletonList(field.get(o));
            }
        }
        catch (IllegalArgumentException ex) {
            Logger.getLogger(FieldRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            Logger.getLogger(FieldRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;        
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
            if ( f.isSynthetic() ) {
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
                        
            if (isDirectlyIndexed(type) ){
                fr.store = getFieldStore(fr.field);
                fr.analyzer = getFieldAnalyzer(fr.field);
                fr.index = getFieldIndex(fr.field);
                //System.out.println("Creating FR " + fr.name + " : " + fr.getType() + " " + fr.store + " " + fr.index + " " + fr.analyzer  );
            }    
            else {
                // System.out.println("NOT DIRECTLY INDEXED " + fr.name );
                fr.subs = forClass( type, fr.name );
            }
            result.add(fr);
        }

        return result;
    }
    
    private static Store getFieldStore( Field field ) {
        return field.isAnnotationPresent(Lutz.Store.class) ? Store.YES: Store.NO;
    }
    
    private static String getNamePrefix(Class<?> clazz) {
        Annotation np = clazz.getAnnotation(Lutz.NamePrefix.class);
        if ( np != null ) {
            return ((NamePrefix)np).value();
        }
        else {
            return null;
        }
    }
    
    private static boolean isUntokenized(Field field) {
        return field.isAnnotationPresent(Lutz.Untokenized.class);
    }
    
    private static Index getFieldIndex( Field field ) {
        Class type = field.getType();
        
        
        if ( isUntokenized( field ) ||
             type.isEnum() ||
             type == Integer.TYPE || type == Integer.class ||
             type == Long.TYPE || type == Long.class ||
             type == Short.TYPE || type == Short.class || 
             type == Byte.TYPE || type == Byte.class ||
             type == Boolean.TYPE || type == Boolean.class || 
             type == Character.TYPE || type == Character.class || 
             type == Date.class ) {
             return Index.UN_TOKENIZED;
        }
        else {
            return Index.TOKENIZED;
        }
    }
    
    private static Analyzer getFieldAnalyzer(Field field) {
        
        Class type = field.getType();
        
        if ( isUntokenized(field) ||
             type.isEnum() ||
             type == Integer.TYPE || type == Integer.class ||
             type == Long.TYPE || type == Long.class ||
             type == Short.TYPE || type == Short.class || 
             type == Byte.TYPE || type == Byte.class ||
             type == Boolean.TYPE || type == Boolean.class || 
             type == Character.TYPE || type == Character.class || 
             type == Date.class ) {
             return new KeywordAnalyzer();
        }
        else {
            return null;
        }
    }
    
    // XXX ugly this method has to be in sync with asText()   
    private static boolean isDirectlyIndexed(Class type) {
        
        if ( type == Integer.TYPE || type == Integer.class ||
             type == Long.TYPE || type == Long.class ||
             type == Short.TYPE || type == Short.class || 
             type == Byte.TYPE || type == Byte.class ||
             type == Boolean.TYPE || type == Boolean.class || 
             type == Character.TYPE || type == Character.class || 
             type == Date.class ||
             type == String.class ||
             type.isEnum() ) {
            return true;
        }
        else {
            return false;
        }
    }
    
    // XXX ugly this method has to be in sync with isDirectlyIndexed()
    String asText(Object o) {
        
        if ( o == null ) {
            return "null";
        }
        
        Class type = getType(); 
        if ( type.isArray() ) {
            type = type.getComponentType();
        }
        
        if ( type == Integer.TYPE || type == Integer.class ||
             type == Long.TYPE || type == Long.class ||
             type == Short.TYPE || type == Short.class || 
             type == Byte.TYPE || type == Byte.class ) {
            return NumberTools.longToString(Long.valueOf(((Number)o).longValue())); 
        } 
        else if ( type == Boolean.TYPE || type == Boolean.class ) {
            return ((Boolean)o).booleanValue() ? "true" : "false";
        }
        else if ( type == Boolean.TYPE || type == Boolean.class ) {
            return ((Character)o).toString() ;
        }
        else if ( type == Date.class ) {
            return DateTools.dateToString((Date)o, DateTools.Resolution.MILLISECOND); //XXX
        }
        else if ( type.isEnum() || type == String.class ) {
            return o.toString();
        }
        else {
            throw new IllegalStateException( "Can't index " + type + "  " + o + "!");
        }
    }
        
}
