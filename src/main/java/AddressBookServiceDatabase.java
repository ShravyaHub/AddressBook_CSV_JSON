import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookServiceDatabase {

    private List<PersonData> addressBookData;
    private PreparedStatement addressBookPreparedStatement;

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

    public List<PersonData> readData() throws AddressBookException {
        String sql = "SELECT * FROM Person";
        return getAddressBookDataUsingDatabase(sql);
    }

    public List<PersonData> readData(LocalDate start, LocalDate end) throws AddressBookException {
        String sql = String.format("SELECT * FROM Person WHERE AddDate BETWEEN '%s' AND '%s';", Date.valueOf(start), Date.valueOf(end));
        return getAddressBookDataUsingDatabase(sql);
    }

    public List<PersonData> readData(String condition, String value) throws AddressBookException {
        String sql = String.format("SELECT * FROM Person WHERE %s = '%s';", condition, value);
        return getAddressBookDataUsingDatabase(sql);
    }

    private List<PersonData> getAddressBookDataUsingDatabase(String sql) throws AddressBookException {
        List<PersonData> addressBookData = new ArrayList();
        try(Connection connection= this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            addressBookData = this.getAddressBookData(resultSet);
        } catch (SQLException sqlException) {
            throw new AddressBookException(sqlException.getMessage(), AddressBookException.ExceptionType.CONNECTION_FAIL);
        }
        return addressBookData;
    }

    private List<PersonData> getAddressBookData(ResultSet resultSet) throws AddressBookException {
        List<PersonData> addressBookData = new ArrayList();
        try {
            while(resultSet.next()) {
                int id = resultSet.getInt("PersonID");
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String address = resultSet.getString("Address");
                String city = resultSet.getString("City");
                String state = resultSet.getString("State");
                int zip = resultSet.getInt("Zip");
                long phoneNumber = resultSet.getLong("PhoneNumber");
                String email = resultSet.getString("Email");
                addressBookData.add(new PersonData(id, firstName, lastName, address, city, state, zip, phoneNumber, email));
            }
        } catch (SQLException sqlException) {
            throw new AddressBookException(sqlException.getMessage(), AddressBookException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
        return addressBookData;
    }

    public int updateAddressBookData(String name, String address) throws AddressBookException {
        try(Connection connection= this.getConnection()) {
            String sql = String.format("UPDATE Person SET Address = '%s' WHERE FirstName = '%s';", address, name);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            return preparedStatement.executeUpdate(sql);
        } catch (SQLException sqlException) {
            throw new AddressBookException(sqlException.getMessage(), AddressBookException.ExceptionType.CONNECTION_FAIL);
        }
    }

    public List<PersonData> getAddressBookData(String name) throws AddressBookException {
        if(this.addressBookPreparedStatement == null) this.prepareAddressBookStatement();
        try {
            addressBookPreparedStatement.setString(1, name);
            ResultSet resultSet = addressBookPreparedStatement.executeQuery();
            addressBookData = this.getAddressBookData(resultSet);
        } catch(SQLException sqlException) {
            throw new AddressBookException(sqlException.getMessage(), AddressBookException.ExceptionType.CONNECTION_FAIL);
        }
        return addressBookData;
    }

    private void prepareAddressBookStatement() throws AddressBookException {
        try {
            Connection connection = this.getConnection();
            String sql = "SELECT * FROM Person WHERE FirstName = ?";
            addressBookPreparedStatement = connection.prepareStatement(sql);
        } catch (SQLException sqlException) {
            throw new AddressBookException(sqlException.getMessage(), AddressBookException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
    }
}
