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

package org.codeviation.commons.reflect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codeviation.commons.patterns.Factory;
import org.codeviation.commons.patterns.Pair;
import org.codeviation.commons.patterns.Pairs;

public class ObjectNameFactory<T> implements Factory<String, T> {

    private String defaultExtension;

    private Map<Class<?>, Pair<String,List<Field>>> classInfos = new HashMap<Class<?>, Pair<String, List<Field>>>();

    public ObjectNameFactory() {
    }

    public ObjectNameFactory(String defaultExtension) {
        this.defaultExtension = defaultExtension;
    }


    public String create(T object) {

        Pair<String,List<Field>> ci = getClassInfo(object.getClass());

        String nameFormat = ci.getFirst();
        List<Field> idParts = ci.getSecond();

        Object[] ids = new Object[idParts.size()];
        for (int i = 0; i < idParts.size(); i++) {
            try {
                Field field = idParts.get(i);
                field.setAccessible(true);
                ids[i] = field.get(object);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ObjectNameFactory.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ObjectNameFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        StringBuilder sb = new StringBuilder();
        
        if ( nameFormat == null ) {
            for (Object id : ids) {
                sb.append(id == null ?  "null" : id.toString());
            }
            if ( defaultExtension != null ) {
                sb.append(defaultExtension);
            }
        }
        else {
            Formatter f = new Formatter();
            f.format(nameFormat, ids);
            sb.append(f.toString());    
        }
        return sb.toString();
    }

    // Annotations -------------------------------------------------------------

    /** Tells the NameFactory how the filename of the record should be formated
     * the format string is the same as the one of printf method. If used on
     * field it means that the field will be stored in different file. See also
     * IdPart annotation. If the Id part not used the first field is taken.
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD })
    public @interface NameFormat {
        String value() default "";
    }

    /** Tells the NameFactory that given field is part of the ID.
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD )
    public @interface IdPart {
        int value() default -1;
    }


    // Private methods ---------------------------------------------------------

    private synchronized Pair<String,List<Field>> getClassInfo(Class<?> clazz) {

        Pair<String,List<Field>> ci = classInfos.get(clazz);

        if ( ci == null ) {

            NameFormat fnfA = clazz.getAnnotation(NameFormat.class);
            String nameFormat = fnfA == null ? null : fnfA.value();
            List<Field> fields = getIdParts(FieldUtils.getAll(clazz, null, null).values());

            if ( fields == null ) { // No Id(s) specified
                if ( nameFormat == null ) {
                    nameFormat = clazz.getSimpleName() + ( defaultExtension == null ? "" : defaultExtension );
                }
                fields = Collections.<Field>emptyList();
            }

            ci = Pairs.pair(nameFormat, fields);
            classInfos.put(clazz, ci);
        }

        return ci;
    }


    private static List<Field> getIdParts(Collection<Field> fields) {
        
        SortedMap<Integer,Field> numberedIds= new TreeMap<Integer, Field>();
        List<Field> anonIds = new ArrayList<Field>();

//        Field first = null;

        for (Field f : fields) {

//            if (first == null) {
//                first = f;
//            }

            IdPart idAn = f.getAnnotation(IdPart.class);
            if (idAn != null ) {
                int value = idAn.value();
                if ( value == -1 ) {
                    anonIds.add(f);                        
                }
                else {
                    numberedIds.put(value, f);
                }
            }

        }

        int idsSize = numberedIds.size() + anonIds.size();

        if ( idsSize == 0 ) {
            return null;
        }
        else {
            List<Field> idFields = new ArrayList<Field>(idsSize);
            idFields.addAll(numberedIds.values());
            idFields.addAll(anonIds);
            return idFields;
        }
         
    }

}
