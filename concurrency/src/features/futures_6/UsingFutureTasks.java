package features.futures_6;

import features.TaskFactorialCallable;

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

	public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2); //max 2 at a time
        List<Future<Integer>> resultList = new ArrayList<>();
        Random random = new Random();

        System.out.println("setting callables");
        for (int i=0; i<4; i++)
        {
            Integer number = random.nextInt(10);
            TaskFactorialCallable calculator  = new TaskFactorialCallable(number);
            Future<Integer> result = executor.submit(calculator);   //submit method
            resultList.add(result);
        }
        System.out.println("\n*********getting future objects results****\n");

        for(Future<Integer> future : resultList)
        {
            System.out.println("*Future result is - "+ future.get() + "; And Task done is "
                        + future.isDone()+"; And if cancelled-"+future.isCancelled());
        }
        System.out.println("\n*********shut down the executor service now****\n");
        executor.shutdown();
    }
}
