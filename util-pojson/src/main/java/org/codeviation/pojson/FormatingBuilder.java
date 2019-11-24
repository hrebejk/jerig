/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.pojson;

import java.io.IOException;
import java.io.Writer;
import static org.codeviation.pojson.PojsonUtils.*;

/**
 *
 * @author Petr Hrebejk
 */
class FormatingBuilder implements PojsonBuilder<Void,IOException> {

    private final Writer w;
    private final FormatingBuilder parent;
    private final char endingChar;
    private boolean needsComma;
    private boolean isField;
    private final String indent;
    private int level;

    private FormatingBuilder(Writer w, FormatingBuilder parent, char endingChar, String indent, int level) {
        this.w = w;
        this.parent = parent;
        this.endingChar = endingChar;
        this.indent = indent;
        this.level = level;
    }

    public static FormatingBuilder create(Writer w) {
        return new FormatingBuilder(w, null, (char)-1, null, 0);
    }

    public static FormatingBuilder create(Writer w, String indent, int level) {
        return new FormatingBuilder(w, null, (char)-1, indent, level);
    }

    @Override
    public Void build() throws IOException {
        return null;
    }

    @Override
    public PojsonBuilder<Void, IOException> field(String name) throws IOException {
        comma();
        nl();
        indent();
        w.write(encode(name));
        w.write( indent == null ?  ":" : ": ");
        needsComma = false;
        isField = true;
        return this;
    }

    @Override
    public PojsonBuilder<Void, IOException> hash() throws IOException {
        comma();
        if ( !isField && parent != null ) {
            nl();
            //indent();
        }
        indent();
        w.write("{" );
        return new FormatingBuilder(w, this, '}', indent, level + 1);
    }

    @Override
    public PojsonBuilder<Void, IOException> array() throws IOException {
        comma();
        if ( needsComma ) { nl(); }
        indent();
        w.write("[" );        
        return new FormatingBuilder(w, this, ']', indent, level + 1);
    }

    @Override
    public PojsonBuilder<Void, IOException> value() throws IOException {
        writeValue("null");
        return this;
    }

    @Override
    public PojsonBuilder<Void, IOException> value(String value) throws IOException {
        writeValue(encode(value));
        return this;
    }

    @Override
    public PojsonBuilder<Void, IOException> value(boolean value) throws IOException {
        writeValue(encode(value));
        return this;
    }

    @Override
    public PojsonBuilder<Void, IOException> value(long value) throws IOException {
        writeValue(encode(value));
        return this;
    }

    @Override
    public PojsonBuilder<Void, IOException> value(float value) throws IOException {
        writeValue(encode(value));
        return this;
    }

    @Override
    public PojsonBuilder<Void, IOException> value(double value) throws IOException {
        writeValue(encode(value));
        return this;
    }

    @Override
    public PojsonBuilder<Void, IOException> up() throws IOException {
        if ( needsComma ) { 
            nl();
            level--;
            indent();
        }
        w.write( endingChar );        
        parent.needsComma = true;
        parent.isField = false;
        return parent;
    }

    private void writeValue(String value) throws IOException {
        comma();
        if ( !isField ) {
            nl();
        }
        indent();
        needsComma = true;
        w.write(value);
        needsComma = true;
        isField = false;
    }

    private void comma() throws IOException {
        if ( needsComma ) {
            w.write(',');
        }
    }

    private void nl() throws IOException {
        if ( indent != null )  {
            w.write('\n');
        }
    }

    private void indent() throws IOException {

         if ( isField ) {
             return;
         }

         if ( indent == null || level == 0 ) {
             return;
         }

         for( int i = 0; i < level; i++) {
             w.write(indent);
         }
    }

}