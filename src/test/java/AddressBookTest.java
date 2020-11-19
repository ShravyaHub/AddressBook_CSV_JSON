import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AddressBookTest {

    @Test
    public void givenAddressBookData_WhenRetrieved_ShouldMatchNumberOfContacts() throws AddressBookException {
        AddressBookServiceDatabase addressBookServiceDatabase = new AddressBookServiceDatabase();
        List<Person> addressBookData = addressBookServiceDatabase.readData();
        Assert.assertEquals(3, addressBookData.size());
    }

}
