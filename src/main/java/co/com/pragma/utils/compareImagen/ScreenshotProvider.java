package co.com.pragma.utils.compareImagen;

import net.serenitybdd.core.Serenity;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;

public class ScreenshotProvider {

    public static void takeScreenshot(String pathToSave) {
        File capture = ((TakesScreenshot) Serenity.getWebdriverManager().getCurrentDriver()).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(capture, new File(pathToSave));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
