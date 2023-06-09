package co.com.pragma.tasks;

import co.com.pragma.userinterfaces.pragma.LoginPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;


public class Login implements Task {

    private final String correo;
    private final String contrasena;
    private final String codigo;

    private  final Boolean mobileVerifyActive;

    public Login (String correo, String contrasena, String codigo, Boolean mobileVerifyActive){
        this.correo = correo;
        this.contrasena = contrasena;
        this.codigo = codigo;
        this.mobileVerifyActive = mobileVerifyActive;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        actor.attemptsTo(
                Click.on(LoginPage.BOTON_INICIO_SESION),
                Enter.theValue(correo).into(LoginPage.CAMPO_CORREO),
                Click.on(LoginPage.BOTON_SIGUIENTE),
                WaitUntil.the(LoginPage.CAMPO_CONTRASENIA, isVisible()).forNoMoreThan(30).seconds(),
                Enter.theValue(contrasena).into(LoginPage.CAMPO_CONTRASENIA),
                Click.on(LoginPage.BOTON_SIGUIENTE)
        );
        if (Boolean.TRUE.equals(mobileVerifyActive)) {
            actor.attemptsTo(
                    Click.on(LoginPage.OPCION_PROBAR_OTRA_MANERA),
                    Click.on(LoginPage.OPCION_CODIGO_AUTENTTICATOR),
                    WaitUntil.the(LoginPage.CAMPO_INGRESO_CODIGO, isVisible()).forNoMoreThan(30).seconds(),
                    Enter.theValue(codigo).into(LoginPage.CAMPO_INGRESO_CODIGO),
                    Click.on(LoginPage.BOTON_SIGUIENTE_INGRESAR)
            );
        }else {
            actor.attemptsTo(
                    WaitUntil.the(LoginPage.CAMPO_INGRESO_CODIGO, isVisible()).forNoMoreThan(30).seconds(),
                    Enter.theValue(codigo).into(LoginPage.CAMPO_INGRESO_CODIGO),
                    Click.on(LoginPage.BOTON_SIGUIENTE_INGRESAR)
            );
        }
    }

    public static Login withCredentials(String correo, String contrasena, String codigo, Boolean mobileVerifyActive)  {
        return Tasks.instrumented(Login.class, correo, contrasena,codigo, mobileVerifyActive);
    }



}
