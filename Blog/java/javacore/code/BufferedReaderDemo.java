import java.io.*;

public class BufferedReaderDemo {
    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("data.txt"));
        // BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "utf8"));
        String str;
        while (true) {
            str = br.readLine();
            if (str == null)
                break;
            System.out.println(str);
        }

        br.close();
    }
}