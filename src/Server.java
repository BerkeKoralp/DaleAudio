import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Server {

    public static void listen(int portNumber){
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server waiting for connection on port " + portNumber);

            // Wait for a client to connect
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected from: " + clientSocket.getInetAddress().getHostAddress());

            // Create a folder to store received songs
            String folderName = "ReceivedSongs";
            Path folderPath = Paths.get(folderName);
            if (!Files.exists(folderPath)) {
                Files.createDirectory(folderPath);
                System.out.println("Created folder: " + folderPath.toAbsolutePath());
            }

            // Open input stream to read data from the client
            InputStream in = clientSocket.getInputStream();

            // Read the file name length
            int fileNameLength = in.read();

            // Read the file name
            byte[] fileNameBytes = new byte[fileNameLength];
            in.read(fileNameBytes);
            String fileName = new String(fileNameBytes);

            // Construct the complete path for the received file
            Path filePath = Paths.get(folderName, fileName);

            // Open output stream to write the received data to a file
            BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(filePath.toString()));

            // Buffer for reading data
            byte[] buffer = new byte[4096];
            int bytesRead;

            // Read data from the client and write it to the file
            while ((bytesRead = in.read(buffer)) != -1) {
                fileOut.write(buffer, 0, bytesRead);
            }

            System.out.println("File received successfully and stored in: " + filePath.toAbsolutePath());

            // Clean up
            fileOut.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        listen(3000);
    }
}
