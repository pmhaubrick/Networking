import java.io.FileWriter;
import java.io.IOException;


/**
 * This class creates a new FileLogger object, which creates and keeps access to a text file, to which it can dump any important
 * information to at any time.
 * 
 * @author 170009629
 */
public class FileLogger {

    FileWriter writer;


    /**
     * This constructor function is used to give the object access to the text file used for logging. It allows the file to
     * continuously be ADDED to instead of overwritten, which tracks the full history of requests and responses (and their
     * corresponding dates and times).
     * 
     * @throws IOException If unable to create/access file (perhaps permission issues).
     */
    public FileLogger() throws IOException {

        writer = new FileWriter("logFile.txt", true);
    }


    /**
     * This method takes any string supplied to it, and appends it to the end of the logFile.
     * 
     * @param output Is the string that is desired to be logged to the file.
     * @throws IOException If cannot log to the file.
     */
    public void logToFile(String output) throws IOException {
        writer.write(output);
    }

}
