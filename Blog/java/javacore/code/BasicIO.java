import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

public class BasicIO {
  public static void main(String[] args) {
    // Scanner scanner = new Scanner(System.in);
		// int n = scanner.nextInt();
    // String line = scanner.nextLine();
		// scanner.close();

    // System.in là 1 InputStream đọc dữ liệu trực tiếp từ bàn phím
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    try {
      // System.out là 1 PrintStream kế thừa OutputStream
      System.out.print("Enter a: ");
      int a = Integer.parseInt(reader.readLine());
      int b = Integer.parseInt(reader.readLine());
      System.out.println(a + b);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Scanner scanner = new Scanner(System.in);
    int number = scanner.nextInt();
    String str = scanner.nextLine();

    String name = JOptionPane.showInputDialog("Please enter your name");
  }
}
