package patterns.fork_join_1.util;

/**
 * Created by saurabhagrawal on 23/12/18.
 */
import java.util.concurrent.ForkJoinPool;

public class PoolUtil {
    public static ForkJoinPool forkJoinPool = new ForkJoinPool(2);
}
