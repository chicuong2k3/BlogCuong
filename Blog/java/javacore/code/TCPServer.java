import java.io.*;
import java.net.*;

public class TCPServer {
	public static void main(String[] arg) {
		try {
			ServerSocket ss = new ServerSocket(3200);
			do {
				System.out.println("Waiting for a Client");
			
				Socket s = ss.accept();
				
				InputStream is = s.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				
				OutputStream os = s.getOutputStream();
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

				while (true) {
					String receivedMessage = br.readLine();
					System.out.println("Received : " + receivedMessage);
					if (receivedMessage.equalsIgnoreCase("quit")) {
						System.out.println("Client has left!");
						break;
					}
					else {
						DataInputStream din = new DataInputStream(System.in);
						String k = din.readLine();
						bw.write(k);
						bw.newLine();
						bw.flush();
					}
				}

				bw.close();
				br.close();

                s.close();
			}
			while (true);
			
            
		}
		catch(IOException e) {
			System.out.println("There are some errors");
		}
	}
}
