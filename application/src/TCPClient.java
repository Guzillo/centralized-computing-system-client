import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPClient extends Thread{
    int port;
    ServerSocket serverSocket;
    Socket clientSocket;
    Statistician statistician;

    public TCPClient(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
        System.out.println("TCP server is running");
        statistician = new Statistician();
        statistician.start();
    }

    public void run() {
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                new TCPClientHandler(clientSocket, this, statistician).start();
                statistician.incrementClientsCount();
            } catch (Exception ignored) {}

        }

    }

}
