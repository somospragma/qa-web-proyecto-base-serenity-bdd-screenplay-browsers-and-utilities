package co.com.pragma.navigation;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.*;

public class NavigateTo {

    public static Performable theMisionMatchHomePage() {
        return Task.where("{0} opens the Mision Match home page",
                Open.browserOn().thePageNamed("home.page")
        );
    }

    public static Performable theMisionMatchLoginPage() {
        return Task.where("{0} opens the Mision Match login page",
                Open.browserOn().thePageNamed("login.page")
        );
    }

    public static Performable theMisionMatchSolicitudesPage() {
        return Task.where("{0} opens the Mision Match solicitudes page",
                Open.browserOn().thePageNamed("solicitudes.page")
        );
    }

    public static Performable theMisionMatchCrearSolicitudesPage() {
        return Task.where("{0} opens the Mision Match crear solicitud page",
                Open.browserOn().thePageNamed("crear.solicitudes.page")
        );
    }

    public static Performable theMisionMatchDetalleSolicitudPage(String pageName, String parameter) {
        return Task.where("{0} opens the Mision Match detalle de solicitud page",
                OpenPageWithParameter.open(pageName,parameter)
        );
    }
}
