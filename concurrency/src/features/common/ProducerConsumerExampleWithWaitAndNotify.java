package features.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saurabhagrawal on 30/04/18.
 */

/* Producer Consumer -- 1 each
1) Producer thread produce a new resource in every 1 second and put it in ‘taskQueue’.
2) Consumer thread takes 1 seconds to process consumed resource from ‘taskQueue’.
3) Max capacity of taskQueue is 5 i.e. maximum 5 resources can exist inside ‘taskQueue’ at any given time.
4) Both threads run infinitely.

***In both the methods, we use notify at the end of all statements. The reason is simple, once you have something in list,
you can have the consumer thread consume it, or if you have consumed something, you can have the producer produce something.

 */
public class ProducerConsumerExampleWithWaitAndNotify
{
    public static void main(String[] args)
    {
        List<Integer> taskQueue = new ArrayList<Integer>();
        int MAX_CAPACITY = 5;
        Thread tProducer = new Thread(new Producer(taskQueue, MAX_CAPACITY), "Producer");
        Thread tConsumer = new Thread(new Consumer(taskQueue), "Consumer");
        tProducer.start();
        tConsumer.start();
    }
}

class Producer implements Runnable
{
    private final List<Integer> taskQueue;
    private final int MAX_CAPACITY;

    public Producer(List<Integer> sharedQueue, int size)
    {
        this.taskQueue = sharedQueue;
        this.MAX_CAPACITY = size;
    }

    @Override
    public void run(){
        int counter = 0;
        while (true){
            try {
                produce(counter++);  //producer keeps producing elements at regular interval.
            }
            catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

    private void produce(int i) throws InterruptedException{
        synchronized (taskQueue){
            while (taskQueue.size() == MAX_CAPACITY){
                System.out.println("Queue is full " + Thread.currentThread().getName() +
                        " is waiting , size: " + taskQueue.size());
                taskQueue.wait();
            }

            Thread.sleep(1000);  //1 sec sleep
            taskQueue.add(i);
            System.out.println("Produced: " + i);
            taskQueue.notifyAll();
        }
    }
}


class Consumer implements Runnable
{
    private final List<Integer> taskQueue;

    public Consumer(List<Integer> sharedQueue)
    {
        this.taskQueue = sharedQueue;
    }

    @Override
    public void run()
    {
        while (true){
            try{
                consume();
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

    private void consume() throws InterruptedException
    {
        synchronized (taskQueue)
        {
            while (taskQueue.isEmpty()){
                System.out.println("Queue is empty " + Thread.currentThread().getName() + " is waiting , size: " +
                        taskQueue.size());
                taskQueue.wait();
            }
            Thread.sleep(1000);
            int i = (Integer) taskQueue.remove(0);
            System.out.println("Consumed: " + i);
            taskQueue.notifyAll();
            //Because the last-time wait() method was called by producer thread (that’s why producer is in waiting state),
            // producer gets the notification.
        }
    }
}

