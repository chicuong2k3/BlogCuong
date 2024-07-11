import java.io.*;

public class FileReaderDemo {
    public static void main(String[] args) throws IOException {
        FileReader fr = new FileReader("a1.txt");
        while (true) {
            int i = fr.read();
            if (i == -1)
                break;
            char ch = (char) i;
        }
        fr.close();
    }
}