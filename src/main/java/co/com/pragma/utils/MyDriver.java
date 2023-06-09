
package co.com.pragma.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class MyDriver {
    private static WebDriver driver;

    public static MyDriver open() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/driver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        return new MyDriver();
    }
    public WebDriver page(String url){
        driver.get(url);
        return driver;
    }
}
