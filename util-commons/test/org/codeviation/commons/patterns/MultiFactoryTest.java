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

package org.codeviation.commons.patterns;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phrebejk
 */
public class MultiFactoryTest {
    
    @Test
    public void multi() {
        System.out.println("multi");
        
        initMulti(new Multi());
        
        fail("Not done yet");
    } 
    
    private void initMulti(Object o) {
        
    }
    
    private static Object getImplemetation(Object o) {
        return Proxy.newProxyInstance(o.getClass().getClassLoader(), new Class[] {Factory.class}, new MethodHandler() );
    }
    
    private static class MethodHandler implements InvocationHandler {

        Object original;
        Method method;
        
        public Object invoke(Object proxy, Method im, Object[] args) throws Throwable {            
            return method.invoke(original, args);
        }
        
    }
    
    private static class Multi {
        
        public Factory<String,Integer> INTEGER_2_STRING;
        
        public Factory<Integer,String> STRING_2_INTEGER;
                
        @FactoryMethod("INTEGER_2_STRING")
        String method1(Integer i) {
            return i.toString();
        }
                
        @FactoryMethod("STRING_2_INTEGER")
        int method2(String text) {
            return Integer.parseInt(text);
        }
        
    }
    
    private @interface FactoryMethod {
        String value();
    }
}
