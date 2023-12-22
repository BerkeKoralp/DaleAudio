import org.sqlite.SQLiteDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
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

    public void updateIPAddress(String username) {
        String localIP = getLocalIPAddress();
        String updateIP = "UPDATE users SET ipv4_address = ? WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateIP)) {
            statement.setString(1, localIP);
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getLocalIPAddress() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            // Handle the exception appropriately in your application
            return null;
        }
    }

    public void addUser(String username, String password, String IP) {
        String addUserQuery = "INSERT INTO users (username, password, ipv4_address) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(addUserQuery)) {
            // Setting the values for the prepared statement
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, IP);

            // Executing the query to insert the new user
            preparedStatement.executeUpdate();

            System.out.println("User added successfully!");
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        }
    }

    public void deleteUser(String username) {
        String deleteUserQuery = "DELETE FROM users WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteUserQuery)) {
            // Setting the value for the prepared statement
            preparedStatement.setString(1, username);

            // Executing the query to delete the user
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("User not found or could not be deleted.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        }
    }

    public String retrieveIP(String username) {
        String retrieveIPQuery = "SELECT ipv4_address FROM users WHERE username = ?";
        String ipv4_address = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(retrieveIPQuery)) {
            // Setting the value for the prepared statement
            preparedStatement.setString(1, username);

            // Executing the query to retrieve the IP address
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    ipv4_address = resultSet.getString("ipv4_address");
                    System.out.println("IP address retrieved successfully: " + ipv4_address);
                } else {
                    System.out.println("User not found or no IP address associated with the user.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ipv4_address;
    }

    public Connection getConnection() {
        return connection;
    }

}
