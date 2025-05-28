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

package org.codeviation.pojson.records;

import java.lang.annotation.RetentionPolicy;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Petr Hrebejk
 */
public class RecordObjectTypes {

    public Boolean fBoolean;
    public Character fCharacter;
    public Byte fByte;
    public Short fShort;
    public Integer fInteger;
    public Long fLong;
    public Float fFloat;
    public Double fDouble;
    public String fString;
    public RetentionPolicy fEnum; 
    public URL fUrl;
    public URI fUri;
    
    public RecordObjectTypes init() {
        fBoolean = Boolean.TRUE;
        fCharacter = new Character('c');
        fByte = new Byte((byte) 1);
        fShort = new Short((short) 2);
        fInteger = new Integer(3);
        fLong = new Long(4);
        fFloat = new Float(5.5f);
        fDouble = new Double(6.6d);
        fString = "string";
        fEnum = RetentionPolicy.SOURCE;
        try {
            fUrl = new URL("http://www.codeviation.org");
            fUri = new URI("http://www.codeviation.org");
        }
        catch (MalformedURLException ex) {
            throw new IllegalStateException(ex);
        }
        catch (URISyntaxException ex) {
            throw new IllegalStateException(ex);
        }
        return this;
    }
    
}
