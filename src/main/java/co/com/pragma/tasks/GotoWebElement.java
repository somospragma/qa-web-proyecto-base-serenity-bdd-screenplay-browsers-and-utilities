package co.com.pragma.tasks;

import co.com.pragma.utils.compareImagen.ScreenshotProvider;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;

import net.serenitybdd.screenplay.actions.Scroll;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.*;

public class GotoWebElement implements Task {



    public  Target webElement;
    private String pathToSave = "src/test/resources/data/screenshot/capture.png";

    public GotoWebElement(Target webElement) {
        this.webElement = webElement;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                WaitUntil.the(webElement,isEnabled()),
                Scroll.to(webElement)
        );
        ScreenshotProvider.takeScreenshot(pathToSave);

    }

    public static GotoWebElement lookingFor(Target webElement){
        return Tasks.instrumented(GotoWebElement.class,webElement);
    }
}
