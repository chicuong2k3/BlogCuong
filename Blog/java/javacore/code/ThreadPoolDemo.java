import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class WorkerThread implements Runnable {  
    public WorkerThread() {  
    }  
    
    public void run() {  
        try {  
            System.out.println(Thread.currentThread().getName() + " Start"); 
            Thread.sleep(2000);  
            System.out.println(Thread.currentThread().getName() + " End");
        } catch (InterruptedException e) { 
            System.out.println(e.getMessage());
        } 
    }  
}  

public class ThreadPoolDemo {
    public static void main(String[] args) {  
        // creating a pool of 5 threads  
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {  
            Runnable worker = new WorkerThread();  
            // calling execute method of ExecutorService  
            executor.execute(worker);

            // or
            // executor.submit(() -> { 
            //     String threadName = Thread.currentThread().getName(); 		
            //     System.out.println("Hello " + threadName); 
            // });
        
        }  

        // the thread pool has to be ended explicitly
        executor.shutdown();  
        //executor.awaitTermination(5, TimeUnit.SECONDS);

        while (!executor.isTerminated()) {

        }
  
        System.out.println("Finished all threads");  
    }  
}
