import java.io.*;
import java.net.Socket;

public class Client {

    public static void sendData(String serverAddress, int serverPort, String filePath){
        try(Socket socket = new Socket(serverAddress, serverPort); OutputStream outputStream = socket.getOutputStream(); BufferedInputStream fileInput = new BufferedInputStream(new FileInputStream(filePath))){
            // send the file name to the server
            String fileName = new File(filePath).getName();
            byte[] fileNameBytes = fileName.getBytes();
            outputStream.write(fileNameBytes.length);
            outputStream.write(fileNameBytes);

            // buffer for reading data
            byte[] buffer = new byte[4096];
            int bytesRead;

            // read data from the file and send it to the server
            while((bytesRead = fileInput.read(buffer)) != -1){
                outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println(fileName + " sent successfully!");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverAddress = "212.133.250.63"; // Replace with the actual IP address of the server
        int serverPort = 3000;
        String filePath = "C:\\Users\\E\\Downloads\\ALIZADE-SU-AN.mp3";

        sendData(serverAddress, serverPort, filePath);
    }
}
