import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * This class is used to handle every connection (which have the requests), and extends Thread to inherit the ability to spin up
 * new threads.
 * 
 * @author 170009629
 */
public class ConnectionHandler extends Thread {

    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private FileLogger logger;


    /**
     * This constructor function is used to create/initiate some new objects that are used later in this class.
     * 
     * @param s Is simply the socket that was passed to it after accepting the request from the client.
     * @throws IOException If unable to create the BufferedReade/PrintWriter.
     */
    public ConnectionHandler(Socket s) throws IOException {
        socket = s;
        logger = new FileLogger(); // This is created to allow access to the logging file and a method for logging to it.
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream())); // This is for reading in the client request.
            pw = new PrintWriter(socket.getOutputStream()); // Used to send response to client when dealing with HTML or headers.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Used to override run in the Thread Class, and give a response for the request.
     */
    public void run() {
        WebServerMain.threadCount++; // Adds to the thread count as another has been created.
        String clientRequest = "";
        try {
            while (br.ready() || clientRequest.length() == 0) {
                clientRequest += (char) br.read(); // Used to save the request to a string, character by character.
            }
            synchFileLogger(getTime()); // Gives the date and time to the logger object to be saved to the log file.
            System.out.println(clientRequest); // Printing the ConnectionHandler info received from the client.
            synchFileLogger("|----CLIENT REQUEST----|\n\n" + clientRequest); // Gives the request to the logger to be logged.
            Request request = new Request(clientRequest); // Creates a new request object, which extracts the key information.
            Response response = new Response(request, socket); // Creates object which uses request info to generate response.
            pw.write(response.response); // Sends the response string back to client (used for HTML/headers, not images).
            synchFileLogger("|----SERVER RESPONSE----|\n\n" + response.logResponse + "\n\n\n"); // Gives response to the logger.
            pw.close();
            logger.writer.close();
            br.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        WebServerMain.threadCount--; // Decrements the thread count as the current thread is about to be terminated.
        return;
    }


    /**
     * Finds out the current date and time. Used mainly for logging purposes.
     * 
     * @return String containing the current date and time.
     */
    public String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        Date date = new Date();
        return ("|||___DATE/TIME OF CLIENT REQUEST:  " + dateFormat.format(date) + "___|||\n\n");
    }


    /**
     * Used to synchronise threads. They all have a common logging file that they share, so to ensure that there are never more
     * than one thread trying to access and log info to this file at any one time, this 'middle-man' synchronised method is used.
     * 
     * @param logText Is the info desired to be logged, in the form of a string.
     * @throws IOException If not able to log to file.
     */
    public synchronized void synchFileLogger(String logText) throws IOException {
        logger.logToFile(logText);
    }

}
