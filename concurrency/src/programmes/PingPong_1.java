package programmes;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by saurabhagrawal on 26/12/18.
 */
public class PingPong_1 {

    Runnable r1=()->System.out.print("Ping ");
    Runnable r2=()->System.out.print("Pong2 ");

    public static void main(String[] a) throws InterruptedException, ExecutionException {
        PingPong_1 object=new PingPong_1();
        object.withoutExecutor();
        object.withExecutor();
    }
    private void withExecutor() throws ExecutionException, InterruptedException {
        System.out.println("\n\n****Process started with Executor****");

        ExecutorService executor= Executors.newCachedThreadPool();

        for (int i=0;i<10;i++){
            executor.submit(r1).get();   //.get() will make it to block
            executor.submit(r2).get();
        }
        executor.shutdown();
    }

    private void withoutExecutor() throws InterruptedException{

        System.out.println("Process started without Executor");
        for (int i=0;i<10;i++){
            Thread t1=new Thread(r1);   //has to define new object inside
            Thread t2=new Thread(r2);
            t1.start();    //start is important
            t1.join();

            t2.start();
            t2.join();
        }
    }
}
