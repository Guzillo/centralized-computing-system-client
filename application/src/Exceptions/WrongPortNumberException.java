package Exceptions;

public class WrongPortNumberException extends Exception{
    public WrongPortNumberException() {
        super("Invalid port number! Please enter a port number between 1024 and 49151.");
    }

    public WrongPortNumberException(String message) {
        super(message);
    }
}
