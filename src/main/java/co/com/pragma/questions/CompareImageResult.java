package co.com.pragma.questions;

import net.serenitybdd.screenplay.Question;

public class CompareImageResult {

    private CompareImageResult() {
        throw new IllegalStateException("Utility class");
    }
    public static Question<String> theResultOfComparisonIs(){
        return actor -> actor.recall("Result Key").toString();
    }

}
