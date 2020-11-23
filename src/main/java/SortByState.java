import java.util.Comparator;

public class SortByState implements Comparator<Contact> {

    @Override
    public int compare(Contact contact1, Contact contact2) {

        return (contact1.state).compareTo(contact2.state);

    }

}
