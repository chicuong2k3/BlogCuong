import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class BufferedInputOutputStreamDemo {
    public static void main(String[] args) {
        try {
            BufferedInputStream bin = new BufferedInputStream(new FileInputStream("b.dat"));
            BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream("a.dat"));
            while (true) {
                int datum = bin.read();

                if (datum == -1)
                    break;
                bout.write(datum);
            }
            bout.flush();
            bin.close();
            bout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
