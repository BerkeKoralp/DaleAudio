import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        String serverAddress = "EXTERNAL_IP_ADDRESS"; // Replace with the actual IP address of the server
        int serverPort = 3000;
        String filePath = "C:\\Users\\E\\Downloads\\ALIZADE-SU-AN.mp3";

        try (Socket socket = new Socket(serverAddress, serverPort);
             OutputStream out = socket.getOutputStream();
             BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(filePath))) {

            // Send the file name to the server
            String fileName = new File(filePath).getName();
            byte[] fileNameBytes = fileName.getBytes();
            out.write(fileNameBytes.length);
            out.write(fileNameBytes);

            // Buffer for reading data
            byte[] buffer = new byte[4096];
            int bytesRead;

            // Read data from the file and send it to the server
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            System.out.println("File sent successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
