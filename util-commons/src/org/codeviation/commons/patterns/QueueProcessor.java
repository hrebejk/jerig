/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.commons.patterns;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Maintains a queue and periodically tries to empty it
 *
 * @author Petr Hrebejk
 */
public abstract class QueueProcessor<E> implements Runnable, Iterable<E> {

    private volatile int rt;

    private LinkedBlockingQueue<E> q;
    private Semaphore s;
    private String name;
    private int permits;
    private volatile boolean stop;
    private Thread t;

    public QueueProcessor(String name, int permits) {
        this.name = name;
        this.q = new LinkedBlockingQueue<E>();
        this.s = permits == -1 ? null : new Semaphore(permits, true);
        this.permits = permits;
    }

    public synchronized void start() {
        t = new Thread(this, name);
        t.start();
    }

    public synchronized void  stop() {
        stop = true;
        if ( t != null) {
            t.interrupt();
        }
    }

    public void add( E e ) {
        q.add(e);
    }

    public int size() {
        return q.size();
    }

    public int runningTasks() {
        return rt;
    }

    public Iterator<E> iterator() {
        return q.iterator();
    }
    
    /** Returns true when the queue is empty and all the permits were released.
     *
     * @return
     */
    public boolean isDone() {
        return q.isEmpty() && ( !s.hasQueuedThreads() ) ;
    }

    /** This method will be called in order to process the event. Notice that
     * this will happen in separate thread.
     * @param e
     */
    protected abstract void processEvent(E e);

    public void run() {
        while( !stop ) {
            try {
                // Get next available event
                E e = q.take();

                if( s != null ) { // if limited resources
                    s.acquire();  // wait till a resource is available
                }

                // Process the event in new thread
                Processor p = new Processor(e);
                new Thread(p).start();
            } catch (InterruptedException ex) {
                // Logger.getLogger(QueueProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class Processor implements Runnable {

        private E e;

        public Processor(E e) {
            this.e = e;
        }
        
        public void run() {
            rt++;
            processEvent(e);
            if ( s != null) {
                s.release(); // Put back the permit
            }
            rt--;
        }

    }

}
