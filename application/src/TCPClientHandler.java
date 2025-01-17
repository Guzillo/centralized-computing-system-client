import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class TCPClientHandler extends Thread {
    private Socket clientSocket = null;
    String operator;
    int arg1;
    int arg2;
    TCPClient client;
    Statistician statistician;
    public TCPClientHandler (Socket clientSocket, TCPClient client, Statistician statistician) {
        this.clientSocket = clientSocket;
        this.client = client;
        this.statistician = statistician;
    }

    @Override
    public void run() {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                boolean areArgumentsValid = validateMathArguments(message);

                if (areArgumentsValid) {
                    String[] arguments = message.split(" ");
                    operator = arguments[0];
                    arg1 = Integer.parseInt(arguments[1]);
                    arg2 = Integer.parseInt(arguments[2]);
                    int result = processOperation(operator, arg1, arg2);
                    sentResultToClient(out, result);
                } else {
                    out.println("ERROR");
                    statistician.incrementErrorsCount();
                }
            }
        }catch (Exception ignore){}
    }
    public int processOperation(String operator, int arg1, int arg2){
        int result;
        switch (operator) {
            case "ADD":
                result = arg1 + arg2;
                break;
            case "SUB":
                result = arg1 - arg2;
                break;
            case "DIV":
                result = arg1 / arg2;
                break;
            case "MUL":
                result = arg1 * arg2;
                break;
            default:
                result = 0;
                break;
        }
        System.out.println("Operation received:    " + arg1 + " " + operator + " " + arg2 + " = " + result);
        statistician.addOperation(operator, arg1, arg2, result);
        return result;
    }

    public void sentResultToClient(PrintWriter writer, int result) {
        writer.println(result);
    }

    public boolean validateMathArguments(String arguments) {
        boolean result = false;
        String operator;
        String[] validOperators = new String[]{"ADD", "SUB", "MUL", "DIV"};
        int arg1, arg2;

        if (!Objects.equals(arguments, "")) {
            String[] splittedArguments = arguments.split(" ");
            if (splittedArguments.length != 3) {
                return false;
            }
            operator = splittedArguments[0];
            for (String element : validOperators) {
                if (Objects.equals(operator, element)) {
                    result = true;
                    break;
                }
            }

            try {
                arg1 = Integer.parseInt(splittedArguments[1]);
                arg2 = Integer.parseInt(splittedArguments[2]);
                if ( (operator.equals("DIV")) && (arg2 == 0) ){
                    result = false;
                }
            } catch (NumberFormatException e) {
                return false;
            }

            return result;
        }

        return false;
    }

}