import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

//class for creating http response, printing it to console and to file
public class Response {
    private int responseCode;
    private String responseMessage;
    private Map<String, List<String>> HeadersMap;
    private String bodyContent;
    private static final Logger LOG = LoggerFactory.getLogger(Response.class.getName());

    public Response(int responseCode, String responseMessage, Map<String, List<String>> map, String bodyContent) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.HeadersMap = map;
        if (bodyContent != null) {
            this.bodyContent = bodyContent;
        }
        else{
            this.bodyContent = "";
        }
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getBodyContent() {
        return bodyContent;
    }

    public void printResponseToConsole(){
        LOG.info("printResponseToConsole method from {} class was invoked", Response.class.getName());
        System.out.println("Response Code: " + responseCode);
        System.out.println("Response Message: " + responseMessage);
        System.out.println("Response Headers: ");
        for (Map.Entry<String, List<String>> entry : HeadersMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("Response body: ");
        System.out.println(bodyContent);
        System.out.println();
    }

    public void printResponseToFile(String fileName){
        LOG.info("printResponseToFile method from {} class was invoked", Response.class.getName());
        if (fileName == null){
            LOG.error("printResponseToFile method got null as a name of file for writing response");
            return;
        }
        if (fileName.equals("")){
            LOG.error("printResponseToFile method got empty string as a name of file for writing response");
            return;
        }
        try (FileWriter writer = new FileWriter(fileName)){
            writer.write(bodyContent.toString());
            writer.flush();
        } catch (IOException ex) {
            LOG.error("Cannot create file writer in printResponseToFile method, caught {} stack trace {}", ex.getClass().getName(), ex.getStackTrace());
            LOG.error("Stack trace {}",  ex.getStackTrace());
            ex.printStackTrace();
        }
    }
}
