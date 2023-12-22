import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private InputStream in;

    public Server(int portNumber) throws IOException {
        serverSocket = new ServerSocket(portNumber);
        System.out.println("Server waiting for connection on port " + portNumber);
    }

    public void acceptClientConnection() throws IOException {
        clientSocket = serverSocket.accept();
        System.out.println("Client connected from: " + clientSocket.getInetAddress().getHostAddress());
    }

    public void createReceivedSongsFolder() throws IOException {
        String folderName = "ReceivedSongs";
        Path folderPath = Paths.get(folderName);
        if (!Files.exists(folderPath)) {
            Files.createDirectory(folderPath);
            System.out.println("Created folder: " + folderPath.toAbsolutePath());
        }
    }

    public void initializeInputStream() throws IOException {
        in = clientSocket.getInputStream();
    }

    public void receiveAndSaveFile() throws IOException {
        // Read the file name length
        int fileNameLength = in.read();

        // Read the file name
        byte[] fileNameBytes = new byte[fileNameLength];
        in.read(fileNameBytes);
        String fileName = new String(fileNameBytes);

        // Construct the complete path for the received file
        Path filePath = Paths.get("ReceivedSongs", fileName);

        // Open output stream to write the received data to a file
        try (BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(filePath.toString()))) {
            // Buffer for reading data
            byte[] buffer = new byte[4096];
            int bytesRead;

            // Read data from the client and write it to the file
            while ((bytesRead = in.read(buffer)) != -1) {
                fileOut.write(buffer, 0, bytesRead);
            }

            System.out.println("File received successfully and stored in: " + filePath.toAbsolutePath());
        }
    }
    public void closeConnections() throws IOException {
        in.close();
        clientSocket.close();
    }

    public  void receiveFile() throws IOException {
     acceptClientConnection();
     createReceivedSongsFolder();
     initializeInputStream();
     receiveAndSaveFile();
     closeConnections();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public InputStream getIn() {
        return in;
    }
}
