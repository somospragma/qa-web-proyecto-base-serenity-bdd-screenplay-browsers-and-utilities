package co.com.pragma.stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Arrays;
import java.util.List;


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

        String resolution = "1920,1080";
        String headless = "false";
        String driverSelected = "chrome";
        try {
                resolution = variables.getProperty("webdriver.resolution");
                if (!resolution.contentEquals(","))
                    resolution = "1920,1080";

        }catch (NullPointerException e){
            System.out.println("Resolución no indicada, se usara por defecto 1920,1080");
        }
        try {
             headless = variables.getProperty("webdriver.headless");
            if (headless.equals("true")) {
                chromeOptions.addArguments("headless");
                edgeOptions.addArguments("headless");
                firefoxOptions.addArguments("-headless");
            }
        }catch (NullPointerException e){
            System.out.println("headless no indicado, se usara por defecto FALSE");
        }
        try {
                driverSelected = variables.getProperty("webdriver.driver");
        }catch (NullPointerException e){
            System.out.println("driver no indicado, se usara por defecto Chrome");
        }

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

        switch (driverSelected.toLowerCase()) {
            case "edge":
                edgeOptions.addArguments(argsGeneral);
                driver = new EdgeDriver(edgeOptions);
                break;
            case "chrome":
                chromeOptions.addArguments(argsGeneral);
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                firefoxOptions.addArguments("--allow-origins");
                firefoxOptions.addArguments("--width=" + resolution.split(",")[0]);
                firefoxOptions.addArguments("--height=" + resolution.split(",")[1]);
                driver = new FirefoxDriver(firefoxOptions);
                break;
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
