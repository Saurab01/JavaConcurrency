package com.saurabh.personal.java.concurrency.features.executors;

import com.saurabh.personal.java.concurrency.features.TaskRunnable;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Thread creation is expensive and difficult to manage.
 * Executors help us to decouple task submission from execution.
 * We have 4 types of executors:
 * - Single Thread Executor: Uses a single worker to process tasks.
 * - Cached Thread Pool: Unbounded thread limit, good performance for long running tasks.
 * - Fixed Thread Pool: Bounded thread limit, maintains the same thread pool size.
 * - Scheduled Thread Pool: Bounded thread limit, used for delayed tasks.
 * 
 * And 2 types of tasks:
 * - execute: Executes without giving feedback. Fire-and-forget.
 * - submit: Returns a FutureTask.
 * 
 * ThreadPools: Used by the executors described above. ThreadPoolExecutor can be
 * used to create custom Executors.
 * 
 * shutdown() -> Waits for tasks to terminate and release resources.
 * shutdownNow() -> Try to stops all executing tasks and returns a list of not
 * executed tasks.
 *
 */
public class UsingExecutors {

	public static void usingSingleThreadExecutor() {
		System.out.println("=== SingleThreadExecutor ===");
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		singleThreadExecutor.execute(() -> System.out.println("Print this."));
		singleThreadExecutor.execute(() -> System.out.println("and this one to."));
		singleThreadExecutor.shutdown();
		try {
			singleThreadExecutor.awaitTermination(4, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("\n\n");
	}

	public static void usingCachedThreadPool() {
		System.out.println("=== CachedThreadPool ===");
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		List<Future<UUID>> uuids = new LinkedList<>();
		for (int i = 0; i < 10; i++) {
			Future<UUID> submitted = cachedThreadPool.submit(() -> {
				UUID randomUUID = UUID.randomUUID();
				System.out.println("UUID " + randomUUID + " from " + Thread.currentThread().getName());
				return randomUUID;
			});
			uuids.add(submitted);
		}
		cachedThreadPool.execute(() -> uuids.forEach((f) -> {
			try {
				System.out.println("Result " + f.get() + " from thread " + Thread.currentThread().getName());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}));
		cachedThreadPool.shutdown();
		try {
			cachedThreadPool.awaitTermination(4, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("\n\n");

	}

	public static void usingFixedThreadPool() {
		System.out.println("=== FixedThreadPool ===");
		ExecutorService fixedPool = Executors.newFixedThreadPool(4);
        /*At any point, at most {@code nThreads} threads will be active processing tasks.
     * If additional tasks are submitted when all threads are active, they will wait in the queue until a thread is
     * available. If any thread terminates due to a failure during execution prior to shutdown, a new one will
     * take its place if needed to execute subsequent tasks.  */

        List<Future<UUID>> uuids = new LinkedList<>();

        for (int i = 0; i < 20; i++) {
			Future<UUID> submitted = fixedPool.submit(() -> {
				UUID randomUUID = UUID.randomUUID();
				System.out.println("UUID " + randomUUID + " from " + Thread.currentThread().getName());
				return randomUUID;
			});
			uuids.add(submitted);
		}
        /*for (int i = 0; i < 10; i++)
        {
            Task task = new Task("Task " + i);
            System.out.println("A new task has been added : " + task.getName());
            executor.execute(task);
        }  */

        fixedPool.execute(() -> uuids.forEach((f) -> {
			try {
				System.out.println("Result " + f.get() + " from " + Thread.currentThread().getName());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}));
		fixedPool.shutdown();
		try {
			fixedPool.awaitTermination(4, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("\n\n");
	}

	public static void usingScheduledThreadPool() {
		System.out.println("=== ScheduledThreadPool ===");
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(4);
		scheduledThreadPool.scheduleAtFixedRate(
				() -> System.out.println("Print every 2s"), 0, 2, TimeUnit.SECONDS);
		scheduledThreadPool.scheduleWithFixedDelay(
				() -> System.out.println("Print every 2s delay"), 0, 2, TimeUnit.SECONDS);

        TaskRunnable task1 = new TaskRunnable ("Demo Task 1");
        TaskRunnable task2 = new TaskRunnable ("Demo Task 2");

        System.out.println("The time is : " + new Date());
        scheduledThreadPool.schedule(task1, 5 , TimeUnit.SECONDS);
        scheduledThreadPool.schedule(task2, 10 , TimeUnit.SECONDS);

		try {
			scheduledThreadPool.awaitTermination(6, TimeUnit.SECONDS);  /*Blocks until all tasks have completed
			execution after a shutdown * request, or the timeout occurs, or the current thread is
            * interrupted, whichever happens first.*/

			scheduledThreadPool.shutdown();  /*This method does not wait for previously submitted tasks to
     * complete execution. */
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//usingSingleThreadExecutor();
		//usingCachedThreadPool();
		usingFixedThreadPool();
		//usingScheduledThreadPool();
	}
}
