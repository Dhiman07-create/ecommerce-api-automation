package utils.test_data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.gitlab4j.api.models.RepositoryFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class TestDataReader {

    public static <T> T readTestDataFromYaml(String testDataFileName, Class<T> valueType) {
        T testData;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(testDataFileName)));
            var om = new ObjectMapper(new YAMLFactory());
            testData = om.readValue(reader, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return testData;
    }

    public static String getProperty(RepositoryFile repositoryFile, String propertyName) {
        var file = new ByteArrayInputStream(repositoryFile.getDecodedContentAsBytes());
        return getPropertyFormFile(file, propertyName);
    }

    private static String getPropertyFormFile(InputStream file, String propertyName) {
        var prop = new Properties();
        try {
            prop.load(new InputStreamReader(file, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(propertyName);
    }
}