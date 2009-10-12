/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.patterns;

/**
 *
 * @author phrebejk
 */
public class Closures {

    private Closures() {}

    /** XXX make me to accept iterable as parameter
     */
    public static <I> Closure.WorkingSet<I,RuntimeException> workingSet(Iterable<I> iterable) {
        return new IterableWorkingSet(iterable);
    }

    private static class IterableWorkingSet<I> implements Closure.WorkingSet<I, RuntimeException> {

        private Iterable<I> iterable;

        private IterableWorkingSet( Iterable<I> iterable ) {
            this.iterable = iterable;
        }

        @Override
        public <R> R apply(Closure<R, ? super I> processor) {

            for (I item : iterable) {
                if ( !processor.processItem(item) ) {
                    break;
                }
            }
            return processor.getResult();
        }


    }
}
