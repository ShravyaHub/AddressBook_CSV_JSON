import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AddressBookService {

    public List<Person> addressBookData;

    public List<Person> readData() throws AddressBookException {
        try {
            return this.addressBookData = new AddressBookServiceDatabase().readData();
        } catch (AddressBookException addressBookException) {
            throw new AddressBookException("Cannot execute query", AddressBookException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
    }

    public List<Person> readData(String condition, String value) throws AddressBookException {
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
        Person addressBookData = this.getAddressBookData(name);
        if (addressBookData != null) addressBookData.address = address;
    }

    private Person getAddressBookData(String name) {
        return this.addressBookData.stream()
                .filter(addressBookItem -> addressBookItem.firstName.equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean checkAddressBookInSyncWithDatabase(String name) throws AddressBookException {
        try {
            List<Person> addressBookData = new  AddressBookServiceDatabase().getAddressBookData(name);
            return addressBookData.get(0).equals(getAddressBookData(name));
        } catch (AddressBookException addressBookException) {
            throw new AddressBookException("Cannot execute query", AddressBookException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
    }

    public List<Person> readAddressBookData(LocalDate start, LocalDate end) throws AddressBookException {
        try {
            return new AddressBookServiceDatabase().readData(start, end);
        } catch (AddressBookException addressBookException) {
            throw new AddressBookException("Cannot execute query", AddressBookException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
    }

    public void addNewContact(String firstName, String lastName, String address, String city, String state, int zip, long phoneNumber, String email) throws AddressBookException {
        addressBookData.add(new AddressBookServiceDatabase().addNewContact(firstName, lastName, address, city, state, zip, phoneNumber, email));
    }

    public int updateAddressBookWithThreads(List<Person> contactsList) throws AddressBookException {
        Map<Integer, Boolean> contactsMap = new HashMap<>();
        for(int index = 0; index < contactsList.size(); index++) {
            int finalIndex = index;
            Runnable task = () -> {
                contactsMap.put(contactsList.hashCode(), false);
                System.out.println("Contact being added: " + Thread.currentThread().getName());
                try {
                    this.addNewContact(contactsList.get(finalIndex).firstName, contactsList.get(finalIndex).lastName, contactsList.get(finalIndex).address, contactsList.get(finalIndex).city, contactsList.get(finalIndex).state, contactsList.get(finalIndex).zip, contactsList.get(finalIndex).phoneNumber, contactsList.get(finalIndex).email);
                } catch (AddressBookException addressBookException) {
                    new AddressBookException("Cannot update using threads", AddressBookException.ExceptionType.CANNOT_EXECUTE_QUERY);
                }
                contactsMap.put(contactsList.hashCode(), true);
                System.out.println("Contact added: " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, contactsList.get(index).firstName);
            thread.start();
        }
        while (contactsMap.containsValue(false) && contactsList.size() != 3) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException interruptedException) {

            }
        }
        System.out.println(contactsList);
        return new AddressBookService().readData().size();
    }

    public int addPersonToJSONServer(int id, String firstName, String lastName, String address, String city, String state, int zip, int phoneNumber, String email) throws AddressBookException {
        try {
            RestAssured.baseURI = "http://localhost:3000";
            RequestSpecification request = given();
            JSONObject requestParams = new JSONObject();
            requestParams.put("id", id);
            requestParams.put("firstName", firstName);
            requestParams.put("lastName", lastName);
            requestParams.put("address", address);
            requestParams.put("city", city);
            requestParams.put("state", state);
            requestParams.put("zip", zip);
            requestParams.put("phoneNumber", phoneNumber);
            requestParams.put("email", email);
            request.header("Content-Type", "application/json");
            request.body(requestParams.toString());
            Response response = request.post("/person");
            return response.getStatusCode();
        } catch (JSONException jsonException) {
            throw new AddressBookException(jsonException.getMessage(), AddressBookException.ExceptionType.CONNECTION_FAIL);
        }
    }

    public int getDataFromJSONServer() {
        Response response = RestAssured.get("http://localhost:3000/person");
        System.out.println(response.getBody().asString());
        return response.getStatusCode();
    }

}
