import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClient extends Thread{
    private DatagramSocket socket;
    private boolean running;
    private final byte[] buffer = new byte[256];
    final int port;

    public UDPClient(int port) throws SocketException {
        this.port = port;
        socket = new DatagramSocket(port);
        System.out.println("UDP Server is running");
    }

    public void run() {
        running = true;

        while (running) {
            try {
                DatagramPacket packet =
                        new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);
                System.out.println("Message received");
                InetAddress address = packet.getAddress();
                int packetPort = packet.getPort();

                packet = new DatagramPacket(
                        buffer, buffer.length, address, port);
                String received
                        = new String(packet.getData(), 0 , packet.getLength());
                if (received.startsWith("CCS DISCOVER")){
                    String responseMssg = "CCS FOUND";
                    byte[] responseBuffer = responseMssg.getBytes();

                    InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");

//                    TODO: generally fix broadcasting issue
//                    ?What's going on here

                    DatagramPacket responsePacket = new DatagramPacket(
                            responseBuffer, responseBuffer.length, address, packetPort
                    );

                    socket.send(responsePacket);
                    System.out.println("Message sent");
                }

            } catch (IOException e) {
                System.out.println("Corrupted package received");
            }
        }
    }
}
