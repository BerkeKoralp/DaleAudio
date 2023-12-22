import java.io.*;
import java.net.Socket;

public class Client {
    // Buffer size for reading data from the file
    private static final int BUFFER_SIZE = 4096;
    // Method to send a file to the server
    public  void sendFileToServer(String serverAddress, String filePath) {
        int serverPort = 3000;
        try (Socket socket = new Socket(serverAddress, serverPort);
             OutputStream out = socket.getOutputStream();
             BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(filePath))) {

            // Send the file name to the server
            sendFileName(out, filePath);

            // Send file content to the server
            sendFileContent(out, fileIn);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    // Method to send the file name to the server
    private static void sendFileName(OutputStream out, String filePath) throws IOException {
        // Extract the file name from the file path
        String fileName = new File(filePath).getName();
        // Convert file name to bytes and send the length followed by the bytes
        byte[] fileNameBytes = fileName.getBytes();
        out.write(fileNameBytes.length);
        out.write(fileNameBytes);
    }

    // Method to send the file content to the server
    private static void sendFileContent(OutputStream out, BufferedInputStream fileIn) throws IOException {
        // Buffer for reading data from the file
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;

        // Read data from the file and send it to the server
        while ((bytesRead = fileIn.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
    }
}
