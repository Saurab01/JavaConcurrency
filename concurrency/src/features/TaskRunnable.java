package features;

import java.util.Date;

/**
 * Created by saurabhagrawal on 01/05/18.
 */
public class TaskRunnable implements Runnable {
    private String name;

    @Override
    public void run() {
        try {
            System.out.println("Doing a task during : " + name + " - Time - " + new Date());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TaskRunnable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}