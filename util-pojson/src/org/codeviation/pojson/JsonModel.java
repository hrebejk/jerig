/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.pojson;

import java.lang.reflect.Field;

/**
 *
 * @author phrebejk
 */
public class JsonModel {


    static class MetaModelWriter {

        <T,E extends Exception> void writeTo( Class clazz, PojsonBuilder<T,E> builder ) throws E {

            builder = builder.hash();

            builder = builder.field("javaClass").value(clazz.getName());
            builder = builder.field("fields").hash();
            for( Field f : PojsonUtils.getFields(clazz) ) {

                builder = builder.field("name").value(f.getName());

            }
            builder = builder.up();

            builder = builder.up();

        }

    }


}
