import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientThread extends Thread {
    private ServerSocket serverSocket;
    private String host;
    private int port;
    private Socket socket;
    public ClientThread(String host, int port, ServerSocket serverSocket) {
        this.host = host;
        this.port = port;
        this.serverSocket = serverSocket;
    }
    public void run() {
        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            return;
        }
    }
    public Socket getSocket() {
        return socket;
    }
}
