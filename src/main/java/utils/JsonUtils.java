package utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@UtilityClass

public class JsonUtils {

    private static final String JSON_PATH = "src/test/resources/json/";

    public static Path getJsonFilePath(String fileName) {
        return Paths.get(JSON_PATH + fileName);
    }

    @SneakyThrows
    public static String readJsonFile(String jsonName) {
        return Files.readString(getJsonFilePath(jsonName));
    }

    @Step
    @SneakyThrows
    public static String modifyJson(String file, Map jsonTemp) {
        FileReader reader = null;
        DocumentContext doc = null;
        JSONParser JsonParser = new JSONParser();
        var var1 = jsonTemp.entrySet().iterator();
        reader = new FileReader(file);
        JSONObject json = (JSONObject) JsonParser.parse(reader);
        if (json != null)
            while (var1.hasNext()) {
                Map.Entry entry = (Map.Entry) var1.next();
                doc = JsonPath.parse(json);
                doc.set((String) entry.getKey(), (String) entry.getValue());
            }
        assert doc != null;
        System.out.println(doc.jsonString());
        return doc.jsonString();
    }
}