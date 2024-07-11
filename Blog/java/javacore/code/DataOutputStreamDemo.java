
import java.io.*;

public class DataOutputStreamDemo {
    public static void main(String[] args) throws IOException {

        DataOutputStream dos;
        try {
            dos = new DataOutputStream(new FileOutputStream("a.txt"));
            dos.writeInt(12345);
            dos.writeDouble(-67.76);
            dos.writeBoolean(true);
            dos.writeUTF("Some text");

            dos.close();
        } catch (IOException exc) {
            System.out.println("Error open file.");
        }

    }
}
