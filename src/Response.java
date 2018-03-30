import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;


/**
 * This class is used to create a 'Response' object. On completion of creation, this object will contain the response header (and
 * body if HTML) in its 'response' field.
 * 
 * @author 170009629
 */
public class Response {

    Request request;
    String response;
    String logResponse;
    private String fileName;
    private FileInputStream fileInStream;


    /**
     * Uses the information from the 'request' object to generate the correct response.
     * 
     * @param r Is the previously created request object.
     * @param s Is the socket used for connecting to the client.
     * @throws IOException If unable load file.
     */
    public Response(Request r, Socket s) throws IOException {
        request = r;
        fileName = WebServerMain.rootPath + request.requestedFile; // Concatenates the requested file to the absolute folder path.
        File file = new File(fileName); // Opening the file using the entire root path.

        if (request.requestType.toLowerCase().equals("options")) { // This block supports an extra method for the client can use.
            response = "The request methods supported by the server are listed as follows:\n\n" // Creates a response based
                    + "GET     - Returns the file that has been requested, with the header attached.\n" // on the allowable method
                    + "HEAD    - Returns only the header (i.e. no body/file).\n" // types the client can use.
                    + "OPTIONS - Returns a list of the request methods supported by this server (i.e. this list).\r\n\r\n";
        } else {
            // The next line and the following IF statement formulate the response Header.
            response = "HTTP/1.1 200 OK\r\n" + "Server: My Java Server\r\n" + "Content-Type: " + request.contentType + "\r\n";
            if (!(request.requestType.toLowerCase().equals("head")) && !(request.requestType.toLowerCase().equals("get"))) {
                response = response.replace("200 OK", "501 Not Implemented");
            }
            try {
                fileInStream = new FileInputStream(file); // Used to stream in the requested file.
                response += "Content-Length: " + file.length() + "\r\n\r\n"; // Adds file size to header ONLY if file exists.
                logResponse = response; // Saves the header in a string for logging after the response object is created.

                if (request.contentType.toLowerCase().contains("image")) { // For images, a separate BYTES response is performed.
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream()); // Used to stream the output.
                    dos.writeBytes(response); // Writes the bytes for the header to the output stream back to the client.
                    int i;
                    while ((i = fileInStream.read()) != -1) { // Reads the image file, one byte at a time.
                        dos.write(i); // Immediately writes the byte read from the image file in-stream directly to out-stream.
                    }
                } else {
                    if (request.requestType.toLowerCase().equals("get")) { // If simply a HTML file, and also a GET request.
                        int i;
                        while ((i = fileInStream.read()) != -1) { // Loops through the HTML file.
                            response += (char) i; // Adds the HTML as the 'body' to the response string (which currently holds
                        } // only the header), character by character.
                    }
                    if (request.requestType.toLowerCase().equals("head")) { // If HTML file, but also only a HEAD request
                        int j;
                        String temp = "";
                        while ((j = fileInStream.read()) != -1) { // This block is not used to add a body (as a header doesn't have a
                            temp += (char) j; // body), but is instead to try read the file (to check it exists).
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                response = response.replace("200 OK", "404 Not Found"); // Edits response to reply with the appropriate message.
            } catch (Exception e) {
                response = response.replace("200 OK", "501 Not Implemented"); // Edits response to reply with appropriate message.
            }
        }
    }
}
