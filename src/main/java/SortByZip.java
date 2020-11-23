import java.util.Comparator;

public class SortByZip implements Comparator<Contact> {

    @Override
    public int compare(Contact contact1, Contact contact2) {

        return (contact1.zip).compareTo(contact2.zip);

    }

}
