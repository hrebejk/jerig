/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.patterns;

/** Applies processor to all items until the processor return false from
 *  the processItem method.
 *
 * @author phrebejk
 */
public interface Closure<I, E extends Throwable> {

    public <R> R apply( Processor<R,? super I> processor ) throws E;

    public static interface Processor<PR, PI> {

        boolean processItem(PI param);

        PR getResult();

    }

}
