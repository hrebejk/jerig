/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.patterns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public static <T> Closure<List<T>,T> listCollector() {
        return new Collector(new ArrayList<T>() );
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

    public static class Collector<C extends Collection<T>, T, E> implements Closure<C, T> {

        private C result;

        public Collector(C result) {
            this.result = result;
        }

        public boolean processItem(T param) {
            result.add(param);
            return true;
        }

        public C getResult() {
            return result;
        }

    }

}
