import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientThread extends Thread {
    private String host;
    private int port;
    private Socket socket;
    public ClientThread(String host, int port) {
        this.host = host;
        this.port = port;
    }
    public void run() {
        try {
            //socket = serverSocket.accept();
            try {
                this.socket = new Socket(host, port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer.write("Connection Established");
            writer.flush();
            Menu menu = new Menu();
            menu.runClient(reader, writer);
        } catch (Exception e) {
            return;
        }
    }
    public Socket getSocket() {
        return socket;
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new ClientThread("localhost", 4242));
        thread.start();
    }

}



