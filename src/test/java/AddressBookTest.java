import org.junit.Assert;
import org.junit.Test;

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

}
