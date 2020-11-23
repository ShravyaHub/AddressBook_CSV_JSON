import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/AddressBookService?useSSL=false";
        String username = "root";
        String password = "shravya";
        Connection connection;
        System.out.println("Connecting to database: " + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, username, password);
        System.out.println("Connection is successful: " + connection);
        return connection;
    }

}
