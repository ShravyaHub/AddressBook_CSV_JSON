import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
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

    @Test
    public void givenMultipleAddressBookData_WhenAddedToJSONServer_ShouldValidateRequestResponseReceived() throws AddressBookException {
        try {
            Person[] arrayOfContacts = {
                    new Person(2, "Jeff", "K", "Bangalore", "Bangalore", "KA", 566054, 969472635, "jeff@gmail.com"),
                    new Person(3, "Lasya", "M", "Bangalore", "Bangalore", "KA", 456653, 857463334, "lasya@gmail.com")
            };
            AddressBookService addressBookService = new AddressBookService();
            List<Integer> statusCodes = addressBookService.addMultiplePeopleToJSONServer(Arrays.asList(arrayOfContacts));
            assertEquals("201", statusCodes.get(0).toString());
            assertEquals("201", statusCodes.get(1).toString());
        } catch (AddressBookException addressBookException) {
            throw new AddressBookException("Cannot connect to JSON server", AddressBookException.ExceptionType.CONNECTION_FAIL);

        }
    }

    @Test
    public void givenAddressBookData_WhenUpdated_ShouldValidateRequestResponseReceived() throws AddressBookException {
        try {
            assertEquals(200, new AddressBookService().updatePersonDataInJSONServer(3, 959126665));
        } catch (AddressBookException addressBookException) {
            throw new AddressBookException("Cannot connect to JSON server", AddressBookException.ExceptionType.CONNECTION_FAIL);
        }
    }

    @Test
    public void givenAddressBookData_WhenDeleted_ShouldValidateRequestResponseReceived() {
        assertEquals(200, new AddressBookService().deleteContactFromJSONServer(2));
    }

}
