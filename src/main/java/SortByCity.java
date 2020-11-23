import java.util.Comparator;

public class SortByCity implements Comparator<Contact> {

    @Override
    public int compare(Contact contact1, Contact contact2) {

        return (contact1.city).compareTo(contact2.city);

    }

}
