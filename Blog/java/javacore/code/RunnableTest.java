class MyThread implements Runnable {
    String name;
    Thread t;
    MyThread(String threadName) {
        name = threadName;
        t = new Thread(this, name);
        t.start();
    }

    public void run() {
        System.out.println(name + " start.");
		try {
			for(int i = 0; i <= 10; i++) {
				System.out.println(name + ": " + i);
				Thread.sleep(200);
			}
		} catch (InterruptedException e) {
			System.out.println(name + " interrupted.");
		}
		System.out.println(name + " exiting.");
	}
}

public class RunnableTest {
    public static void main(String[] args) {
        MyThread t1 = new MyThread("T1");
        MyThread t2 = new MyThread("T2");
        MyThread t3 = new MyThread("T3");

        System.out.println("Thread 1 is alive: " + t1.t.isAlive());
		System.out.println("Thread 2 is alive: " + t2.t.isAlive());
		System.out.println("Thread 3 is alive: " + t3.t.isAlive());

        try {
			System.out.println("Waiting for threads to finish.");
			t1.t.join();
			t2.t.join();
			t3.t.join();
		} catch (InterruptedException e) {
			System.out.println("Main thread Interrupted");
		}
		
		System.out.println("Thread 1 is alive: " + t1.t.isAlive());
		System.out.println("Thread 2 is alive: " + t2.t.isAlive());
		System.out.println("Thread 3 is alive: " + t3.t.isAlive());
		System.out.println("Main thread exiting.");

    }
}