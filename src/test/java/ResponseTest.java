import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ResponseTest {
//creating test table of headers
    private static List<String> tmpList;
    private static Map<String, List<String>> testHeadersMap;
    private static String fileName;

    @BeforeClass
    public static void setUp() {
        tmpList = new ArrayList<>();
        tmpList.add("333");
        testHeadersMap = new HashMap<>();
        testHeadersMap.put("555", tmpList);
        fileName = "test123.json";
    }

    @Test
    public void printResponseToFile() {
        //creating test response
        Response response = new Response(200, "111", testHeadersMap, "222");
        response.printResponseToFile(fileName);
        String actual = "";
        try (BufferedReader breader = new BufferedReader(new FileReader(fileName))){
            actual = breader.readLine();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        assertEquals("Wrong response's body content was printed", "222", actual);
    }

    @Test
    public void getBodyContentWithNullBody() {
        Response response = new Response(200, "111", testHeadersMap, null);
        assertEquals("Wrong response's body content was printed", "", response.getBodyContent());
    }
}