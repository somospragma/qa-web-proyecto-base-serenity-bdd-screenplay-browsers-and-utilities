package co.com.pragma.tasks;

import co.com.pragma.userinterfaces.reqbin.ReqresPage;
import lombok.SneakyThrows;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.List;

import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getProxiedDriver;

public class InterceptarPeticion implements Task {

    public static InterceptarPeticion withBrowserMob(){
        return new InterceptarPeticion();
    }

    @SneakyThrows
    @Override
    public <T extends Actor> void performAs(T actor) {
        // Configuración del proxy
        // start the proxy
        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.start(0);
        // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

        // get the Selenium proxy object
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
//        String hostIp = null;
//        try {
//            hostIp = Inet4Address.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            throw new RuntimeException(e);
//        }
//        seleniumProxy.setHttpProxy(hostIp + ":" + proxy.getPort());
//        seleniumProxy.setSslProxy(hostIp + ":" + proxy.getPort());

        // configure it as a desired capability
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

        // Seteo del proxy al driver
        ChromeDriver driver = (ChromeDriver) getProxiedDriver();
        driver.getCapabilities().merge(capabilities);



        // create a new HAR with the label "example"
        proxy.newHar("reqres.com");

        //TODO - Se abre en este punto el navegador segun el ejemplo

        // Hacer clic en algún elemento de la página que realiza la petición
        Click.on(ReqresPage.EXECUTE_BTN);

        Har har = proxy.getHar();
        File harFile = new File("reqres.har");
        har.writeTo(harFile);
        List<HarEntry> entries = har.getLog().getEntries();
        String requestUrl = entries.get(entries.size() - 1).getRequest().getUrl();
        System.out.println(requestUrl);


    }
}
