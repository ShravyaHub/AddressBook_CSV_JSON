public class PersonData {

    String firstName, lastName, address, city, state, email;
    int id, zip;
    long phoneNumber;

    PersonData(int id, String firstName, String lastName, String address, String city, String state, int zip, long phoneNumber, String email){

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.email = email;

    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PersonData person = (PersonData) object;
        return id == person.id && CharSequence.compare(person.address, address) == 0 && firstName.equals(person.firstName);
    }

}
