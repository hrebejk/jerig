/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.pojson.records;

/**
 *
 * @author phrebejk
 */
public class RecordPrivateConstructor {

    private RecordPrivateConstructor() {}

    public String text;

    public static RecordPrivateConstructor create() {
        return new RecordPrivateConstructor();
    }

}
