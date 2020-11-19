import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

public class AddressBookTest {

    @Test
    public void givenAddressBookData_WhenRetrieved_ShouldMatchNumberOfContacts() throws AddressBookException {
        AddressBookService addressBookService = new AddressBookService();
        List<PersonData> addressBookData = addressBookService.readData();
        Assert.assertEquals(3, addressBookData.size());
    }

    @Test
    public void givenNewDataForContact_WhenUpdated_ShouldSyncWithDatabase() throws AddressBookException {
        AddressBookService addressBookService = new AddressBookService();
        List<PersonData> addressBookData = new AddressBookServiceDatabase().readData();
        addressBookService.updateContactAddress("Sudha", "Jayanagar");
        boolean result = addressBookService.checkAddressBookInSyncWithDatabase("Sudha");
        Assert.assertTrue(result);
    }

    @Test
    public void givenAddressBookData_ShouldReturnNumberOfContactsWithinDateRange() throws AddressBookException {
        AddressBookService addressBookService = new AddressBookService();
        LocalDate start = LocalDate.of(2020, 03, 01);
        LocalDate end = LocalDate.of(2020, 04, 30);
        List<PersonData> addressBookData = addressBookService.readAddressBookData(start, end);
        Assert.assertEquals(2, addressBookData.size());
    }

    @Test
    public void givenAddressBookData_ShouldReturnNumberOfContactsInCity() throws AddressBookException {
        AddressBookService addressBookService = new AddressBookService();
        Assert.assertEquals(2, addressBookService.readData("City", "Bangalore").size());
    }

    @Test
    public void givenAddressBookData_ShouldReturnNumberOfContactsInState() throws AddressBookException {
        AddressBookService addressBookService = new AddressBookService();
        Assert.assertEquals(1, addressBookService.readData("State", "AP").size());
    }

}
