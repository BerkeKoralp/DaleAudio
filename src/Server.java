import java.io.*;
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

            // Read the file name length
            int fileNameLength = in.read();

            // Read the file name
            byte[] fileNameBytes = new byte[fileNameLength];
            in.read(fileNameBytes);
            String fileName = new String(fileNameBytes);

            // Open output stream to write the received data to a file
            BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(fileName));

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
