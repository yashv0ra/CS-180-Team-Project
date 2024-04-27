import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MainThread extends Thread {
    private int portNum = 4242;
    private ServerSocket serverSocket;

    {
        try {
            serverSocket = new ServerSocket(4243);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
//                String confirmation = reader.readLine();
                Menu menu = new Menu();
                menu.setReader(reader);
                menu.setWriter(writer);
                menu.run();
//                if (confirmation.equals("Connection Established")) {
//                    System.out.println("New Connection formed");
//                }
            }
        } catch (Exception e) {
            System.out.println("some exception");
            return;
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public static void main(String[] args) throws IOException {
        MainThread mainThread = new MainThread();
        mainThread.start();
    }


}