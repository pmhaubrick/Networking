
/**
 * This class allows new 'request' objects to be created. It takes the text based request from the ConnectionHandler, and extracts
 * the relevant information, storing it appropriately. This allows the 'response' object (created next) to be able to access the
 * right information and therefore generate the correct response.
 * 
 * @author 170009629
 */
public class Request {

    private String req;
    String requestedFile;
    String requestType;
    String contentType;
    private String ct;


    /**
     * This method is the 'request' constructor. It takes the request string and stores all the important information into the
     * class's fields. This allows each new 'request' object to be unique to the given client request.
     * 
     * @param r Is the entire text that came through the socket from the client's end. It is in the format of a single string.
     */
    public Request(String r) {
        try {
            req = r;
            String[] requestLines = req.split("\n"); // Stores the supplied request into a string array, by lines.
            String[] temp = requestLines[0].split(" "); // Splits the important line (the TOP one) into its 3 components.
            requestType = temp[0].trim(); // Sets the type (ie. GET or HEAD or OPTIONS) to the type requested.
            requestedFile = temp[1].trim(); // Sets the filename to the file that is requested.
            ct = temp[1].substring(requestedFile.lastIndexOf(".") + 1); // Splits the filename requested at the "." returning
                                                                        // only the extension.
            if (ct.toLowerCase().equals("html")) {
                contentType = "text/html"; // Sets the content type to correct format for html files.
            } else if (ct.toLowerCase().equals("jpg") || ct.toLowerCase().equals("jpeg") || ct.toLowerCase().equals("png") || ct.toLowerCase().equals("gif")) {
                contentType = "image/" + ct; // Sets the content type to correct format for image files.
            }
        } catch (Exception e) {
            String[] requestLines = req.split("\n"); // Storing the previously output handler request into a string array, by
            requestType = requestLines[0].trim();
        }
    }
}
