package co.com.pragma.stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Arrays;
import java.util.List;

import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getProxiedDriver;


public class SerenityWebHocks {

    public static Actor actor;
    WebDriver driver;

    @Before(order = 1)
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
        EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();

        ChromeOptions chromeOptions = new ChromeOptions();
        EdgeOptions edgeOptions = new EdgeOptions();
        FirefoxOptions firefoxOptions = new FirefoxOptions();

        String resolution = variables.getProperty("webdriver.resolution");

        if (resolution == null || !resolution.contentEquals(","))
            resolution = "1920,1080";

        List<String> argsGeneral = Arrays.asList(
                "window-size=" + resolution,
                "test-type",
                "no-sandbox",
                "lang=es",
                "disable-popup-blocking",
                "disable-download-notification",
                "ignore-certificate-errors",
                "allow-running-insecure-content",
                "disable-translate",
                "disable-dev-shm-usage",
                "always-authorize-plugins",
                "disable-extensions",
                "remote-allow-origins=*");

        if (variables.getProperty("webdriver.headless").equals("true")) {
            chromeOptions.addArguments("headless");
            edgeOptions.addArguments("headless");
            firefoxOptions.addArguments("-headless");
        }

        switch (variables.getProperty("webdriver.driver").toLowerCase()) {
            case "edge":
                edgeOptions.addArguments(argsGeneral);
                driver = new EdgeDriver(edgeOptions);
                break;
            case "chrome":
                chromeOptions.addArguments(argsGeneral);
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                // firefoxOptions.addArguments(argsGeneral);
                firefoxOptions.addArguments("--allow-origins");
                firefoxOptions.addArguments("--width=" + resolution.split(",")[0]);
                firefoxOptions.addArguments("--height=" + resolution.split(",")[1]);

                driver = new FirefoxDriver(firefoxOptions);
                break;
            default:
                chromeOptions.addArguments(argsGeneral);
                driver = new ChromeDriver(chromeOptions);
        }
    }


    @Before(order = 2)
    public void setTheActor(){
        actor = Actor.named("pragma");
        actor.can(BrowseTheWeb.with(driver));
    }

    @ParameterType(value = "true|True|TRUE|false|False|FALSE")
    public Boolean booleanValue(String value) {
        return Boolean.valueOf(value);
    }

}
