public class Contact {

    String firstName, lastName, address, city, state, zip, phoneNumber;
    int id;

    Contact(String firstName, String lastName, String address, String city, String state, String zip, String phoneNumber){

        this.firstName=firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;

    }

    Contact(int id, String firstName, String lastName, String address, String city, String state, String zip, String phoneNumber){

       new Contact(firstName, lastName, address, city, state, zip, phoneNumber);
       this.id = id;

    }


    public void display() {

        System.out.println("First name: " + firstName + "\nLast name: " + lastName + "\nAddress: " + address + "\nCity : " + city + "\nState : " + state + "\nZip: " + zip + "\nPhone number : " + phoneNumber);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Contact contact = (Contact) object;
        return firstName == contact.firstName && CharSequence.compare(contact.address, address) == 0 && lastName.equals(contact.lastName);
    }

}
