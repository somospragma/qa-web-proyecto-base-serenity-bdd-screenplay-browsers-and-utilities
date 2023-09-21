package co.com.pragma.runners;

import co.com.pragma.utils.GmailService;
import co.com.pragma.utils.GoogleSheetsReader;
import co.com.pragma.utils.UtilConstants;
import co.com.pragma.utils.data.AppDB;
import co.com.pragma.utils.data.ConexionGestorDB;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static co.com.pragma.utils.UtilConstants.*;

public class JunitTest {

    @Test
    public void connectionBDTest() throws SQLException {
        Connection connection = ConexionGestorDB.util().crearConexionMySql(MYSQL_URL,MYSQL_USER,MYSQL_PASSWORD);
        ResultSet resultSet = AppDB.executeSelect("SELECT * FROM search_values WHERE id='1'",connection);
        HashMap<String,String> hashMap = (HashMap<String, String>) AppDB.fillHashWithResultSetRecord(resultSet);
        System.out.println(hashMap);
        ConexionGestorDB.util().closeConnection(connection);
    }

    @Test
    public void urlDBEnviroments() {
        System.out.println(MYSQL_URL);
    }

    @Test
    public void getGmailMessages() throws GeneralSecurityException, IOException, ParseException {
        String DATE_FORMAT = "yyyy-MM-dd HH:mm";
        String SPECIFIED_DATE_STR = "2023-09-12 11:00";
        String MESSAGE_TO_VALIDATE = "no comparta";

        Gmail service = GmailService.getGmailService();
        List<Message> messages = GmailService.getLastMessage(service);
        if (messages != null && messages.size() > 0) {
            Message lastMessage = messages.get(0);
            Date lastMessageDate = GmailService.getLastMessageDate(service, lastMessage);
            System.out.println("La fecha del ultimo mensaje es: " + lastMessageDate);
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Date specifiedDate = sdf.parse(SPECIFIED_DATE_STR);
            if (lastMessageDate.after(specifiedDate)) {
                GmailService.checkIfContainsTxt(service, lastMessage, MESSAGE_TO_VALIDATE);
            } else {
                System.out.println("El ultimo mensaje es anterior a la fecha especificada.");
            }
        } else {
            System.out.println("No se encontraron mensajes.");
        }
    }

