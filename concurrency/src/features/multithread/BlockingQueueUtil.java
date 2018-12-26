package features.multithread;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by saurabhagrawal on 26/12/18.
 */
public class BlockingQueueUtil {
    private List queue = new LinkedList();
    private int  limit = 10;

    public BlockingQueueUtil(int limit){
        this.limit = limit;
    }
    public synchronized void enqueue(Object item) throws InterruptedException  {
        while(this.queue.size() == this.limit) {    //if limit maximum, block enqueu
            wait();
        }
        if(this.queue.size() == 0) {
            notifyAll();
        }
        this.queue.add(item);
    }


    public synchronized Object dequeue() throws InterruptedException{
        while(this.queue.size() == 0){
            wait();
        }
        if(this.queue.size() == this.limit){
            notifyAll();
        }
        return this.queue.remove(0);
    }

}