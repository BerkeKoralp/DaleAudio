import org.sqlite.SQLiteDataSource;

import java.sql.Connection;

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
}
