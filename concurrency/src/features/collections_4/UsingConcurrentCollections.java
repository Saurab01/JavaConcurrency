package features.collections_4;

import features.CommonUtils;

import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Concurrent collections are an alternative to the Synchronized Collections.
 * 
 * Supports concurrent access from n threads and performs better than Synchronized collections.
 *
 */
public class UsingConcurrentCollections {

    ConcurrentHashMap<Integer,String> conHashMap=new ConcurrentHashMap<>();
    static UsingConcurrentCollections classObj=new UsingConcurrentCollections();
    /**
	 * Map for Concurrent access. Sacrifices some aspects of synchronization to
	 * achieve better performance.
	 *
	 * Locks it's values with Lock Striping.
	 * 
	 * Lock Striping divides the protected region through several locks.
	 * 
	 * - Don't throw ConcurrentModificationException
	 * 
	 * - size() and isEmpty() can be incorrect. Don't rely on them.
	 * 
	 * - Supports atomic operations, don't need client side locking.
	 * 
	 * - Readers can access concurrently, and iterator have weak consistency.
	 * 
	 * - Just a few Writers can modify it.
	 */
    public static void usingConcurrentHashMap() {
        ExecutorService executor = Executors.newCachedThreadPool();
        System.out.println("=== ConcurrentHashMap ===");
        executor.execute(classObj.new WriteThreasOne());
        executor.execute(classObj.new WriteThreasTwo());
        executor.execute(classObj.new ReadThread());
        executor.shutdownNow();

        CommonUtils.awaitTermination(executor);
    }

	/**
	 * Replacement for synchronized list. Based on the immutable object concept.
	 * 
	 * Use when reading is far more common than writing.
	 * 
	 * Creates a new copy every time that the list is modified, only synchronizing
	 * briefly to ensure array content visibility.
	 * 
	 * iterator returns a snapshot of the current state of the collection.
	 * 
	 * Supports atomic operations.
	 * 
	 */
	public static void usingCopyOnWriteArrayList() {
		ExecutorService executor = Executors.newCachedThreadPool();
		System.out.println("=== CopyOnWriteArrayList ===");
		Random random = new Random();
		// No ConcurrentModificationException
		CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<Integer>();

		for (int i = 0; i < 100; i++) {
			if (i % 8 == 0) {
				// write
				executor.execute(() -> {
					Integer value = random.nextInt(10);
					System.err.println("Added " + value);
					copyOnWriteArrayList.add(value);
				});
			} else {
				// read
				executor.execute(() -> {
					StringBuilder sb = new StringBuilder();
					for (Integer vv : copyOnWriteArrayList) {
						sb.append(vv + " ");
					}
					System.out.println("Reading " + sb.toString());
				});
			}
		}

		// Finishing
		executor.shutdown();
        CommonUtils.awaitTermination(executor);
	}

	/**
	 * Concurrent Queue interface.
	 * 
	 * Implementations: LinkedBlockingQueue, ArrayBlockingQueue,
	 * PriorityBlockingQueue, SynchronizedQueue.
	 * 
	 * Used for the Producer-Consumer pattern.
	 * 
	 * Blocking methods: put/take; Timed blocking methods: offer, poll;
	 * 
	 * Can be bounded or unbounded.
	 * 
	 */
	public static void usingBlockingQueue() {
		System.out.println("=== BlockingQueue ===");

		// Bounded UUID queue
		BlockingQueue<UUID> uuidQueue = new LinkedBlockingQueue<>(10);

		System.out.println("Queue will execute for 10s");

		// Multiple consumers
		Runnable runConsumer = () -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					UUID uuid = uuidQueue.take();
					System.out.println("Consumed: " + uuid + " by " + Thread.currentThread().getName());

				} catch (InterruptedException e) {
					// interrupted pattern
					System.err.println("Consumer Finished");
				}
			}
		};
		Thread consumer1 = new Thread(runConsumer);
		consumer1.start();
		Thread consumer2 = new Thread(runConsumer);
		consumer2.start();

		// Producer Thread
		Runnable runProducer = () -> {
			try {
				while (!Thread.currentThread().isInterrupted()) {
					Random r = new Random();
					// Delay producer
					Thread.sleep(r.nextInt(1000));
					UUID randomUUID = UUID.randomUUID();
					System.out.println("Produced: " + randomUUID + " by " + Thread.currentThread().getName());
					uuidQueue.put(randomUUID);
				}
			} catch (InterruptedException e) {
				// interrupted pattern
				System.err.println("Producer Finished");
			}
		};

		// Multiple producers - Examples using simple threads this time. 
		Thread producer1 = new Thread(runProducer);
		producer1.start();
		Thread producer2 = new Thread(runProducer);
		producer2.start();
		Thread producer3 = new Thread(runProducer);
		producer3.start();

		try {
			// Queue will run for 10secs
			Thread.sleep(10000);
			producer1.interrupt();
			producer2.interrupt();
			producer3.interrupt();
			consumer1.interrupt();
			consumer2.interrupt();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



    class WriteThreasOne implements Runnable {
        @Override
        public void run() {
            for(int i= 1; i<=1000; i++) {
                System.err.println("For Thread: "+Thread.currentThread().getName());
                System.err.println(i+"  A"+ i);
                conHashMap.putIfAbsent(i, "A"+ i);
            }
        }
    }
    class WriteThreasTwo implements Runnable {
        @Override
        public void run() {
            for(int i= 1; i<=500; i++) {
                System.out.println("For Thread: "+Thread.currentThread().getName());
                System.out.println(i+"  B"+ i);
                conHashMap.put(i, "B"+ i);  //if present will replace
            }
        }
    }
    class ReadThread implements Runnable {
        @Override
        public void run() {
            Iterator<Integer> ite = conHashMap.keySet().iterator();
            while(ite.hasNext()){
                Integer key = ite.next();
                System.out.println(key+" : " + conHashMap.get(key));
            }
        }
    }
    public static void main(String[] args) {
        usingConcurrentHashMap();
        usingCopyOnWriteArrayList();
        usingBlockingQueue();
    }

}
