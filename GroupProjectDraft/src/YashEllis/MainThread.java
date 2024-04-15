import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainThread extends Thread {
    private int portNum;
    private ServerSocket serverSocket;

    public MainThread(int portNum) {
        this.portNum = portNum;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(portNum);
            while (true) {
//                Socket socket;
//                Thread thread = new Thread(new ClientThread("localhost", 4242));
//                thread.start();
                Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                String confirmation = reader.readLine();
                if (confirmation.equals("Connection Established")) {
                    //System.out.println("New Connection formed");
                }
            }
        } catch (IOException e) {
            return;
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public static void main(String[] args) {
        MainThread mainThread = new MainThread(4242);
        mainThread.start();
    }


}
