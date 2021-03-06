package features;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by saurabhagrawal on 01/05/18.
 */
public class TaskFactorialCallable implements Callable<Integer> {

    private Integer number;

    public TaskFactorialCallable(Integer number) {
        this.number = number;
    }

    @Override
    public Integer call() throws Exception {

        int result = 1;
        if ((number == 0) || (number == 1)) {
            result = 1;
        } else {
            for (int i = 2; i <= number; i++) {
                result *= i;
                TimeUnit.SECONDS.sleep(1);
            }
        }
        System.out.println("Result for number - " + number + " -> " + result);
        return result;
    }
}