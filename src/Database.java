import org.sqlite.SQLiteDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    Connection connection = null;

    public Database() {
        try {
            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl("jdbc:sqlite:dale.db");
            connection = dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public boolean authenticateUser(String username, String password){
        boolean isAuthenticated = false;
        String checkUsername = "SELECT * FROM users WHERE username = ?";
        try(PreparedStatement statement = connection.prepareStatement(checkUsername)){
            statement.setString(1, username);

            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()) {
                    String usersPassword = resultSet.getString("password");
                    if(password.equals(usersPassword)) {
                        isAuthenticated = true;
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return isAuthenticated;
    }

    public void updateIPAddress(String username){
        String externalIP = getExternalIPAddress();
        String updateIP = "UPDATE users SET ipv4_address = ? WHERE username = ?";
        try(PreparedStatement statement = connection.prepareStatement(updateIP)){
            statement.setString(1, externalIP);
            statement.setString(2,username);
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static String getExternalIPAddress() {
        try {
            URL url = new URL("http://httpbin.org/ip");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Parse the JSON response to get the IP address
                String jsonResponse = response.toString();
                int startIndex = jsonResponse.indexOf("\"origin\":") + "\"origin\":".length();
                int endIndex = jsonResponse.indexOf("}", startIndex);

                if (startIndex != -1 && endIndex != -1) {
                    String ipAddress = jsonResponse.substring(startIndex, endIndex).replaceAll("\"", "").trim();
                    return ipAddress;
                } else {
                    // Handle the case where the JSON structure is unexpected
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately in your application
            return null;
        }
    }


    public Connection getConnection() {
        return connection;
    }
}
