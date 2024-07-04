
class Printer {
	void print(String msg) {
		System.out.print("[" + msg);
		try {
			Thread.sleep(1000);
		}
		catch(InterruptedException e) {
			System.out.println("Interrupted");
		}
		System.out.println("]");
	}
}

class PrintThread implements Runnable {
	String msg;
	Printer printer;
	Thread t;

	public PrintThread(Printer printer, String msg) {
		this.printer = printer;
		this.msg = msg;
		t = new Thread(this);
		t.start();
	}

	public void run() {
        synchronized (printer) {
            printer.print(msg);
        }   
	}
}

public class SynchronizedBlock {
	public static void main(String[] args) {

		Printer printer = new Printer();
        PrintThread t1 = new PrintThread(printer, "Hello");
		PrintThread t2 = new PrintThread(printer, "Synchronized");
		PrintThread t3 = new PrintThread(printer, "World");
		try {
			t1.t.join();
			t2.t.join();
			t3.t.join();
		} catch(InterruptedException e) {
			System.out.println("Interrupted");
		}

	}
}