/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.pojson.records;

/**
 *
 * @author phrebejk
 */
public class RecordFinalFields {

    public final String text;

    public final int i;

    public RecordFinalFields() {
        text = "AAA";
        i = -100;
    }

    public RecordFinalFields(String text, int i) {
        this.text = text;
        this.i = i;
    }

}
