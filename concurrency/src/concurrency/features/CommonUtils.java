package concurrency.features;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by saurabhagrawal on 01/05/18.
 */
public class CommonUtils {

    public static void awaitTermination(ExecutorService executor){
        try {
            executor.awaitTermination(2000, TimeUnit.SECONDS);
            /*Blocks until all tasks have completed execution after a shutdown request, or the timeout occurs,
            or the current thread is interrupted, whichever happens first. */
            Thread.sleep(2000);
            System.out.println("\n\n\n\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
