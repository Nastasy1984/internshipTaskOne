import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class ReqSenderTest {

    private static ReqSender reqSender;

    @BeforeClass
    public static void setUp() {
        reqSender = new ReqSender();
    }

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void getReqWithSuccess() {
        String getUrl = "http://192.168.129.62:8083/OCPDataServices/api/customer/history/detail/123";
        Response getResp = reqSender.getReq(getUrl);
        assertEquals("Response code is incorrect", 200, getResp.getResponseCode());
        assertTrue("Response body is incorrect", getResp.getBodyContent().contains("{\"workRequestId\":"));
    }

    @Test
    public void getReqWith404Error() {
        String getUrlWithMistake = "http://192.168.129.62:8083/OCPDataServices/api/cus123tomer/history/detail/123";
        Response getResp = reqSender.getReq(getUrlWithMistake);
        assertEquals("Response code for 404 error is incorrect", 404, getResp.getResponseCode());
        assertEquals("Response body for 404 error is incorrect", "", getResp.getBodyContent());
    }

    @Test
    public void getReqGetMalformedURLException() {
        assertNull(reqSender.getReq(""));
    }

    @Test
    public void postReqWithSuccess() {
        String postUrl = "http://192.168.129.62:8083/OCPDataServices/api/customer/search";
        //String with the example of http request's body
        String testPostBody = "{\"id\":\"54\",\n" +
                " \"parameters\":{\"firstName\":\"e\", \"lastName\":\"\", \"phoneNumber\":\"\", " +
                "\"emailAddress\":\"\", \"socialMediaHandle\":\"\"}\n}";
        Response postResp = reqSender.postReq(postUrl, testPostBody);
        assertEquals("Response code is incorrect", 200, postResp.getResponseCode());
        assertTrue("Response body is incorrect", postResp.getBodyContent().contains("\"id\":\"54\""));
    }

    @Test
    public void postReqWith404Error() {
        String postUrlWithMistake = "http://192.168.129.62:8083/OCPDataServices/api/cus123tomer/search";
        //String with the example of http request's body
        String testPostBody = "{\"id\":\"54\",\n" +
                " \"parameters\":{\"firstName\":\"e\", \"lastName\":\"\", \"phoneNumber\":\"\", " +
                "\"emailAddress\":\"\", \"socialMediaHandle\":\"\"}\n}";
        Response postResp = reqSender.postReq(postUrlWithMistake, testPostBody);
        assertEquals("Response code for 404 error is incorrect", 404, postResp.getResponseCode());
        assertEquals("Response body for 404 error is incorrect", "", postResp.getBodyContent());
    }

    @Test
    public void postReqWith400Error() {
        String postUrl = "http://192.168.129.62:8083/OCPDataServices/api/customer/search";
        Response postResp = reqSender.postReq(postUrl, "");
        assertEquals("Response code for 400 error is incorrect", 400, postResp.getResponseCode());
        assertEquals("Response body for 400 error is incorrect", "", postResp.getBodyContent());
    }

    @Test
    public void postReqGetMalformedURLException() {
        assertNull(reqSender.postReq("", ""));
    }

    @Test
    public void sendGetReq() {
        String getUrl = "http://192.168.129.62:8083/OCPDataServices/api/customer/history/detail/123";
        reqSender.sendGetReq(getUrl);
        String actual = "";
        try (BufferedReader breader = new BufferedReader(new FileReader("getReqResult.json"))){
            actual = breader.readLine();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        assertTrue("Response body was printed incorrectly", actual.contains("{\"workRequestId\":"));
    }

    @Test
    public void sendPostReq() {
        String postUrl = "http://192.168.129.62:8083/OCPDataServices/api/customer/search";
        //String with the example of http request's body
        String testPostBody = "{\"id\":\"54\",\n" +
                " \"parameters\":{\"firstName\":\"e\", \"lastName\":\"\", \"phoneNumber\":\"\", " +
                "\"emailAddress\":\"\", \"socialMediaHandle\":\"\"}\n}";
        reqSender.sendPostReq(postUrl, testPostBody);
        String actual = "";
        try (BufferedReader breader = new BufferedReader(new FileReader("postReqResult.json"))){
            actual = breader.readLine();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        System.out.println(actual);
        assertTrue("Response body was printed incorrectly", actual.contains("\"id\":\"54\""));
    }
}