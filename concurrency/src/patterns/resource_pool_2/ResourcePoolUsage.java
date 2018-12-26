package patterns.resource_pool_2;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Pattern: Resource Pool
 * 
 * Example: Resource Pool usage
 */
public class ResourcePoolUsage {

	public static void main(String[] args) {

		ExecutorService executor = Executors.newCachedThreadPool();
		ResourcePool<Integer> pool = new ResourcePool<>(5,
				Arrays.asList(10, 11, 12, 13, 14));

        Random r = new Random();
		for (int i = 0; i < 10; i++) {

            executor.execute(() -> {
				try {
					Integer value = pool.get(60);
					System.out.println("Head taken: " + value);
					Thread.sleep(r.nextInt(5000));
					pool.release(value);
					System.out.println("Value released " + value);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
		executor.shutdown();
	}
}
