package features.synchronizers_3;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Barriers are used for blocking a group of threads until they come together at
 * a single point in order to proceed. Basically, convergence of threads.
 * 
 * Accepts a runnable in it's constructor to be called when the threads reach the
 * barrier, but before its unblocked
 * 
 * Most common implementation is cyclic barrier.
 * 
 */
public class UsingBarriers {

	public static void main(String[] args) {

		Runnable barrierAction = () -> System.out.println("\nWell done, guys! ran by thread::"+
                Thread.currentThread().getName()+"\n");
		CyclicBarrier barrier = new CyclicBarrier(10, barrierAction);
        //second param is optional to indicate action to be run by the last thread that trips the barrier
        //important--last thread

		Runnable task = () -> {
			try {
				// simulating a task that can take at most 1sec to run
				Thread.sleep(new Random().nextInt(10) * 100);
                System.out.println("entering barrier " + Thread.currentThread().getName());
                barrier.await();
                System.out.println("*****Crossing barrier " + Thread.currentThread().getName());
            } catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
		};


        ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			executor.execute(task);
		}
        System.out.println("\n::Completed Doing graceful shutdown");
		executor.shutdown();
        System.out.println("\n\n:::Completed");
    }

}
