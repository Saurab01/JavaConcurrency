package features.synchronizers_3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphores controls the number of activities that can access a resource or
 * perform a certain action;
 *
 * - First you give a number of 'permits';
 * - Activities will acquire it and release when they're done;
 * - If none is available, activity will block until one become available.
 *
 * Good for resource pools.
 */
public class UsingSemaphores {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		Semaphore semaphore = new Semaphore(3,true);  //true for fairness--optional (FIFO)

		Runnable r = () -> {
			try {
                semaphore.acquire();
                System.out.println("available permits:: "+ semaphore.availablePermits()+
                        "  Acquired - " + Thread.currentThread().getName());
                Thread.sleep(2000);
                System.out.println("\nDone and Releasing for thread- " + Thread.currentThread().getName());
                semaphore.release();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
        System.out.println("starting threads\n");
		for (int i = 0; i < 6; i++) {
			executor.execute(r);
		}
		executor.shutdown();
	}
}
