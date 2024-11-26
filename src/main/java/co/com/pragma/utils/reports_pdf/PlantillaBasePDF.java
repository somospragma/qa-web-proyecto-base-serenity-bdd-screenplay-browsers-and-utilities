package co.com.pragma.utils.reports_pdf;


import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlantillaBasePDF {
    private static final Logger LOGGER = Logger.getLogger(String.valueOf(Conversor.class));
    private final int tamanoFuente = 13;
    private String resultado;

    public PlantillaBasePDF() {
    }

    public void crearPlantillaPDF(String exitpath, ResultTest resultTest, String logoPath) throws IOException, DocumentException {
        String projectName = (new File(".")).getAbsoluteFile().getParentFile().getName();
        Font font = new Font();
        Paragraph proyecto = new Paragraph(new Chunk("Proyecto: " + projectName, new Font(FontFamily.TIMES_ROMAN, 13.0F, 1)));
        Paragraph fecha = new Paragraph(new Chunk("Fecha de Ejecución: " + this.formatoFecha(resultTest.getStartTime()), new Font(FontFamily.TIMES_ROMAN, 13.0F, 1)));
        if (resultTest.getResult().equals("SUCCESS")) {
            this.resultado = "Exitoso";
        } else {
            this.resultado = "Fallido";
        }

        String var10004 = this.resultado;
        Paragraph result = new Paragraph(new Chunk("Resultado de Ejecución: " + var10004, new Font(FontFamily.TIMES_ROMAN, 13.0F, 1)));
        var10004 = convertirAMilisegundos(resultTest.getDuration());
        Paragraph duration = new Paragraph(new Chunk("Tiempo de Ejecución: " + var10004, new Font(FontFamily.TIMES_ROMAN, 13.0F, 1)));
        var10004 = resultTest.getDriver();
        Paragraph driver = new Paragraph(new Chunk("Navegador de Ejecución: " + var10004, new Font(FontFamily.TIMES_ROMAN, 13.0F, 1)));
        font.setColor(resultTest.getResult().equals("SUCCESS") ? BaseColor.GREEN : BaseColor.RED);
        result.setFont(font);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String var10000 = System.getProperty("user.dir");
        String ExitPath = var10000 + "/src/test/resources/Reporte-" + resultTest.getName() + currentDateTime.format(formatter).replaceAll("\\s+", "-").replace(":", "-") + ".pdf";
        Document document = new Document();
        FileOutputStream archivo = new FileOutputStream(ExitPath);
        PdfWriter.getInstance(document, archivo);
        document.open();
        Image logo = Image.getInstance(logoPath);
        logo.scaleToFit(200.0F, 150.0F);
        logo.setAlignment(1);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100.0F);
        PdfPCell cell1 = new PdfPCell();
        cell1.addElement(logo);
        cell1.setBorderWidth(0.1F);
        table.addCell(cell1);
        PdfPCell cell2 = new PdfPCell();
        cell2.addElement(proyecto);
        cell2.addElement(fecha);
        cell2.addElement(duration);
        cell2.addElement(result);
        cell2.addElement(driver);
        cell2.setBorderWidth(0.1F);
        table.addCell(cell2);
        document.add(table);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(new Chunk("Descripción del Escenario : " + resultTest.getName(), new Font(FontFamily.TIMES_ROMAN, 13.0F, 1))));
        Paragraph paragraph = new Paragraph();
        document.add(Chunk.NEWLINE);
        ArrayList<ResultTest.Steps> steps = resultTest.getSteps();
        String description = null;

        for(int i = 0; i < steps.size(); ++i) {
            ResultTest.Steps step = (ResultTest.Steps)steps.get(i);
            if (!step.getStep().equals(description) && !step.getStep().contains("clave")) {
                document.add(new Paragraph(new Chunk("Descripción del Paso: ", new Font(FontFamily.TIMES_ROMAN, 13.0F, 1))));
                document.add(new Paragraph(step.getStep()));
                description = step.getStep();
            }

            if (!step.getDescription().contains("Login produccion")) {
                document.add(new Paragraph(new Chunk("Paso: ", new Font(FontFamily.TIMES_ROMAN, 13.0F, 1))));
                document.add(new Paragraph(eliminarNulos(step.getDescription())));
            }

            LOGGER.info("dato screen:" + step.getScreenshot() + ":");
            if (step.getScreenshot() != "") {
                Image imagen = Image.getInstance(exitpath + "/" + step.getScreenshot());
                imagen.scaleToFit(500.0F, 500.0F);
                imagen.setAlignment(0);
                document.add(imagen);
                document.add(new Paragraph(" "));
            }

            if (step.getError() != null && resultTest.getResult().equals("FAILURE")) {
                paragraph.add(new Chunk("Tipo de error: ", new Font(FontFamily.TIMES_ROMAN, 13.0F, 1)));
                paragraph.add(new Chunk(step.getErrorType() + "\n", new Font(FontFamily.TIMES_ROMAN, 12.0F, 0, BaseColor.RED)));
                paragraph.add(new Chunk("Error: ", new Font(FontFamily.TIMES_ROMAN, 13.0F, 1)));
                paragraph.add(new Chunk(step.getError() + "\n", new Font(FontFamily.TIMES_ROMAN, 12.0F, 0, BaseColor.RED)));
            }

            if (step.getRestQuery() != "") {
                document.add(new Paragraph(new Chunk("Request body: ", new Font(FontFamily.TIMES_ROMAN, 13.0F, 1))));
                document.add(new Paragraph(step.getRestQuery() + "\n", new Font(FontFamily.TIMES_ROMAN, 12.0F, 0, BaseColor.BLACK)));
            }

            document.add(paragraph);
            document.add(new Paragraph(" "));
        }

        document.add(new Paragraph("Fecha Creación : " + now.format(formatter), new Font(FontFamily.TIMES_ROMAN, 13.0F, 1)));
        document.close();
        LOGGER.info("Archivo creado con exito");
    }

    public String formatoFecha(String fechaStr) {
        String fecha = "";

        try {
            ZonedDateTime fechaOriginal = ZonedDateTime.parse(fechaStr);
            LocalDateTime fechaLocal = fechaOriginal.toLocalDateTime();
            DateTimeFormatter formatoDeseado = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            fecha = fechaLocal.format(formatoDeseado);
        } catch (Exception var6) {
            LOGGER.info(var6.getMessage());
        }

        return fecha;
    }

    public static String convertirAMilisegundos(int tiempo) {
        double segundos = (double)tiempo / 1000.0;
        Object[] var10001;
        if (segundos > 60.0) {
            double minutos = segundos / 60.0;
            var10001 = new Object[]{minutos};
            return String.format("%.2f", var10001) + " minutos";
        } else {
            var10001 = new Object[]{segundos};
            return String.format("%.2f", var10001) + " segundos";
        }
    }

    public static String eliminarNulos(String cadena) {
        String patron = "(\\w+)=(null),?\\s?";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(cadena);
        StringBuffer sb = new StringBuffer();

        while(matcher.find()) {
            matcher.appendReplacement(sb, "");
        }

        matcher.appendTail(sb);
        return sb.toString();
    }
}

