import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class AddressBookTest {

    @Test
    public void givenAddressBookData_WhenRetrieved_ShouldMatchNumberOfContacts() throws AddressBookException {
        AddressBookService addressBookService = new AddressBookService();
        List<Person> addressBookData = addressBookService.readData();
        Assert.assertEquals(6, addressBookData.size());
    }

    @Test
    public void givenNewDataForContact_WhenUpdated_ShouldSyncWithDatabase() throws AddressBookException {
        AddressBookService addressBookService = new AddressBookService();
        new AddressBookServiceDatabase().readData();
        addressBookService.updateContactAddress("Sudha", "Jayanagar");
        boolean result = addressBookService.checkAddressBookInSyncWithDatabase("Sudha");
        Assert.assertTrue(result);
    }

    @Test
    public void givenAddressBookData_ShouldReturnNumberOfContactsWithinDateRange() throws AddressBookException {
        AddressBookService addressBookService = new AddressBookService();
        LocalDate start = LocalDate.of(2020, 03, 01);
        LocalDate end = LocalDate.of(2020, 04, 30);
        List<Person> addressBookData = addressBookService.readAddressBookData(start, end);
        Assert.assertEquals(2, addressBookData.size());
    }

    @Test
    public void givenAddressBookData_ShouldReturnNumberOfContactsInCity() throws AddressBookException {
        AddressBookService addressBookService = new AddressBookService();
        Assert.assertEquals(5, addressBookService.readData("City", "Bangalore").size());
    }

    @Test
    public void givenAddressBookData_ShouldReturnNumberOfContactsInState() throws AddressBookException {
        AddressBookService addressBookService = new AddressBookService();
        Assert.assertEquals(1, addressBookService.readData("State", "AP").size());
    }

    @Test
    public void givenNewContact_WhenAdded_ShouldSyncWithDatabase() throws AddressBookException {
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readData();
        addressBookService.addNewContact("Harika", "M", "J P Nagar", "Bangalore", "KA", 560091, 969572255, "harika@gmail.com");
        boolean result = addressBookService.checkAddressBookInSyncWithDatabase("Harika");
        Assert.assertTrue(result);
    }

    @Test
    public void givenNewContacts_WhenAdded_ShouldReturnNumberOfEntries() throws AddressBookException {
        Person[] arrayOfContacts = {
                new Person(0, "Jeff", "L", "Bangalore", "Bangalore", "KA", 560065, 959144433, "jeff@gmail.com"),
                new Person(0, "Mark", "K", "Bangalore", "Bangalore", "KA", 565065, 957144433, "mark@gmail.com"),
                new Person(0, "Priya", "M", "Bangalore", "Bangalore", "KA", 530065, 950144433, "priya@gmail.com"),
        };
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.updateAddressBookWithThreads(Arrays.asList(arrayOfContacts));
        Assert.assertEquals(5, addressBookService.updateAddressBookWithThreads(Arrays.asList(arrayOfContacts)));
    }

}
