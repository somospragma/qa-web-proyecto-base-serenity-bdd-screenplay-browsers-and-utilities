package co.com.pragma.utils;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;


public class ScrollTop implements Interaction {

    String nivel;

    public ScrollTop (String nivel){
        this.nivel = nivel;
    }


    //hace scroll al terminar de diligenciar la informacion de la experiencia laboral
    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriver webDriver = BrowseTheWeb.as(actor).getDriver();
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;
        javascriptExecutor.executeScript(nivel);
    }

    public static ScrollTop pagina(String nivel) {
        return new ScrollTop(nivel) ;

    }
}