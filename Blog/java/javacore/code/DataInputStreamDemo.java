
import java.io.*;

public class DataInputStreamDemo {
    public static void main(String[] args) throws IOException {
        int iVal = 0;
        boolean bVal = false;
        DataInputStream dis;

        try {
            dis = new DataInputStream(new FileInputStream("a.txt"));
            iVal = dis.readInt();
            bVal = dis.readBoolean();
            // dis.readUTF();
        } catch (IOException exc) {
            System.out.println("Error open file.");
        } catch (IOException exc) {
            System.out.println("Error read file.");
        } finally {
            dis.close();
        }

    }
}
