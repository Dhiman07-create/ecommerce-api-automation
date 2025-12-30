package base;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;


public class BaseTest {
    protected static final String BASE = "https://dummyjson.com";


    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
