class DaemonThread extends Thread {
    public DaemonThread(String threadName) {
        this.setName(threadName);
    }

    public void run() {
        if (Thread.currentThread().isDaemon()) {
            System.out.println(Thread.currentThread().getName() + " is daemon thread.");
        } else {
            System.out.println(Thread.currentThread().getName() + " is user thread.");
        }
    }
}

public class DaemonThreadDemo {
    public static void main(String[] args) {
		DaemonThread t1 = new DaemonThread("Thread 1");
        DaemonThread t2 = new DaemonThread("Thread 2");
        DaemonThread t3 = new DaemonThread("Thread 3");

        t1.setDaemon(true);

        t1.start();
        t2.start();
        t3.start();
    }
}
