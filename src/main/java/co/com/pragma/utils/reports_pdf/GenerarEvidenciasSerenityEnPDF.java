package co.com.pragma.utils.reports_pdf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.DocumentException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Logger;

public class GenerarEvidenciasSerenityEnPDF {
    private static final Logger LOGGER = Logger.getLogger(String.valueOf(Conversor.class));
    static String name = "";
    static int duration = 0;
    static String startTime = "";
    static String result = "";
    static String scenario = "";
    static String pantallazo = "";
    static String description = "";
    static String paso = "";
    static String driver;
    static String error;
    static String errorType;
    static String restQuery = "";
    static String descripcion = "description";
    static String childrenString = "children";
    static String screenshots = "screenshots";
    static String exceptionString = "exception";
    static ArrayList<ResultTest.Steps> pasos = new ArrayList<ResultTest.Steps>();

    private GenerarEvidenciasSerenityEnPDF() {
    }

    public static void identificaJson(String pathCarpetaFuenteEvidencias, String pathLogo) throws IOException, DocumentException {
        File folder = new File(pathCarpetaFuenteEvidencias);
        File[] files = folder.listFiles();
        File[] var4 = files;
        int var5 = files.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            File file = var4[var6];
            if (file.isFile() && file.getName().toLowerCase().endsWith(".json")) {
                jsonFileReader(file.getName(), pathCarpetaFuenteEvidencias, pathLogo);
            }
        }

    }

    public static void jsonFileReader(String file, String pathCarpetaFuenteEvidencias, String pathLogo) throws IOException, DocumentException {
        try {
            pasos.clear();
            String rutaArchivo = pathCarpetaFuenteEvidencias + "/" + file;
            byte[] bytes = Files.readAllBytes(Paths.get(rutaArchivo));
            String jsonString = new String(bytes);
            JSONObject jsonObject = new JSONObject(jsonString);
            name = jsonObject.getString("name");
            result = jsonObject.getString("result");
            if (jsonObject.has("context")) {
                driver = jsonObject.getString("context");
            } else if (jsonObject.has("driver")) {
                driver = jsonObject.getString("driver");
            }

            JSONArray results = jsonObject.getJSONArray("testSteps");

            for(int i = 0; i < results.length(); ++i) {
                try {
                    JSONObject testSteps = results.getJSONObject(i);
                    duration = testSteps.getInt("duration");
                    startTime = testSteps.getString("startTime");
                    scenario = testSteps.getString(descripcion);
                    if (!testSteps.has(childrenString)) {
                        extraerDatos(testSteps);
                    } else {
                        JSONArray children = testSteps.getJSONArray(childrenString);

                        for(int j = 0; j < children.length(); ++j) {
                            JSONObject children1 = children.getJSONObject(j);
                            if (!children1.has(childrenString)) {
                                extraerDatos(children1);
                            } else {
                                JSONArray childrenArray = children1.getJSONArray(childrenString);

                                for(int k = 0; k < childrenArray.length(); ++k) {
                                    JSONObject children2 = childrenArray.getJSONObject(k);
                                    if (!children2.has(childrenString)) {
                                        extraerDatos(children2);
                                    } else {
                                        JSONArray childrenArray3 = children2.optJSONArray(childrenString);

                                        for(int l = 0; l < childrenArray3.length(); ++l) {
                                            JSONObject children3 = childrenArray3.getJSONObject(l);
                                            if (children3.has(childrenString)) {
                                                JSONArray childrenArray4 = children3.optJSONArray(childrenString);

                                                for(int m = 0; m < childrenArray4.length(); ++m) {
                                                    JSONObject children4 = childrenArray4.getJSONObject(m);
                                                    extraerDatos(children4);
                                                }
                                            } else {
                                                extraerDatos(children3);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception var22) {
                    LOGGER.info(var22.getMessage());
                }
            }

            ResultTest resultado = new ResultTest(name, duration, startTime, result, scenario, driver, pasos);
            PlantillaBasePDF plantillaBasePDF = new PlantillaBasePDF();
            plantillaBasePDF.crearPlantillaPDF(pathCarpetaFuenteEvidencias, resultado, pathLogo);
        } catch (Exception var23) {
            LOGGER.info(var23.getMessage());
        }

    }

    private static void extraerDatos(JSONObject screenshotObject) {
        restQuery = "";
        errorType = null;
        error = null;
        paso = screenshotObject.getString("description");
        if (screenshotObject.has(screenshots)) {
            description = screenshotObject.getString(descripcion);
            JSONArray screenshotInterno = screenshotObject.getJSONArray(screenshots);
            if (screenshotObject.has(exceptionString)) {
                JSONObject exception = screenshotObject.getJSONObject(exceptionString);
                errorType = exception.getString("errorType");
                error = exception.getString("message");
            }

            for(int m = 0; m < screenshotInterno.length(); ++m) {
                JSONObject screenshotInterno1 = screenshotInterno.getJSONObject(m);
                pantallazo = screenshotInterno1.getString("screenshot");
                pasos.add(new ResultTest.Steps(paso, description, pantallazo, error, errorType, restQuery));
            }
        } else if (screenshotObject.has("restQuery")) {
            JSONObject service = screenshotObject.getJSONObject("restQuery");
            Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
            restQuery = gson.toJson(gson.fromJson(service.toString(), Object.class));
            pasos.add(new ResultTest.Steps(paso, description, pantallazo, error, errorType, restQuery));
        }

    }
}

