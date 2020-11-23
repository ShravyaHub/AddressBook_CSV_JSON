import javax.xml.crypto.Data;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookServiceDatabase {

    private List<Person> addressBookData;
    private PreparedStatement addressBookPreparedStatement;

    public List<Person> readData() throws AddressBookException {
        String sql = "SELECT * FROM Person";
        return getAddressBookDataUsingDatabase(sql);
    }

    public List<Person> readData(LocalDate start, LocalDate end) throws AddressBookException {
        String sql = String.format("SELECT * FROM Person WHERE AddDate BETWEEN '%s' AND '%s';", Date.valueOf(start), Date.valueOf(end));
        return getAddressBookDataUsingDatabase(sql);
    }

    public List<Person> readData(String condition, String value) throws AddressBookException {
        String sql = String.format("SELECT * FROM Person WHERE %s = '%s';", condition, value);
        return getAddressBookDataUsingDatabase(sql);
    }

    private List<Person> getAddressBookDataUsingDatabase(String sql) throws AddressBookException {
        List<Person> addressBookData = new ArrayList();
        try(Connection connection = new DatabaseConnection().getConnection()) {
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
                int id = resultSet.getInt("PersonID");
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String address = resultSet.getString("Address");
                String city = resultSet.getString("City");
                String state = resultSet.getString("State");
                int zip = resultSet.getInt("Zip");
                long phoneNumber = resultSet.getLong("PhoneNumber");
                String email = resultSet.getString("Email");
                addressBookData.add(new Person(id, firstName, lastName, address, city, state, zip, phoneNumber, email));
            }
        } catch (SQLException sqlException) {
            throw new AddressBookException(sqlException.getMessage(), AddressBookException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
        return addressBookData;
    }

    public int updateAddressBookData(String name, String address) throws AddressBookException {
        try(Connection connection= new DatabaseConnection().getConnection()) {
            String sql = String.format("UPDATE Person SET Address = '%s' WHERE FirstName = '%s';", address, name);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            return preparedStatement.executeUpdate(sql);
        } catch (SQLException sqlException) {
            throw new AddressBookException(sqlException.getMessage(), AddressBookException.ExceptionType.CONNECTION_FAIL);
        }
    }

    public List<Person> getAddressBookData(String name) throws AddressBookException {
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
            Connection connection = new DatabaseConnection().getConnection();
            String sql = "SELECT * FROM Person WHERE FirstName = ?";
            addressBookPreparedStatement = connection.prepareStatement(sql);
        } catch (SQLException sqlException) {
            throw new AddressBookException(sqlException.getMessage(), AddressBookException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
    }

    public Person addNewContact(String firstName, String lastName, String address, String city, String state, int zip, long phoneNumber, String email) throws AddressBookException {
        int personID = -1;
        Person person;
        String sql = String.format("INSERT INTO Person(FirstName, LastName, Address, City, State, Zip, PhoneNumber, Email) VALUES ('%s', '%s', '%s', '%s', '%s', '%d', '%d', '%s')", firstName, lastName, address, city, state, zip, phoneNumber, email);
        try(Connection connection = new DatabaseConnection().getConnection()) {
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if(rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) personID = resultSet.getInt(1);
            }
            person = new Person(personID, firstName, lastName, address, city, state, zip, phoneNumber, email);
        } catch (SQLException sqlException) {
            throw new AddressBookException(sqlException.getMessage(), AddressBookException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
        return person;
    }

}
