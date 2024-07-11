
import java.io.*;

public class RandomAccessFileDemo {
    public static void main(String[] args) throws IOException {
        double data[] = { 19.4, 10.1, 123.54, 33.0, 87.9, 74.25 };
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile("random.dat", "rw");
        } catch (FileNotFoundException exc) {
            System.out.println("Cannot open file.");
            return;
        }

        for (int i = 0; i < data.length; i++) {
            try {
                raf.writeDouble(data[i]);
            } catch (IOException exc) {
                System.out.println("Error writing to file.");
                return;
            }
        }

        double d;
        try {
            raf.seek(0); // seek to first double
            d = raf.readDouble();

            raf.seek(8); // seek to second double
            d = raf.readDouble();

            raf.seek(8 * 3); // seek to fourth double
            d = raf.readDouble();

            // Now, read every other value.
            for (int i = 0; i < data.length; i += 2) {
                raf.seek(8 * i); // seek to ith double
                d = raf.readDouble();
            }
        } catch (IOException exc) {
            System.out.println("Error seeking or reading.");
        }
        raf.close();
    }
}
