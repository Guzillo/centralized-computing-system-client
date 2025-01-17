import Exceptions.WrongPortNumberException;

public class CCS {

    static int amountOfArgs;
    static String argument;
    static int port;

    public static void validateArguments(String[] args) throws Exception {
        amountOfArgs = args.length;
        if (amountOfArgs != 1) {
            throw new WrongPortNumberException();
        }

        try {
            argument = args[0];
            port = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new WrongPortNumberException();
        }

        if ( !(port >= 1024 && port <= 49151) ) {
            throw new WrongPortNumberException();
        }

    }
    public static void main(String[] args) throws Exception {
        validateArguments(args);
        new UDPClient(port).start();
        new TCPClient(port).start();
    }
}
