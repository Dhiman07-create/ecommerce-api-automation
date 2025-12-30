package utils.test_data;

import org.slf4j.helpers.Util;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import static utils.test_data.EnvUtils.getEnv;

public class LocalPropertyReader {

    private static final String TEST_DATA_PROPERTY_FILE_NAME = "test_data.properties";

    public static String getTestDataProperties(String labelName) {
        return getProperty(TEST_DATA_PROPERTY_FILE_NAME, labelName);
    }

    private static String getProperty(String fileName, String propertyName) {
        var file = getFile(fileName);
        var value = String.format(getPropertyFromFile(file, propertyName), getEnv());
        Util.report("Test data Location: " + value);
        return value;
    }

    private static String getPropertyFromFile(InputStream file, String propertyName) {
        var prop = new Properties();
        try {
            prop.load(new InputStreamReader(file, StandardCharsets.UTF_8));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop.getProperty(propertyName);
    }

    private static InputStream getFile(String fileName) {
        return LocalPropertyReader.class.getClassLoader().getResourceAsStream(fileName);
    }
}