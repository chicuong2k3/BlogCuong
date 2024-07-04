import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CallableDemo {
    public static void main(String[] args) {
        Callable<String> callableTask = () -> {
            System.out.println("Task is executing ...");
            TimeUnit.SECONDS.sleep(3);
            return "Hello!";
        };
        
        try {
            ExecutorService executor = Executors.newFixedThreadPool(5);
            Future<String> future = executor.submit(callableTask);

            System.out.println("Task execution finished: " + future.isDone());

            //wait for the task to finished
            String message = future.get();

            System.out.println("Task execution finished: " + future.isDone());
            System.out.print("Message: " + message);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
