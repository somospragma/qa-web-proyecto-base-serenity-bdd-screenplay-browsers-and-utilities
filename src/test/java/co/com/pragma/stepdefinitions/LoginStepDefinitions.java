package co.com.pragma.stepdefinitions;

import co.com.pragma.models.GoogleUser;
import co.com.pragma.navigation.NavigateTo;
import co.com.pragma.tasks.Login;
import co.com.pragma.userinterfaces.pragma.LoginPage;
import co.com.pragma.utils.otp.TowFactorAuthentication;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;


import static co.com.pragma.stepdefinitions.SerenityWebHocks.actor;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.*;

public class LoginStepDefinitions {

    String authenticateCode;

    @Given("que {string} esta en el Login Page de Tienda Pragma")
    public void queUsuarioEstaEnLoginPage(String actorName) {

        actor.assignName(actorName);

        actor.attemptsTo(
                NavigateTo.theMisionMatchLoginPage()
        );
    }

    @When("ingresa las credenciales del registro {int} de Google Sheets con {booleanValue}")
    public void ingreseSusCredencialesSheetnameAndRownumber(Integer rowNumber, Boolean mobileVerifyActive) {

        try {
            authenticateCode = TowFactorAuthentication.getTwoFactorCode(GoogleUser.ofGoogleSheetsRow(rowNumber).getSecret());
            System.out.println(authenticateCode);
        }catch (Exception e){
            System.out.println("error al obtener el codigo de autenticacion, error: "+ e.getMessage());
        }

        actor.attemptsTo(
                    Login.withCredentials(
                        GoogleUser.ofGoogleSheetsRow(rowNumber).getCorreo(),
                        GoogleUser.ofGoogleSheetsRow(rowNumber).getContrasena(),
                        authenticateCode,
                        mobileVerifyActive
                    )
        );

    }

    @When("ingresa las credenciales incorrectas del registro {int} de Google Sheets")
    public void ingreseSusCredencialesSheetnameAndRownumber(Integer rowNumber) {

        actor.attemptsTo(
                Click.on(LoginPage.BOTON_INICIO_SESION),
                Enter.theValue(GoogleUser.ofGoogleSheetsRow(rowNumber).getCorreo()).into(LoginPage.CAMPO_CORREO),
                Click.on(LoginPage.BOTON_SIGUIENTE)
        );

    }

    @Then("el usuario visualiza el home de Mision Match")
    public void elUsuarioVisualizaElHomeOfPage() {
        actor.attemptsTo(
                WaitUntil.the(LoginPage.ESCRITORIO_BTN, isVisible()).forNoMoreThan(15).seconds()
        );
    }

    @Then("no logra avanzar")
    public void elUsuarioNoLograAvanzar() {
        // aca algunas aserciones
    }



}
