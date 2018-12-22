package features.atomics_2;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by saurabhagrawal on 23/12/18.
 */
//http://tutorials.jenkov.com/java-util-concurrent/atomicreference.html
public class OtherAtomicClasses {
    public static void main(String[] args) {
        atomicBoolean();
        atomicLong();
        atomicReference();

    }

    private static void atomicReference() {
        System.out.println("\n*************************AtomicReference******************");
        AtomicReference atomicReference = new AtomicReference();

    }

    private static void atomicBoolean() {
        System.out.println("\n*************************AtomicBoolean******************");
        AtomicBoolean atomicBool=new AtomicBoolean();   //by default false (can be passed true/false with cons)
        System.out.println("After initialising:::"+atomicBool.get());
        atomicBool.set(false);
        System.out.println("After getAndSet:::"+ atomicBool.getAndSet(true));
        System.out.println("After compareAndSet:::"+ atomicBool.compareAndSet(true,false));
    }
    private static void atomicLong() {
        System.out.println("\n*************************AtomicLong******************");
        AtomicLong atomicLong=new AtomicLong(210);
        atomicLong.set(220);
        System.out.println("After compareAndSet:::"+ atomicLong.compareAndSet(220,225));
        System.out.println("After getAndSet:::"+ atomicLong.getAndSet(10));
        System.out.println("After getAndAdd:::"+ atomicLong.getAndAdd(100));
        System.out.println("After addAndGet:::"+ atomicLong.addAndGet(10));
        System.out.println("After decrementAndGet:::"+ atomicLong.decrementAndGet()); //subtract 1
        System.out.println("After getAndDecrement:::"+ atomicLong.getAndDecrement());
    }


}
