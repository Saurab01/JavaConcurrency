package features.atomics_2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Atomics can be used from the java.util.concurrent.atomic.* package.
 * 
 * An atomic operation is a compound action that totally completes out totally
 * fails, not supporting inconsistent values or results during it's execution.
 * 
 * The classes in this package supports atomic operations on single variables,
 * having get and set (read and write) methods that behave like a volatile
 * variable.
 * 
 * The compareAndSet are commonly used in non-blocking algorithms. They
 * basically tries to set a new value to a specified field, and it returns a
 * boolean indicating success or not. All atomic, only blocking briefly.
 * 
 * Interesting classes in this package are: AtomicBoolean, AtomicLong,
 * AtomicReference<T>, AtomicMarkableReference<T> and
 * AtomicReferenceFieldUpdater<T, V>.
 * 
 *
 */
public class UsingAtomics {

	/*
	 * A Counter using AtomicInteger
	 */
	static class AtomicCounter {
		private AtomicInteger atomicInteger = new AtomicInteger(0);  //initial value
        public AtomicInteger getAtomicInteger() {return atomicInteger;}

        public void increment() {
			atomicInteger.incrementAndGet();
		}
		public void decrement() {
			atomicInteger.decrementAndGet();
		}
		public int get() {
			return atomicInteger.get();
		}
        public void set(int value)  {atomicInteger.set(value);}  //set value
	}

	public static void main(String[] args) throws InterruptedException {
		AtomicCounter counter = new AtomicCounter();
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
            executorService.execute(() -> counter.increment());
		}
        executorService.awaitTermination(2, TimeUnit.SECONDS);
        System.out.println("Result shound be 10: Actual result is: " + counter.get());

        System.out.println("\nStarted decreasing");
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> counter.decrement());
        }
        executorService.awaitTermination(2, TimeUnit.SECONDS);
        System.out.println("Result shound be 0: Actual result is: " + counter.get());
        executorService.shutdown();

        //Other Atomic Integer functions
        counter.set(30); //setting new value
        System.out.println("\n\nAfter set value: " + counter.get());

        System.out.println("After getAndAdd value: " +counter.getAtomicInteger().getAndAdd(10));
        System.out.println("After addAndGet value: " +counter.getAtomicInteger().addAndGet(10));

        //atomicInteger.compareAndSet(expectedValue, newValue);
        counter.getAtomicInteger().compareAndSet(50,70);
        System.out.println("After compareAndSet value: " + counter.get());

        counter.getAtomicInteger().compareAndSet(50,70);  //no change as expected not 50 here
        System.out.println("again After compareAndSet value: " + counter.get());
    }
}
