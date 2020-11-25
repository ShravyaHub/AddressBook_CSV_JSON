import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AddressBookJSONTest {

    @Test
    public void givenAddressBookData_WhenAddedToJSONServer_ShouldValidateRequestResponseReceived() throws AddressBookException {
        try {
            AddressBookService addressBookService = new AddressBookService();
            assertEquals(201, addressBookService.addPersonToJSONServer(1, "Priya", "M", "Bangalore", "Bangalore", "KA", 560054, 969175556, "priya@gmail.com"));
        } catch (AddressBookException addressBookException) {
            throw new AddressBookException("Cannot connect to JSON server", AddressBookException.ExceptionType.CONNECTION_FAIL);
        }
    }

    @Test
    public void givenAddressBookData_WhenRetrieved_ShouldValidateRequestResponseReceived() {
        assertEquals(200, new AddressBookService().getDataFromJSONServer());
    }

}
