package org.codeviation.commons.patterns;

public interface Closure<PR, PI> {

    boolean processItem(PI param);

    PR getResult();

    public static interface WorkingSet<I, E extends Throwable> {

        public <R> R apply( Closure<R,? super I> processor ) throws E;

    }
}
