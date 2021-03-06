/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 Sun Microsystems, Inc. All rights reserved.
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
 * 
 * Contributor(s):
 * 
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */

package org.codeviation.pojson;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import org.codeviation.commons.patterns.Filter;
import org.codeviation.commons.reflect.FieldUtils;

/** Utility methods for endocding/decoding values and other parts of Json format.
 *
 * @author Petr Hrebejk
 */
final class PojsonUtils {

    // Static class
    private PojsonUtils() {}
    
    public static String encode(String value) {
        return JsonUtils.toJsonString(value);
    }
    
    public static String encode(char value) {
        return JsonUtils.toJsonString(value);
    }
    
    public static String encode(boolean value) {
        return JsonUtils.toJsonString(value);
    }
    
    public static String encode(Enum value) {
        return JsonUtils.toJsonString(value);
    }
    
    public static String encode(byte value) {
        return JsonUtils.toJsonString(value);
    }
    
    public static String encode(short value) {
        return JsonUtils.toJsonString(value);
    }
    
    public static String encode(int value) {
        return JsonUtils.toJsonString(value);
    }
    
    public static String encode(long value) {
        return JsonUtils.toJsonString(value);
    }
    
    public static String encode(float value) {
        return JsonUtils.toJsonString(value);
    }
    
    public static String encode(double value) {
        return JsonUtils.toJsonString(value);
    }
    
    public static String encode(Object value) {
        return JsonUtils.toJsonString(value);
    }
    
    
    /** Method that creates a Map -> Name to Filed and honors Pojson annotations
     * 
     * @param clazz
     * @return
     */
    public static Map<String,Field> getFields( Class<?> clazz ) {

        Pojson.StopAt saa = clazz.getAnnotation(Pojson.StopAt.class);

        Class stopClass;

        if ( saa == null ) {
            stopClass = clazz;
        }
        else {
            stopClass = saa.value();
            if ( saa.value() == Pojson.StopAtCurrentClass.class ) {
                stopClass = clazz;
            }
        }

        return FieldUtils.getAll(clazz, stopClass, new FieldFilter(clazz));

    }


    public static String getPojsonFieldName(Field f) {

        String name;

        Pojson.Name na = f.getAnnotation(Pojson.Name.class);

        if (na == null) {
            name = f.getName();
        }
        else {
           name = na.value();
           name = name == null ? f.getName() : name;
        }

        Pojson.NamePrefix np = f.getDeclaringClass().getAnnotation(Pojson.NamePrefix.class);
        if ( np != null ) {
            name = np.value() + name;
        }

        return name;

    }

    /** Gets name of a field honoring the Pojson Annotations */
    private static class FieldFilter implements Filter<Field> {

        private static final int[] DEFAULT_NEGATIVE = new int[] { Modifier.TRANSIENT, Modifier.STATIC };

        private int[] positive;
        private int[] negative;

        public FieldFilter(Class<?> clazz) {
            
            Pojson.ModifierPositive mp = clazz.getAnnotation(Pojson.ModifierPositive.class);
            if ( mp != null ) {
                positive = mp.value();
                if (positive.length == 0) {
                    positive = null;
                }
            }

            Pojson.ModifierNegative mn = clazz.getAnnotation(Pojson.ModifierNegative.class);
            if ( mn != null ) {
                negative = mp.value();
                if (negative.length == 0) {
                    negative = null;
                }
            }
            else {
                negative = DEFAULT_NEGATIVE;
            }

        }

        public boolean accept(Field field) {

            if (field.isSynthetic() ||
                field.isAnnotationPresent(Pojson.SuppressStoring.class) ) {
                return false;
            }

            int m = field.getModifiers();

            if ( positive != null ) {
                for( int p : positive ) {
                    if ( (m & p) != p) {
                        return false;
                    }
                }
            }

            if ( negative != null ) {
                for( int p : negative ) {
                    if ( (m & p) != 0) {
                        return false;
                    }
                }
            }

            return true;
        }

    }


}
