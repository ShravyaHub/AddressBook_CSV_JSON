import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class AddressBookManager implements IAddressBook{

    String firstName, lastName, address, city, state, zip, phoneNumber;
    Scanner scanner = new Scanner(System.in);
    ArrayList<Contact> contacts = new ArrayList<>();
    Contact contact;
    boolean personExists;
    int choice;
    HashMap<Contact, String> cityPersonMap = new HashMap<>();
    HashMap<Contact, String> statePersonMap = new HashMap<>();
    Set<Contact> keys = new HashSet<>();

    @Override
    public void createPerson(String addressBookName) {
        personExists = false;
        System.out.println("Enter first name: ");
        firstName = scanner.nextLine();
        System.out.println("Enter last name: ");
        lastName = scanner.nextLine();
        System.out.println("Enter address : ");
        address = scanner.nextLine();
        System.out.println("Enter city: ");
        city = scanner.nextLine();
        System.out.println("Enter state: ");
        state = scanner.nextLine();
        System.out.println("Enter zip: ");
        zip = scanner.nextLine();
        System.out.println("Enter phone number: ");
        phoneNumber = scanner.nextLine();
        if(contacts.size() > 0)
            for (Contact contact : contacts) {
                this.contact = contact;
                if (firstName.equals(this.contact.firstName) && lastName.equals(this.contact.lastName)) {
                    System.out.println("Contact " + this.contact.firstName + " " + this.contact.lastName + " already exists");
                    personExists = true;
                    break;
                }
            }
        if(!personExists) {
            contact = new Contact(firstName, lastName, address, city, state, zip, phoneNumber);
            contacts.add(contact);
            cityPersonMap.put(contact, city);
            statePersonMap.put(contact, state);
            addAddressBookToFile(firstName, lastName, address, city, state, phoneNumber, zip, addressBookName);
            try {
                addContactsToCSVFile(addressBookName);
            } catch (IOException ioException) {
                ioException.getMessage();
            }
            try {
                addContactsToJSONFile(addressBookName);
            } catch (IOException ioException) {
                ioException.getMessage();
            }
            System.out.println("Contact added: " + contact.firstName + " " + contact.lastName);
        }
    }

    private void addAddressBookToFile(String firstName, String lastName, String address, String city, String state, String phoneNumber, String zip, String addressBookName) {
        File contactsFile = new File("C:\\Users\\My PC\\Desktop\\Shravya" + addressBookName + ".txt");
        if (!contactsFile.exists())
            try {
                contactsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        for(int index = 0; index < contacts.size(); index++)
            try {
                FileWriter fileWriter = new FileWriter(contactsFile.getAbsoluteFile(), true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("Contact:" +
                        "\n1.First name: " + firstName +
                        "\n2.Last name: " + lastName +
                        "\n3.Address: " + address +
                        "\n4.City: " + city +
                        "\n5.State: " + state +
                        "\n6.Phone number: " + phoneNumber +
                        "\n7.Zip: " + zip + "\n");
                bufferedWriter.close();
            } catch (IOException ioException) {
                ioException.getMessage();
            }
    }

    public void showContactsFromFile(String addressBookName) {
        Path filePath = Paths.get("C:\\Users\\My PC\\Desktop\\Shravya" + addressBookName + ".txt");
        try {
            Files.lines(filePath).map(String::trim).forEach(System.out::println);
        } catch (IOException ioException) {
            ioException.getMessage();
        }
    }

    public void editPerson() {
        personExists = false;
        System.out.print("Enter first name: ");
        firstName = scanner.nextLine();
        System.out.println("Enter last name: ");
        lastName = scanner.nextLine();
        for (Contact contact : contacts)
            if (firstName.equals(contact.firstName) && lastName.equals(contact.lastName)) {
                personExists = true;
                System.out.println("Edit:\n 1.Address\n 2.City\n 3.State\n 4.Zip\n 5.Phone number ");
                choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> {
                        System.out.println("Enter new address: ");
                        address = scanner.nextLine();
                        contact.address = address;
                        System.out.println("Contact updated");
                    }
                    case 2 -> {
                        System.out.println("Enter new city: ");
                        city = scanner.nextLine();
                        contact.city = city;
                        System.out.println("Contact updated");
                    }
                    case 3 -> {
                        System.out.println("Enter new state: ");
                        state = scanner.nextLine();
                        contact.state = state;
                        System.out.println("Contact updated");
                    }
                    case 4 -> {
                        System.out.println("Enter new zip: ");
                        zip = scanner.nextLine();
                        contact.zip = zip;
                        System.out.println("Contact updated");
                    }
                    case 5 -> {
                        System.out.println("Enter new phone number: ");
                        phoneNumber = scanner.nextLine();
                        contact.phoneNumber = phoneNumber;
                        System.out.println("Contact updated");
                    }
                    default -> System.out.println("Invalid input");
                }
            }
        if(!personExists) System.out.println("Contact " + firstName + " " + lastName + " does not exist");
    }

    public void deletePerson() {
        for (int i = 0; i < contacts.size(); i++) {
            contact = contacts.get(i);
            System.out.println("Enter first name: ");
            firstName = scanner.nextLine();
            System.out.println("Enter last name: ");
            lastName = scanner.nextLine();
            if (firstName.equals(contact.firstName) && lastName.equals(contact.lastName)) {
                contacts.remove(i);
                System.out.println("Contact deleted");
            }
            if(firstName.equals(contact.firstName) && lastName.equals(contact.lastName))
                System.out.println("Contact does not exist");
        }
    }

    public void sortAlphabetically() {
        Comparator<Contact> sortingNameList = (person1 , person2) -> {
            if(person1.firstName.compareTo(person2.firstName) == 0)
                return person1.lastName.compareTo(person2.lastName);
            else
                return person1.firstName.compareTo(person2.firstName);
        };
        List<Contact> sortedNames = contacts.stream().sorted(sortingNameList).collect(Collectors.toList());
        for(Contact contact : sortedNames) contact.display();
    }

    public void sortByCityStateZip() {
        System.out.println("Sort by:\n 1.City\n 2.State\n 3.Zip ");
        choice = scanner.nextInt();
        switch (choice) {
            case 1 -> sortingByCity();
            case 2 -> sortingByState();
            case 3 -> sortingByZip();
            default -> System.out.println("Invalid input");
        }
    }

    public void sortingByCity() {
        Comparator<Contact> sortingNameList = (person1 , person2) -> person1.city.compareTo(person2.city);
        List<Contact> sortedNames = contacts.stream().sorted(sortingNameList).collect(Collectors.toList());
        for(Contact contact : sortedNames) contact.display();
    }

    public void sortingByState() {
        Comparator<Contact> sortingNameList = (person1 , person2) -> person1.state.compareTo(person2.state);
        List<Contact> sortedNames = contacts.stream().sorted(sortingNameList).collect(Collectors.toList());
        for(Contact contact : sortedNames) contact.display();
    }

    public void sortingByZip() {
        Comparator<Contact> sortingNameList = Comparator.comparing(person2 -> person2.zip);
        List<Contact> sortedNames = contacts.stream().sorted(sortingNameList).collect(Collectors.toList());
        for(Contact contact : sortedNames) contact.display();
    }

    public void viewPersonByCityOrState() {
        personExists = false;
        System.out.println("Choose:\n 1.city\n 2.State ");
        choice = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter first name: ");
        firstName = scanner.nextLine();
        System.out.println("Enter last name: ");
        lastName = scanner.nextLine();
        switch (choice) {
            case 1 -> {
                System.out.println("Enter city: ");
                city = scanner.nextLine();
                for (Map.Entry<Contact, String> person : cityPersonMap.entrySet()) {
                    if (Objects.equals(city, person.getValue())) keys.add(person.getKey());
                }
                for (Contact contactData : keys)
                    if (firstName.equals(contactData.firstName) && lastName.equals(contactData.lastName) && city.equals(contactData.city)) {
                        contactData.display();
                        personExists = true;
                    }
                if (!personExists) System.out.println("Contact does not exist");
            }
            case 2 -> {
                System.out.println("Enter state: ");
                state = scanner.nextLine();
                for (Map.Entry<Contact, String> person : statePersonMap.entrySet())
                    if (Objects.equals(state, person.getValue())) keys.add(person.getKey());
                for (Contact contactData : keys) {
                    if (firstName.equals(contactData.firstName) && lastName.equals(contactData.lastName) && state.equals(contactData.state)) {
                        contactData.display();
                        personExists = true;
                    }
                }
                if (!personExists) System.out.println("Contact does not exist");
            }
            default -> System.out.println("Invalid input");
        }
    }

    public void searchPeopleInCityOrState() {
        System.out.println("Choose:\n 1.city\n 2.State ");
        choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1 -> {
                System.out.println("Enter city: ");
                city = scanner.nextLine();
                viewPeople(city, cityPersonMap);
            }
            case 2 -> {
                System.out.println("Enter state: ");
                state = scanner.nextLine();
                viewPeople(state, statePersonMap);
            }
            default -> System.out.println("Invalid input");
        }
    }

    public void viewPeople(String cityOrState, HashMap<Contact, String> personHashMap) {
        keys.clear();
        for (Map.Entry<Contact, String> person : personHashMap.entrySet())
            if (Objects.equals(cityOrState, person.getValue())) {
                keys.add(person.getKey());
                person.getKey().display();
            }
    }

    public void countByCity() {
        System.out.println("Enter city: ");
        city = scanner.nextLine();
        long count = contacts.stream().filter(person -> person.city.equals(city)).count();
        System.out.println("The number of people in " + city + " is " + count);
    }

    public void countByState() {
        System.out.println("Enter state: ");
        state = scanner.nextLine();
        long count = contacts.stream().filter(person -> person.state.equals(state)).count();
        System.out.println("The number of people in " + state + " is " + count);
    }

    public void addContactsToCSVFile(String addressBookName) throws IOException {
        Path filePath = Paths.get("C:\\Users\\My PC\\Desktop\\Shravya" + addressBookName + ".csv");
        if (Files.notExists(filePath)) Files.createFile(filePath);
        File file = new File(String.valueOf(filePath));
        try {
            FileWriter fileWriter = new FileWriter(file);
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            List<String[]> data = new ArrayList<>();
            for(Contact contact : contacts) data.add(new String[]{contact.firstName, contact.lastName, contact.address, contact.city, contact.state, contact.phoneNumber, contact.zip});
            csvWriter.writeAll(data);
            csvWriter.close();
        }
        catch (IOException ioException) {
            ioException.getMessage();
        }
    }

    public void readContactsFromCSVFile(String addressBookName) {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader("C:\\Users\\My PC\\Desktop\\Shravya" + addressBookName + ".csv"));
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                for(String token : nextLine) System.out.println(token);
                System.out.print("\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addContactsToJSONFile(String addressBookName) throws IOException {
        Path filePath = Paths.get("C:\\Users\\My PC\\Desktop\\Shravya" + addressBookName + ".json");
        Gson gson = new Gson();
        String json = gson.toJson(contacts);
        FileWriter fileWriter = new FileWriter(String.valueOf(filePath));
        fileWriter.write(json);
        fileWriter.close();
    }

    public void readFromJSONFile(String addressBookName) throws FileNotFoundException {
        Path filePath = Paths.get("C:\\Users\\My PC\\Desktop\\Shravya" + addressBookName + ".json");
        Gson gson = new Gson();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(String.valueOf(filePath)));
        Contact[] contacts = gson.fromJson(bufferedReader, Contact[].class);
        for (Contact contact : contacts) {
            System.out.println("Firstname : " + contact.firstName);
            System.out.println("Lastname : " + contact.lastName);
            System.out.println("Address : " + contact.address);
            System.out.println("City : " + contact.city);
            System.out.println("State : " + contact.state);
            System.out.println("Zip : " + contact.zip);
            System.out.println("Phone number : " + contact.phoneNumber);
        }
    }
}