package co.com.pragma.navigation;

import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;


import net.serenitybdd.screenplay.actions.UnknownPageException;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;


public class OpenPageWithParameter implements Interaction {

    private final String pageName;
    private final String parameter;
    private EnvironmentVariables environmentVariables;

    public OpenPageWithParameter(String pageName, String parameter) {
        this.parameter = parameter;
        this.pageName = pageName;
        this.environmentVariables = SystemEnvironmentVariables.currentEnvironmentVariables();
    }

    public static OpenPageWithParameter open(String pageName, String parameter){
        return new OpenPageWithParameter(pageName,parameter);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String pageUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getOptionalProperty(pageName)
                .orElse(environmentVariables.getProperty(pageName));
        if (pageUrl != null) {
            pageUrl=pageUrl.substring(0,pageUrl.indexOf("#"))+parameter;
        }else {
            throw new UnknownPageException("No page defined with the name '" + pageUrl + "'");
        }
        BrowseTheWeb.as(actor).getDriver().get(pageUrl);
    }

}
