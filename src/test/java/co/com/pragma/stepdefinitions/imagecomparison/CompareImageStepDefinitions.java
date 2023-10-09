package co.com.pragma.stepdefinitions.imagecomparison;

import co.com.pragma.navigation.NavigateTo;
import co.com.pragma.questions.CompareImageResult;
import co.com.pragma.tasks.CompareImage;
import co.com.pragma.tasks.GotoWebElement;
import com.github.romankh3.image.comparison.model.Rectangle;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import org.hamcrest.core.IsEqual;

import java.util.ArrayList;
import java.util.List;

import static co.com.pragma.userinterfaces.pragma.LoginPage.BOTON_IR_DE_COMPRAS;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class CompareImageStepDefinitions {

    private String actorName;
    @Before
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }
    @Given("^(.*) has a captured image")
    public void theUserHasACapturedImage(String user) {
        this.actorName = user;
        theActorCalled(actorName).entersTheScene();
        //do process to capture image

    }
    @When("he apply image comparison the {string} image with {string} image")
    public void heApplyImageComparisonTheImageWithImage(String actual, String expected) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                CompareImage.doComparison(actual,expected)
        );
    }

    @Then("he should see that the result state is {string}")
    public void heShouldSeeThatTheResultStateIs(String result) {
        theActorInTheSpotlight().should(
                seeThat("The result of Absolute Comparison Process: ", CompareImageResult.theResultOfComparisonIs(), IsEqual.equalTo(result))
        );

    }

    @When("he apply image comparison the {string} image with {string} image setting customised options")
    public void heApplyImageComparisonTheImageWithImageSettingCustomisedOptions(String actual, String expected) {
        //Create Customized parameters
        List<Rectangle> excludedAreas = new ArrayList<>(); //object to excluded areas in comparison
        excludedAreas.add(new Rectangle(100, 100, 200, 200));//an area example
        Rectangle minRectangle = new Rectangle(0,0,245,40);//a min rectangle example to comparison

        OnStage.theActorInTheSpotlight().attemptsTo(
                CompareImage.doComparison(actual, expected)
                        .configureImageComparison(imageComparison -> {  //set your customized parameters
                            imageComparison.setExcludedAreas(excludedAreas);
                            imageComparison.setDrawExcludedRectangles(true);
                            imageComparison.setMinimalRectangleSize(minRectangle.size());
                        })
        );

    }

    @When("he apply image comparison the {string} image with {string} using pixel tolerance level")
    public void heApplyImageComparisonTheImageWithUsingPixelToleranceLevel(String actual, String expected) {

        double pixelToleranceLevel = 0.01;
        OnStage.theActorInTheSpotlight().attemptsTo(
                CompareImage.doComparison(actual,expected)
                        .configureImageComparison(imageComparison -> {
                            imageComparison.setPixelToleranceLevel(pixelToleranceLevel);
                        })
        );

    }
    //********************************************************************************************
    @Given("^(.*) navigate to pragma store")
    public void theUserNavigateToPragmaStore(String user) {
        this.actorName = user;
        theActorCalled(actorName).attemptsTo(
                NavigateTo.theMisionMatchHomePage()
        );
    }

    @And("he takes a screenshot of main page")
    public void heTakesAScreenshotOfMainPage() {
        OnStage.theActorInTheSpotlight().attemptsTo(
                GotoWebElement.lookingFor(BOTON_IR_DE_COMPRAS)
        );
    }

    @When("he apply image custom comparison the {string} image with {string} image")
    public void heApplyImageCustomComparisonTheImageWithImage(String actual, String expected) {
        //Create Customized parameters
        List<Rectangle> excludedAreas = new ArrayList<>(); //object to excluded areas in comparison
        excludedAreas.add(new Rectangle(0, 471, 1900, 833));
        Rectangle minRectangle = new Rectangle(0,0,245,40);//an area example

        OnStage.theActorInTheSpotlight().attemptsTo(
                CompareImage.doComparison(actual, expected)
                        .configureImageComparison(imageComparison -> {  //set your customized parameters
                            imageComparison.setExcludedAreas(excludedAreas);
                            imageComparison.setDrawExcludedRectangles(true);
                            imageComparison.setMinimalRectangleSize(minRectangle.size());
                        })
        );

    }



}
