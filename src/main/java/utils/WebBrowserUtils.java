package utils;

import com.codeborne.selenide.SelenideElement;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.Dimension;

import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

@UtilityClass

public class WebBrowserUtils {

    public static void switchToLastTab() {
        switchToLastTabDoNotResize();
        webdriver().driver().getWebDriver().manage().window().setSize(new Dimension(1920, 1080));
    }

    public static void switchToLastTabDoNotResize() {
        var windowHandles = getWebDriver().getWindowHandles();
        var lastWindow = windowHandles.toArray()[windowHandles.size() - 1].toString();
        getWebDriver().switchTo().window(lastWindow);
    }

    public static void switchToFirstTab() {
        switchTo().window(0);
    }

    public static void switchToLastTab(SelenideElement element) {
        var previousSize = getWebDriver().manage().window().getSize();
        element.scrollTo().click();
        switchToLastTab();
        webdriver().driver().getWebDriver().manage().window().setSize(new Dimension(previousSize.getWidth(), previousSize.getHeight()));
    }
}