    @Test
    public void getDataSheet() throws GeneralSecurityException, IOException {
        String range = UtilConstants.NAME_HOJA + "!" + UtilConstants.RANGE;
        List values = null;
        try {
            values = GoogleSheetsReader.read((String)UtilConstants.SPREADSHEET_ID, (String)range);
            if (values == null || values.isEmpty()) {
                throw new RuntimeException("No hay datos en el documento.");
            }
        }
        catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("No se leyo el documento, error: " + e.getMessage());
        }
        try {
            String valueToSearch = String.valueOf(((List)values.get(0)).get(0));
            System.out.println("Valor leido de tabla: " + valueToSearch);
        }
        catch (Exception e) {
            throw new RuntimeException("registro(s) vacio(s), error: " + e.getMessage());
        }
    }

    private String verificationCode;
    @Test
    public void getCodeOfMailCorreoTemporal() {
        String email = "presente-pragma-app@cikue.com";
        int attempts = 10;
        int waitAttemptSeconds = 3;
        LocalDateTime targetDate = LocalDateTime.now(ZoneOffset.UTC);
        AtomicInteger bandera = new AtomicInteger();
        bandera.set(0);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api-tm.solucioneswc.com/api/get-messages/accepted/" + email + "/18000?password=pragma")).header("X-Tm-Token", "JQffcDVgzfrPa9ZPALch").build();
        for (int i = 0; i < attempts; ++i) {
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).thenAccept(responseBody -> {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    JSONArray events = json.getJSONArray("the_events");
                    JSONObject lastEvent = events.getJSONObject(events.length() - 1);
                    String messageId = lastEvent.getString("message_id");
                    Logger.getAnonymousLogger().log(Level.INFO, "El message_id del \u00faltimo mensaje es: " + messageId);
                    String dateStr = lastEvent.getString("date");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
                    LocalDateTime messageDate = LocalDateTime.parse(dateStr, formatter);
                    if (messageDate.isEqual(targetDate) || messageDate.isAfter(targetDate)) {
                        Logger.getAnonymousLogger().log(Level.INFO, "La fecha del mensaje es mayor o igual a la fecha objetivo");
                        HttpRequest secondRequest = HttpRequest.newBuilder().uri(URI.create("https://api-tm.solucioneswc.com/api/message/" + email + "/" + messageId)).header("X-Tm-Token", "JQffcDVgzfrPa9ZPALch").build();
                        client.sendAsync(secondRequest, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).thenAccept(secondResponseBody -> {
                            Pattern pattern = Pattern.compile("<span style=\"color:black;font-size: 40px;background-color: #e5e5e5;padding: 5px 2px;\">(\\d+)</span>");
                            Matcher matcher = pattern.matcher((CharSequence)secondResponseBody);
                            if (matcher.find()) {
                                verificationCode = matcher.group(1);
                                Logger.getAnonymousLogger().log(Level.INFO, "El c\u00f3digo de verificaci\u00f3n es: " + verificationCode);
                            } else {
                                Logger.getAnonymousLogger().log(Level.SEVERE, "No se pudo encontrar el c\u00f3digo de verificaci\u00f3n en la respuesta.");
                            }
                        }).join();
                        bandera.set(1);
                    } else {
                        Logger.getAnonymousLogger().log(Level.INFO, "La fecha del mensaje es anterior a la fecha objetivo. Intentando nuevamente en 3 segundos...");
                        Thread.sleep((long)waitAttemptSeconds * 1000L);
                    }
                }
                catch (JSONException e) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, "No se pudo parsear la respuesta como JSON: " + e.getMessage());
                }
                catch (DateTimeParseException e) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, "No se pudo parsear la fecha: " + e.getMessage());
                }
                catch (InterruptedException e) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, "Se interrumpi\u00f3 el sue\u00f1o del hilo: " + e.getMessage());
                }
            }).join();
            if (bandera.get() == 1) break;
        }
        Logger.getAnonymousLogger().log(Level.INFO, "El c\u00f3digo de verificaci\u00f3n a la salida es: " + verificationCode);
    }

    @Test
    public void getFecha() {
        ZoneId zoneId = ZoneId.of("America/Bogota");
        LocalDate fechaActual = LocalDate.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
        String fechaFormateada = fechaActual.format(formatter);
        System.out.println("La fecha actual en Colombia es: " + fechaFormateada);
    }

    @Test
    public void validateMessageCorreoTemporal() {
        String mensaje = "Solicitaste restablecer tu contrase\u00f1a";
        String email = "presente-pragma-app@cikue.com";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api-tm.solucioneswc.com/api/get-messages/accepted/" + email + "/18000?password=pragma")).header("X-Tm-Token", "JQffcDVgzfrPa9ZPALch").build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).thenAccept(responseBody -> {
            try {
                JSONObject json = new JSONObject(responseBody);
                JSONArray events = json.getJSONArray("the_events");
                JSONObject lastEvent = events.getJSONObject(events.length() - 1);
                String messageId = lastEvent.getString("message_id");
                System.out.println("El message_id del \u00faltimo mensaje es: " + messageId);
                String dateStr = lastEvent.getString("date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
                LocalDateTime messageDate = LocalDateTime.parse(dateStr, formatter);
                LocalDateTime targetDate = LocalDateTime.of(2023, 9, 19, 20, 20);
                System.out.println(targetDate);
                for (int i = 0; i < 3; ++i) {
                    if (messageDate.isEqual(targetDate) || messageDate.isAfter(targetDate)) {
                        System.out.println("La fecha del mensaje es mayor o igual a la fecha objetivo");
                        HttpRequest secondRequest = HttpRequest.newBuilder().uri(URI.create("https://api-tm.solucioneswc.com/api/message/" + email + "/" + messageId)).header("X-Tm-Token", "JQffcDVgzfrPa9ZPALch").build();
                        client.sendAsync(secondRequest, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).thenAccept(secondResponseBody -> {
                            Pattern pattern = Pattern.compile(mensaje);
                            Matcher matcher = pattern.matcher((CharSequence)secondResponseBody);
                            if (matcher.find()) {
                                System.out.println("SI CONTINE EL MENSAJE");
                            } else {
                                System.err.println("No se pudo encontrar el c\u00f3digo de verificaci\u00f3n en la respuesta.");
                            }
                        }).join();
                        break;
                    }
                    System.out.println("La fecha del mensaje es anterior a la fecha objetivo. Intentando nuevamente en 3 segundos...");
                    Thread.sleep(3000L);
                }
            }
            catch (JSONException e) {
                System.err.println("No se pudo parsear la respuesta como JSON: " + e.getMessage());
            }
            catch (DateTimeParseException e) {
                System.err.println("No se pudo parsear la fecha: " + e.getMessage());
            }
            catch (InterruptedException e) {
                System.err.println("Se interrumpi\u00f3 el sue\u00f1o del hilo: " + e.getMessage());
            }
        }).join();
    }

}
