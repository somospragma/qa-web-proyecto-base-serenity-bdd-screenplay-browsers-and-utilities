package co.com.pragma.userinterfaces.pragma;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;


public class LoginPage extends PageObject {

 public static final Target BOTON_INICIO_SESION = Target.the("Botón para iniciar sesion con google").located(By.xpath("//a[contains(text(),'Google')]"));
 public static final Target CAMPO_CORREO = Target.the("Campo para ingresar el correo electronico con el que se iniciará sesión").located(By.id("identifierId"));
 public static final Target BOTON_SIGUIENTE = Target.the("Boton para continuar a ingresar la contraseña").located(By.xpath("//span[.='Siguiente']"));
 public static final Target CAMPO_CONTRASENIA = Target.the("Campo para ingresar la contraseña").located(By.xpath("//input[@type='password']"));
 public static final Target OPCION_PROBAR_OTRA_MANERA = Target.the("Boton para realizar la verificación de la cuenta de otra manera").located(By.xpath("/html/body/div[1]/div[1]/div[2]/div/div[2]/div/div/div[2]/div/div[2]/div[2]/div[2]/div/div/button/span"));
 public static final Target OPCION_CODIGO_AUTENTTICATOR = Target.the("Boton para seleccionar la opción de recibir codigo por google autenticator").located(By.xpath("/html/body/div[1]/div[1]/div[2]/div/div[2]/div/div/div[2]/div/div[1]/div/form/span/section/div/div/div[1]/ul/li[3]/div/div[2]"));
 public static final Target CAMPO_INGRESO_CODIGO= Target.the("Campo para ingresar el código de verificación").located(By.xpath("//*[@id=\"totpPin\"]"));
 public static final Target BOTON_SIGUIENTE_INGRESAR = Target.the("Boton para validar el código e ingresar").located(By.xpath("/html/body/div[1]/div[1]/div[2]/div/div[2]/div/div/div[2]/div/div[2]/div/div[1]/div/div/button/span"));

 public static final Target ESCRITORIO_BTN = Target.the("Titulo mapa de crecimiento que se encuentra al ingresar a la página").located(By.xpath("//a[contains(text(),'Escritorio')]"));

 public static final Target BOTON_IR_DE_COMPRAS = Target.the("Boton para ir de compras").locatedBy("//*[text()='Ir de compras']");

}
