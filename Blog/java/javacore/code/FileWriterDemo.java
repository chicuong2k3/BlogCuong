import java.io.*;

public class FileWriterDemo {
    public static void main(String[] args) throws IOException {
        FileWriter fw;
        try {
            fw = new FileWriter("a1.txt");
        } catch (IOException exc) {
            System.out.println("Error opening file");
            return;
        }

        String str = "";
        for (int i = 0; i <= 10; i++) {
            str = "Hello: " + i + "\r\n";
            fw.write(str);
        }

        fw.close();
    }
}