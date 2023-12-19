import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        String serverAddress = "192.168.1.2"; // Replace with the actual IP address of the server
        int serverPort = 3000;

        try (Socket socket = new Socket(serverAddress, serverPort);
             OutputStream out = socket.getOutputStream();
             BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream("path_to_your_music_file.mp3"))) {

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