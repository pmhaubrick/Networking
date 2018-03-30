import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * This is the main class for the server. I contains the starting (entry) point, main method. It will be used to take some
 * arguments and use them to set up the server. It will then wait and listen for new requests the the socket port.
 * 
 * @author 170009629
 */
public class WebServerMain {

    private static ServerSocket ss;
    private static int portNo;
    static String rootPath;
    static int threadCount = 0;
    private static final int MAX_THREAD_LIMIT = 5;


    /**
     * This is the main method. It is kept concise and short, allowing the other methods and classes to provide the functionality.
     * 
     * @param args Contains 2 command line arguments. args[0] is the entire (absolute) path to the folder where the requested
     *            files are saved. args[1] is the specified port number, to ensure the server is set up to listen to the port that
     *            the client connects to.
     */
    public static void main(String[] args) {
        if (args.length == 2) { // This block makes sure that the program will ONLY run when both required command line arguments
                                // are provided.
            rootPath = args[0];
            portNo = Integer.parseInt(args[1]);
            new WebServerMain().startServer(); // Calls a method in the main class, which is used to start the server.
        } else {
            System.out.println("Usage: java WebServerMain <document_root> <port>"); // The error message output when CL arguments
                                                                                    // are invalid or missing. It specifies the
                                                                                    // format required.
        }
    }


    /**
     * Used to start the server at a certain port number and accept all client requests when the current thread count is below a
     * specified threshold.
     */
    public void startServer() {
        try {
            ss = new ServerSocket(portNo); // Opens a new serversocket for the client to connect to.
            while (true) { // While is used for multiple requests, keeping the server ready for a new one, without terminating.
                if (threadCount < MAX_THREAD_LIMIT - 1) { // Only allows new thread to be spun if current thread count is under limit.
                    Socket s = ss.accept(); // Socket is used for client connection, needed for streams later.
                    ConnectionHandler conHan = new ConnectionHandler(s); // Creates a new ConnectionHandler for each new thread.
                    conHan.start(); // Starts the run method for this new ConnectionHandler thread.
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
