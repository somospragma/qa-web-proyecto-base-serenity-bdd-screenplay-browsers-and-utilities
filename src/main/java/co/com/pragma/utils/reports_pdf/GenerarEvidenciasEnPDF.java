package co.com.pragma.utils.reports_pdf;


import com.itextpdf.text.DocumentException;

import java.io.IOException;

public class GenerarEvidenciasEnPDF {

    public static void main(String[] args) throws DocumentException, IOException {
        GenerarEvidenciasSerenityEnPDF.identificaJson("target/site/serenity","src/test/resources/archivos/Pragma.png");
    }

}
