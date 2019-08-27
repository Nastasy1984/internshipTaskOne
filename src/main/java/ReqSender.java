import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//Task: create maven project, that sends get and post requests with tests and logging

public class ReqSender {
    //the interval of time to wait for the connection to the server to be established
    //or for data to be available for reading
    final static int TIME_OUT = 5000;
    private static final Logger LOG = LoggerFactory.getLogger(ReqSender.class.getName());

    public static void main(String[] args) {
        ReqSender reqSender = new ReqSender();
        String getUrl = "http://192.168.129.62:8083/OCPDataServices/api/customer/history/detail/123";
        //invoking get request
        reqSender.sendGetReq(getUrl);
        String postUrl = "http://192.168.129.62:8083/OCPDataServices/api/customer/search";
        //String with the example of http request's body
        String testPostBody = "{\"id\":\"54\",\n" +
                " \"parameters\":{\"firstName\":\"e\", \"lastName\":\"\", \"phoneNumber\":\"\", " +
                "\"emailAddress\":\"\", \"socialMediaHandle\":\"\"}\n" +
                "}";
        //invoking post request
        reqSender.sendPostReq(postUrl, testPostBody);
    }

    //sendGetReq method invokes get request and prints result to console and to json file
    public void sendGetReq(String getUrl){
        try {
            Response getResp = getReq(getUrl);
            getResp.printResponseToConsole();
            getResp.printResponseToFile("getReqResult.json");
        }
        catch (NullPointerException ex){
            LOG.error("Cannot get response from getReq method, caught {}", ex.getClass().getName());
            LOG.error("Stack trace {}", ex.getStackTrace());
        }
    }

    //sendPostReq method invokes post request and prints result to console and to json file
    public void sendPostReq(String postUrl, String testPostBody){
        try {
            Response postResp = postReq(postUrl, testPostBody);
            postResp.printResponseToConsole();
            postResp.printResponseToFile("postReqResult.json");
        }
        catch (NullPointerException ex){
            LOG.error("Cannot get response from postReq method, caught {}", ex.getClass().getName());
            LOG.error("Stack trace {}", ex.getStackTrace());
        }
    }

    public Response getReq(String getUrl){
        LOG.info("getReq method from {} class was invoked", ReqSender.class.getName());
        try {
            final URL url = new URL(getUrl);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            LOG.info("Connection with {} was opened", url.toString());
            con.setRequestMethod("GET");
            //adding header
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(TIME_OUT);
            con.setReadTimeout(TIME_OUT);
            //checking response code
            int code = con.getResponseCode();
            Response getResponse;
            if (code >= 400){
                getResponse = new Response(code, con.getResponseMessage(),
                        con.getHeaderFields(), "");
                LOG.error("We got {} code in response to get request", code);
                return getResponse;
            }
            //getting the input stream from the connection
            try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))){
                String tmpLine;
                StringBuffer responseContent = new StringBuffer();
                while ((tmpLine = in.readLine()) != null) {
                    responseContent.append(tmpLine);
                }
                getResponse = new Response(code, con.getResponseMessage(),
                        con.getHeaderFields(), responseContent.toString());
                LOG.info("getReq method created response with code {} and message {}",
                        con.getResponseCode(), con.getResponseMessage());
                return getResponse;
            }
            catch (IOException ex){
                LOG.error("Cannot get input stream in getReq method caught {}", ex.getClass().getName());
                LOG.error("Stack trace {}", ex.getStackTrace());
            }
            finally {
                //closing the connection
                con.disconnect();
                LOG.info("Connection with {} was closed", url.toString());
            }

        }
        catch (MalformedURLException ex) {
            //exception from url constructor
            //inherits IOException, therefore firstly we are processing MalformedURLException
            LOG.error("Cannot create url in getReq method caught {}", ex.getClass().getName());
            LOG.error("Stack trace {}", ex.getStackTrace());
        }
        catch (IOException ex){
            //exception from url.openConnection
            LOG.error("Cannot open connection in getReq method caught {}", ex.getClass().getName());
            LOG.error("Stack trace {}", ex.getStackTrace());
        }
        LOG.warn("getReq method returns null");
        return null;
    }

    public Response postReq(String postUrl, String testPostBody) {
        LOG.info("postReq method from {} class was invoked", ReqSender.class.getName());
        if (testPostBody == null){
            LOG.warn("postReq method got null as a request body therefore postReq method returns null");
            return null;
        }
        try {
            final URL url = new URL(postUrl);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            LOG.info("Connection with {} was opened", url.toString());
            con.setRequestMethod("POST");
            //adding header
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(TIME_OUT);
            con.setReadTimeout(TIME_OUT);

            //creating and sending the request's body
            con.setDoOutput(true);
            try(DataOutputStream out = new DataOutputStream(con.getOutputStream())){
                out.writeBytes(testPostBody);
                out.flush();
            }
            catch (IOException ex){
                LOG.error("Cannot get output stream in postReq method caught {}", ex.getClass().getName());
                LOG.error("Stack trace {}", ex.getStackTrace());
            }

            //checking response code
            int code = con.getResponseCode();
            Response postResponse;
            if (code >= 400){
                postResponse = new Response(code, con.getResponseMessage(),
                        con.getHeaderFields(), "");
                LOG.error("We got {} code in response to post request", code);
                return postResponse;
            }
            //getting the input stream from the connection
            try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))){
                String tmpLine;
                StringBuffer responseContent = new StringBuffer();
                while ((tmpLine = in.readLine()) != null) {
                    responseContent.append(tmpLine);
                }
                postResponse = new Response(con.getResponseCode(), con.getResponseMessage(),
                        con.getHeaderFields(), responseContent.toString());
                LOG.info("postReq method created response with code {} and message {}",
                        con.getResponseCode(), con.getResponseMessage());
                return postResponse;
            }
            catch (IOException ex){
                LOG.error("Cannot get input stream in postReq method caught {}", ex.getClass().getName());
                LOG.error("Stack trace {}", ex.getStackTrace());
            }
            finally {
                //closing the connection
                con.disconnect();
                LOG.info("Connection with {} was closed", url.toString());
            }
        }
        catch (MalformedURLException ex) {
            //exception from url constructor
            //inherits IOException, therefore firstly we are processing MalformedURLException
            LOG.error("Cannot create url in postReq method caught {} stack trace {}", ex.getClass().getName());
            LOG.error("Stack trace {}", ex.getStackTrace());
        }
        catch (IOException ex){
            //exception from url.openConnection
            LOG.error("Cannot open connection in postReq method caught {} stack trace {}", ex.getClass().getName());
            LOG.error("Stack trace {}", ex.getStackTrace());
        }
        LOG.warn("postReq method returns null");
        return null;
    }
}