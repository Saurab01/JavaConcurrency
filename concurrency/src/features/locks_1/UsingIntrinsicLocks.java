package features.locks_1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsingIntrinsicLocks {
	private boolean state;

    // Everything in this method can only be accessed by the thread who hold the lock
    // Without sync: states have no order guarantee true, true, false, true...
    // With sync: always true, false, true, false...
    public synchronized void mySynchronizedMethod() {
        System.out.println("\nStarted For threadName: "+Thread.currentThread().getName());
        state = !state;
		System.out.println("state is:" + state);
	}

    //It's possible to lock only a block inside the method
	public void mySynchronizedBlock() {
		/*
		 * Everything in this block can only be accessed by the thread who hold the
		 * lock. The message bellow will be printed before the message inside the
		 * synchronized block
		 */
		System.out.println("Who owns my lock: " + Thread.currentThread().getName());
		synchronized (this) {
			state = !state;
			System.out.println("Who owns my lock after state changes: " + Thread.currentThread().getName());
			System.out.println("State is: " + state);
			System.out.println("====");
		}
	}

	/**
	 * Already holds a lock when called
	 */
	public synchronized void reentrancy() {
        System.out.println("\nStarted For threadName: "+Thread.currentThread().getName());
        System.out.println("Before acquiring again");
		// Tries to hold it without releasing the lock
		synchronized (this) {
			System.out.println("I'm own it! " + Thread.currentThread().getName());
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newCachedThreadPool();
		UsingIntrinsicLocks uil = new UsingIntrinsicLocks();

        System.out.println("****************************\nTesting Synchronised method\n");
		for (int i = 0; i < 10; i++) {
			executor.execute(() -> uil.mySynchronizedMethod());
		}
		Thread.sleep(1000);
        System.out.println("\n****************************\nTesting Synchronised Block\n");
		for (int i = 0; i < 10; i++) {
			executor.execute(() -> uil.mySynchronizedBlock());
		}
		Thread.sleep(1000);
        System.out.println("\n****************************\nReentrant lock test\n");
		for (int i = 0; i < 10; i++) {
			executor.execute(() -> uil.reentrancy());
		}
		executor.shutdown();
	}

}
