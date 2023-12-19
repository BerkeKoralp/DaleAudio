import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        int portNumber = 3000;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server waiting for connection on port " + portNumber);

            // Wait for a client to connect
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected from: " + clientSocket.getInetAddress().getHostAddress());

            // Open input stream to read data from the client
            InputStream in = clientSocket.getInputStream();

            // Open output stream to write the received data to a file
            BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream("received_music.mp3"));

            // Buffer for reading data
            byte[] buffer = new byte[4096];
            int bytesRead;

            // Read data from the client and write it to the file
            while ((bytesRead = in.read(buffer)) != -1) {
                fileOut.write(buffer, 0, bytesRead);
            }

            System.out.println("File received successfully.");

            // Clean up
            fileOut.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}