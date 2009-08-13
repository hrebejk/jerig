/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.patterns;

import org.codeviation.commons.patterns.Closure.Processor;

/**
 *
 * @author phrebejk
 */
public class Closures {

    private Closures() {}

    /** XXX make me to accept iterable as parameter
     */
    public static <I> Closure<I,RuntimeException> closure(Iterable<I> iterable) {
        return new IterableClosure(iterable);
    }

    private static class IterableClosure<I> implements Closure<I, RuntimeException> {

        private Iterable<I> iterable;

        private IterableClosure( Iterable<I> iterable ) {
            this.iterable = iterable;
        }

        @Override
        public <R> R apply(Processor<R, ? super I> processor) {

            for (I item : iterable) {
                if ( !processor.processItem(item) ) {
                    break;
                }
            }
            return processor.getResult();
        }


    }
}
