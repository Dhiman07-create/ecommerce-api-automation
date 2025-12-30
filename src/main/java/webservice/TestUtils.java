package webservice;

import java.nio.file.Paths;
import lombok.Generated;
import org.testng.Reporter;

public final class TestUtils {

    public static String getTestName() {
        try {
            String projectName = Paths.get(System.getProperty("user.dir")).getFileName().toString();
            String testName = Reporter.getCurrentTestResult().getMethod().getMethodName();
            return String.format("%s%s", projectName, testName);
        } catch (Exception var2) {
            return "";
        }
    }

    public static boolean isTest() {
        return Reporter.getCurrentTestResult() != null;
    }

    @Generated
    private TestUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}