package co.com.pragma.userinterfaces.reqbin;

import net.serenitybdd.screenplay.targets.Target;

public class ReqresPage {
    public static final Target GET_USER_BTN = Target.the("boton peticion get User").locatedBy("//div[contains(text(),'user list')]");
    public static final Target TRY_IT_OUT_BTN = Target.the("boton probar peticion").locatedBy("//button[contains(text(),'Try it out')]");
    public static final Target EXECUTE_BTN = Target.the("boton ejecutar peticion").locatedBy("//button[contains(text(),'Execute')]");
}
