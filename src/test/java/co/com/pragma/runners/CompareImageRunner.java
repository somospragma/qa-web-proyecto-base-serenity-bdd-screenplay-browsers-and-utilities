package co.com.pragma.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(features = "src/test/resources/features/imagecomparison/compare_image.feature",
        glue = "co.com.pragma.stepdefinitions.imagecomparison",
        tags = "@Debug")
public class CompareImageRunner {
}
