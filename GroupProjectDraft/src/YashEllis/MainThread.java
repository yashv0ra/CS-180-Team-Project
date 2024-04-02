import java.io.IOException;
import java.net.ServerSocket;

public class MainThread extends Thread {
    private int portNum;
    private ServerSocket serverSocket;
    public MainThread(int portNum) {
        this.portNum = portNum;
    }
    public void run() {
        try {
            serverSocket = new ServerSocket(portNum);
        } catch (IOException e) {
            return;
        }
    }
    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
