public class AddressBookException extends Exception {

    enum ExceptionType {
        CONNECTION_FAIL, CANNOT_EXECUTE_QUERY, UPDATE_FAILED
    }

    ExceptionType exceptionType;

    public AddressBookException(String message, ExceptionType exceptionType) {
        super(message);
        this.exceptionType = exceptionType;
    }

}
