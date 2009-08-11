/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.patterns;

import java.util.Collection;
import org.codeviation.commons.patterns.Closure.Processor;

/**
 *
 * @author phrebejk
 */
public class Closures {

    private Closures() {}

    /** XXX make me to accept iterable as parameter
     */
    public static <I> Closure<I,RuntimeException> collectionClosure(Collection<I> collection) {
        return new CollectionClosure(collection);
    }

    private static class CollectionClosure<I> implements Closure<I, RuntimeException> {

        Collection<I> collection;

        private CollectionClosure( Collection<I> collection ) {
            this.collection = collection;
        }

        @Override
        public <R> R apply(Processor<R, ? super I> processor) {

            for (I item : collection) {
                if ( !processor.processItem(item) ) {
                    break;
                }
            }
            return processor.getResult();
        }


    }
}
