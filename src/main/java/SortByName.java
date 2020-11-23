import java.util.Comparator;

public class SortByName implements Comparator<Contact> {

    @Override
    public int compare(Contact contact1, Contact contact2) {

        if((contact1.firstName).compareTo(contact2.firstName) == 0)
            return (contact1.lastName).compareTo(contact2.lastName);
        else
            return (contact1.firstName).compareTo(contact2.firstName);

    }

}

