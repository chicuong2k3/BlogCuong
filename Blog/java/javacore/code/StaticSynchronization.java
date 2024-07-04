class Printer {
	synchronized static void print(String msg) {
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
        printer.print(msg);
            
	}
}

public class StaticSynchronization {
	public static void main(String[] args) {

		Printer printer1 = new Printer();
        Printer printer2 = new Printer();
        PrintThread t1 = new PrintThread(printer1, "Hello");
		PrintThread t2 = new PrintThread(printer1, "Static");
		PrintThread t3 = new PrintThread(printer2, "Synchronized");
        PrintThread t4 = new PrintThread(printer2, "World");
		try {
			t1.t.join();
			t2.t.join();
			t3.t.join();
            t4.t.join();
		} catch(InterruptedException e) {
			System.out.println("Interrupted");
		}

	}
}