import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookServiceDatabase {

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/AddressBookService?useSSL=false";
        String username = "root";
        String password = "shravya";
        Connection connection;
        System.out.println("Connecting to database: " + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, username, password);
        System.out.println("Connection is successful: " + connection);
        return connection;
    }

    public List<Person> readData() throws AddressBookException {
        String sql = "SELECT * FROM Person";
        return getAddressBookDataUsingDatabase(sql);
    }

    private List<Person> getAddressBookDataUsingDatabase(String sql) throws AddressBookException {
        List<Person> addressBookData = new ArrayList();
        try(Connection connection= this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            addressBookData = this.getAddressBookData(resultSet);
        } catch (SQLException sqlException) {
            throw new AddressBookException(sqlException.getMessage(), AddressBookException.ExceptionType.CONNECTION_FAIL);
        }
        return addressBookData;
    }

    private List<Person> getAddressBookData(ResultSet resultSet) throws AddressBookException {
        List<Person> addressBookData = new ArrayList();
        try {
            while(resultSet.next()) {
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String address = resultSet.getString("Address");
                String city = resultSet.getString("City");
                String state = resultSet.getString("State");
                String zip = String.valueOf(resultSet.getInt("Zip"));
                String phoneNumber = String.valueOf(resultSet.getString("PhoneNumber"));
                addressBookData.add(new Person(firstName, lastName, address, city, state, zip, phoneNumber));
            }
        } catch (SQLException sqlException) {
            throw new AddressBookException(sqlException.getMessage(), AddressBookException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
        return addressBookData;
    }

}
