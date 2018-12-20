package concurrency.features.futures;

import concurrency.features.TaskFactorialCallable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * FutureTask<V> represents an asynchronous computation. It has methods to check
 * if the computation is completed and to cancel it if needed.
 * 
 * Used for computing long running tasks/IO tasks.
 * 
 * Act as a latch and has three states: waiting for run, running or completed.
 * Also, it can be easy canceled with the cancel() method.
 * 
 * if the result is ready, get() will return the value. Otherwise, it'll block.
 *
 */
public class UsingFutureTasks {

	public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        List<Future<Integer>> resultList = new ArrayList<>();
        Random random = new Random();

        for (int i=0; i<4; i++)
        {
            Integer number = random.nextInt(10);
            TaskFactorialCallable calculator  = new TaskFactorialCallable(number);
            Future<Integer> result = executor.submit(calculator);   //submit method
            resultList.add(result);
        }
        for(Future<Integer> future : resultList)
        {
            try
            {
                System.out.println("Future result is - " + " - " + future.get() + "; And Task done is "
                        + future.isDone());
            }
            catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
        }
        //shut down the executor service now
        executor.shutdown();
    }
}
