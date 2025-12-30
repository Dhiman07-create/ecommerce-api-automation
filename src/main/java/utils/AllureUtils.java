package utils;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
public class AllureUtils {

    public static void attachScreenshot(String screenshotName) {
        var screenshot=Selenide.screenshot (OutputType.BYTES);
        if (screenshot != null) {
            Allure.attachment(screenshotName, new ByteArrayInputStream(screenshot));
        }
    }

    public static void allureLogAttachment(String info, String localPath, String name, String fileType) {
        String path = localPath + "\\" + name + fileType;
        Path file = Paths.get(path);
        if (Files.exists(file) && Files.isReadable(file)) {
            try (InputStream textStream = new FileInputStream(file.toFile())) {
                Allure.addAttachment(info, textStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File not found or not readable.");
        }
    }
}