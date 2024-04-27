import javax.swing.*;
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
        BufferedReader reader;
        PrintWriter writer;
        try {
            //socket = serverSocket.accept();
            try {
                this.socket = new Socket("localhost", 4243);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer.write("Connection Established");
            writer.println();
            writer.flush();
        } catch (Exception e) {
            System.out.println("Some exception");
            return;
        }
        try {
            String callUserGUI = null;
                callUserGUI = reader.readLine();

            if(callUserGUI.equals("Call UserGUI")) {
                SwingUtilities.invokeLater(new UserGUI(reader, writer));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public Socket getSocket() {
        return socket;
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new ClientThread("localhost", 4243));
        thread.start();
    }

}


