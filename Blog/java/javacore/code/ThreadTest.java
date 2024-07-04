class CustomThread extends Thread {
    public CustomThread(String threadName) {
        this.setName(threadName);
    }

    public void run() {
        try {
			for(int i = 1; i <= 5; i++) {
				System.out.println("Value : " + i);
				Thread.sleep(3000);
			}
		} catch (InterruptedException iex) {
			System.out.println(iex.getMessage());
		}
    }
}

public class ThreadTest {
    public static void main(String[] args) {
		CustomThread t = new CustomThread("MyThread");
        t.start();
    }
}