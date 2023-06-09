package co.com.pragma.stepdefinitions;

import co.com.pragma.tasks.InterceptarPeticion;
import co.com.pragma.userinterfaces.reqbin.ReqresPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Open;


import static co.com.pragma.stepdefinitions.SerenityWebHocks.actor;

public class InterceptarPeticionRestStepDefinitions {

    @Given("que {string} esta en el Reqbin Page")
    public void queCertificadorEstaEnElReqbinPage(String actorName) {
        actor.assignName(actorName);

        actor.wasAbleTo(
                Open.url("https://reqres.in/api-docs/")
        );


    }
    @When("Envia una peticion Rest desde el Front y la intercepta")
    public void enviaUnaPeticionRestDesdeElFrontYLaIntercepta() {

        actor.attemptsTo(
                Click.on(ReqresPage.GET_USER_BTN),
                Click.on(ReqresPage.TRY_IT_OUT_BTN),
                InterceptarPeticion.withBrowserMob()
        );

    }
    @Then("validad el response de la peticcion")
    public void validadElResponseDeLaPeticcion() {
        // Write code here that turns the phrase above into concrete actions
    }
}
