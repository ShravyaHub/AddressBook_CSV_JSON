import com.opencsv.bean.CsvBindByName;

public class Person {

    String firstName, lastName, address, city, state, zip, phoneNumber;
    int id;

    Person(String firstName, String lastName, String address, String city, String state, String zip, String phoneNumber){

        this.firstName=firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;

    }

    Person(int id, String firstName, String lastName, String address, String city, String state, String zip, String phoneNumber){

       new Person(firstName, lastName, address, city, state, zip, phoneNumber);
       this.id = id;

    }


    public void display() {

        System.out.println("First name: " + firstName + "\nLast name: " + lastName + "\nAddress: " + address + "\nCity : " + city + "\nState : " + state + "\nZip: " + zip + "\nPhone number : " + phoneNumber);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Person person = (Person) object;
        return firstName == person.firstName && CharSequence.compare(person.address, address) == 0 && lastName.equals(person.lastName);
    }

}
