import org.junit.*;

import javax.xml.crypto.Data;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class DatabaseTest {

    static Database database ;
    private static final String username = "newUser";
    private static final String password = "newPassword";
    private static final String ip = "127.0.0.1";
    @BeforeClass
    public static void setUp() throws Exception {

        database = new Database();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        try {
            if (database.getConnection() != null && !database.getConnection().isClosed()) {
                database.getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Before
    public void beforeMethods(){
        database.addUser(username,password,ip);
    }

    @After
    public void afterMethods(){
        database.deleteUser(username);
    }
    @Test
    public void authenticateUser() {

        assertTrue("Authenticated user :"+username, database.authenticateUser(username,password));

    }

    @Test
    public void updateIPAddress() {
        database.updateIPAddress(username);
        assertFalse(ip==database.retrieveIP(username));
    }

    @Test
    public void addUser() {
        assertTrue(database.authenticateUser(username,password));
    }

    @Test
    public void deleteUser() {
        database.deleteUser(username);
        assertFalse(database.authenticateUser(username,password));
    }

    @Test
    public void retrieveIP() {
        boolean ans = ip.equals(database.retrieveIP(username));
        assertFalse(ans);
    }


}