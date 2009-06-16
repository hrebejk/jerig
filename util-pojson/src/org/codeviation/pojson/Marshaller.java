/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */

package org.codeviation.pojson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.codeviation.commons.patterns.Filter;

/** Good for saving objects in Json format.
 *
 * XXX Hold the builders to make caching working
 *
 * @author Petr Hrebejk
 */
public final class Marshaller<T> {
    
    private Filter<String> fieldFilter;
    private String indentation = "    ";
    private int indentLevel = 0;

    private PojoWriter pw;

    public Marshaller() {
        this.pw = new PojoWriter();
    }

    public Marshaller(String indentation, int indentLevel) {
        this();
        this.indentation = indentation;
        this.indentLevel = indentLevel;        
    }

    /** Saves an object to string */
    public String save( T object ) {

        StringWriter sw = new StringWriter(2048);
        try {
            save(object, sw);
            return sw.toString();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }

    }

    public void save( T object, Writer writer ) throws IOException {
        BufferedWriter bw = new BufferedWriter( writer );
        doWrite(object, writer);
        bw.flush();
        bw.close();
    }

    public void save( T object, OutputStream outputStream ) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(outputStream);
        try {
            save( object, osw);
        }
        finally {
            osw.close();
        }
    }

    public void save( T object, File file ) throws IOException  {

        FileWriter fw = null;

        try {
            fw = new FileWriter(file);
            save( object, fw);
        }
        finally {
            if ( fw != null) {
                fw.close();
            }
        }
    }

    void save( T object, ZipOutputStream zipOutputStream, String entryName) throws IOException {
        Writer w = new BufferedWriter( new OutputStreamWriter(zipOutputStream));
        zipOutputStream.putNextEntry(new ZipEntry(entryName));
        doWrite(object, w);
        w.flush();
    }

    void setFieldFilter(Filter<String> fieldFilter) {
        this.fieldFilter = fieldFilter;
    }
    
    /** Sets the string which will be used as indetation
     * 
     * @param indentation The indentation string.
     */
    public void setIndentation(String indentation) {
        this.indentation = indentation;
    }
    
    /** Sets the initial level of indentation.
     * 
     * @param startIndentLevel Number of indentation string repeats
     */
    public void setIndentLevel(int startIndentLevel) {
        this.indentLevel = startIndentLevel;
    }

    private void doWrite(T object, Writer w) throws IOException {
        PojsonBuilder<Void,IOException> pb = FormatingBuilder.create(w, indentation, indentLevel);
        pw.<Object,Void,IOException>writeTo(object, pb);
    }

}
