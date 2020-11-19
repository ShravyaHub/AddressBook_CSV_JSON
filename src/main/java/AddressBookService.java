import java.time.LocalDate;
import java.util.List;

public class AddressBookService {

    public List<PersonData> addressBookData;

    public List<PersonData> readData() throws AddressBookException {
        try {
            return this.addressBookData = new AddressBookServiceDatabase().readData();
        } catch (AddressBookException addressBookException) {
            throw new AddressBookException("Cannot execute query", AddressBookException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
    }

    public List<PersonData> readData(String condition, String value) throws AddressBookException {
        try {
            return new AddressBookServiceDatabase().readData(condition, value);
        } catch (AddressBookException addressBookException) {
            throw new AddressBookException("Cannot execute query", AddressBookException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
    }

    public void updateContactAddress(String name, String address) throws AddressBookException {
        readData();
        int result = new AddressBookServiceDatabase().updateAddressBookData(name, address);
        if (result == 0) throw new AddressBookException("Address book update failed", AddressBookException.ExceptionType.UPDATE_FAILED);
        PersonData addressBookData = this.getAddressBookData(name);
        if (addressBookData != null) addressBookData.address = address;
    }

    private PersonData getAddressBookData(String name) {
        return this.addressBookData.stream()
                .filter(addressBookItem -> addressBookItem.firstName.equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean checkAddressBookInSyncWithDatabase(String name) throws AddressBookException {
        try {
            List<PersonData> addressBookData = new  AddressBookServiceDatabase().getAddressBookData(name);
            return addressBookData.get(0).equals(getAddressBookData(name));
        } catch (AddressBookException addressBookException) {
            throw new AddressBookException("Cannot execute query", AddressBookException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
    }

    public List<PersonData> readAddressBookData(LocalDate start, LocalDate end) throws AddressBookException {
        try {
            return new AddressBookServiceDatabase().readData(start, end);
        } catch (AddressBookException addressBookException) {
            throw new AddressBookException("Cannot execute query", AddressBookException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
    }

    public void addNewContact(String firstName, String lastName, String address, String city, String state, int zip, long phoneNumber, String email) throws AddressBookException {
        addressBookData.add(new AddressBookServiceDatabase().addNewContact(firstName, lastName, address, city, state, zip, phoneNumber, email));
    }

}
