package features.multithread.threadpool;

import features.multithread.BlockingQueueUtil;

/**
 * Created by saurabhagrawal on 26/12/18.
 */
public class PoolThread extends Thread {

    private BlockingQueueUtil taskQueue = null;
    private boolean isStopped = false;

    public PoolThread(BlockingQueueUtil queue){
        taskQueue = queue;
    }

    public void run(){
        while(!isStopped()){
            try{
                Runnable runnable = (Runnable) taskQueue.dequeue();
                runnable.run();
            } catch(Exception e){
                //log or otherwise report exception,
                //but keep pool thread alive.
            }
        }
    }

    public synchronized void doStop(){
        isStopped = true;
        this.interrupt(); //break pool thread out of dequeue() call.
    }

    public synchronized boolean isStopped(){
        return isStopped;
    }
}
