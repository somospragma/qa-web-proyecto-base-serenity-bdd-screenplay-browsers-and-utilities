package co.com.pragma.utils;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class Wait implements Interaction {

    // Esta clase  permite asignarele tiempo
    private final int waitValue;

    public Wait(int waitValue) {
        this.waitValue = waitValue;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        try {
            Thread.sleep(waitValue * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static Wait theSeconds(int waitValue) {
        return instrumented(Wait.class, waitValue);

    }
}