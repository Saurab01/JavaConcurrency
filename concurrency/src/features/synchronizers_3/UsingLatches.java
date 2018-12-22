package features.synchronizers_3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Latches are used to delay the progress of threads until it reach a terminal state
 *
 * Most common implementation is CountDownLatch.
 * 
 * In CountDownLatch, each event adds 1. When ready, countDown() is called,
 * decrementing by counter by 1. await() method releases when counter is 0.
 * 
 * Single use synchronizer.
 */
public class UsingLatches {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newCachedThreadPool();
		CountDownLatch latch = new CountDownLatch(6);

        Runnable r = () -> {
			try {
				Thread.sleep(1000);
				System.out.println("Service in " + Thread.currentThread().getName() + " initialized.");
                latch.countDown();
                System.out.println("::print state::"+ latch.getCount());
            } catch (InterruptedException e) {
				e.printStackTrace();
			}
		};

        for (int i=0;i<6;i++){ executor.execute(r);}
        //latch.await();
        latch.await(4, TimeUnit.SECONDS); //Calling await() blocks the thread until the count reaches zero.
		System.out.println("All services up and running!");

		executor.shutdown();
	}

}
