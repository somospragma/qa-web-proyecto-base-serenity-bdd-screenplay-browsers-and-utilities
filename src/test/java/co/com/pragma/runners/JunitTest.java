package co.com.pragma.runners;

import co.com.pragma.utils.google_apis.GmailService;
import co.com.pragma.utils.google_apis.GoogleSheetsReader;
import co.com.pragma.utils.constants.UtilConstants;
import co.com.pragma.utils.database.AppDB;
import co.com.pragma.utils.database.ConexionGestorDB;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static co.com.pragma.utils.constants.UtilConstants.*;
import static java.util.logging.Logger.getAnonymousLogger;

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
                            Matcher matcher = pattern.matcher(secondResponseBody);
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

    @Test
    public void getCodeEmailTemporalGratis(){
        String email = "presenteappqa";
        String dominio = "dayrep.com";
        String mensajeValidate = "Solicitaste restablecer tu contraseña";

        for (int i = 0; i < 3; ++i) {
            try {
                // Hacer la solicitud GET a la página que contiene los correos electrónicos
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet("https://www.emailtemporalgratis.com/inbox/"+dominio+"/"+email+"/");
                CloseableHttpResponse response = httpClient.execute(httpGet);

                // Leer la respuesta como texto
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder responseBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBody.append(line);
                }

                // Parsear el HTML para obtener el message-id del último correo
                Document document = Jsoup.parse(responseBody.toString());

                // Expresión regular para extraer los detalles de los correos electrónicos
                String regexId = "message-(\\d+)";
//            String regexDate = "<dt>Recibido:</dt>\\s*<dd>(.*?)</dd>";
                String regexDate = "<dt>Recibido:</dt>\\s*<dd>(.*?)\\s*(?:<span[^>]*>.*?</span>)?</dd>";

                Pattern patternId = Pattern.compile(regexId);
                Pattern patternDate = Pattern.compile(regexDate);

                Matcher matcherId = patternId.matcher(responseBody.toString());
                Matcher matcherDate = patternDate.matcher(responseBody.toString());

                // Variables para almacenar los detalles del último correo recibido
                String lastMessageId = null;
                String lastDateStr = null;

                // Obtener los detalles del último correo recibido
                List<String> messageIds = new ArrayList<>();
                while (matcherId.find()) {
                    String messageId = matcherId.group(1);
                    messageIds.add(messageId);
                }

                List<String> dates = new ArrayList<>();
                while (matcherDate.find()) {
                    String date = matcherDate.group(1).trim();
                    //System.out.println("fecha:" + date);
                    dates.add(date);
                }
                //System.out.println("fechas: " + dates);
                lastDateStr = dates.get(0);
                // Eliminar la parte "at" del formato de fecha del HTML
                lastDateStr = lastDateStr.replace(" at", "");
                lastDateStr =parseDate(lastDateStr);

                // Obtener la hora actual del sistema en formato "EEE, MMM dd, yyyy 'at' hh:mm a zzz"
//                LocalDateTime targetDate = LocalDateTime.now(ZoneOffset.UTC); // Obteniendo la fecha y hora actual en UTC
                LocalDateTime targetDate = LocalDateTime.of(2023, 9, 19, 20, 20);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy hh:mm a zzz", Locale.ENGLISH);

                LocalDateTime messageDate = LocalDateTime.parse(lastDateStr, formatter);

                System.out.println("Fecha actual del sistema: " + targetDate);

                // Comparar las fechas
                if (messageDate.isEqual(targetDate) || messageDate.isAfter(targetDate)) {
                    System.out.println("La fecha del HTML es mayor que la fecha actual del sistema.");

                    if (!messageIds.isEmpty()) {
                        lastMessageId = messageIds.get(0);
                        System.out.println("Id del ultimo correo: " + lastMessageId);

                        // Construir la URL del mensaje específico
                        String messageUrl = "https://www.emailtemporalgratis.com/email/dayrep.com/presenteappqa/message-" + lastMessageId + "/";
                        System.out.println("segunda URL: " + messageUrl);

                        // Hacer una nueva solicitud GET para obtener el contenido del mensaje
                        HttpGet messageGet = new HttpGet(messageUrl);
                        CloseableHttpResponse messageResponse = httpClient.execute(messageGet);
                        BufferedReader messageReader = new BufferedReader(new InputStreamReader(messageResponse.getEntity().getContent()));
                        StringBuilder messageBody = new StringBuilder();
                        String messageLine;
                        while ((messageLine = messageReader.readLine()) != null) {
                            messageBody.append(messageLine);
                        }
                        //Validar texto dentro del mensaje
                        Pattern pattern = Pattern.compile(mensajeValidate);
                        Matcher matcher = pattern.matcher(messageBody.toString());
                        if (matcher.find()) {
                            System.out.println("El mensaje si contiene el texto a validar");
                        } else {
                            System.out.println("No se pudo encontrar el mensaje esperado en la respuesta del correo.");
                        }

                        // Parsear el contenido del mensaje para extraer el código OTP
                        Document messageDocument = Jsoup.parse(messageBody.toString());
                        //System.out.println("segundo response: " + messageDocument);
                        Element codeElement = messageDocument.select("span").first();

                        if (codeElement != null) {
                            String otp = codeElement.text();
                            System.out.println("Código OTP: " + otp);
                            break;
                        } else {
                            System.out.println("No se encontró ningún código OTP en el mensaje.");
                        }
                    } else {
                        System.out.println("No se encontraron Message IDs.");
                    }
                } else {
                    System.out.println("La fecha del HTML es anterior a la fecha actual del sistema.");
                }

                // Cerrar recursos
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void borrar() {
        int attempts = 2;
        HttpResponse<String> secondResponse = null;

        //obtener codigo OTP
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://codapi.online/sms-api/get-messages"))
                .header("token", "ea8f1636-5103-52a0-bc91-b860cc860bd2")
                .build();

        HttpResponse<String> response = null;

        for (int i = 0; i < attempts; ++i) {

            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JSONArray json = new JSONArray(response.body());
                JSONObject firstObject = json.getJSONObject(0);
                String id = firstObject.getString("id");
                String code = firstObject.getString("code");
                String date = firstObject.getString("date");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime messageDate = LocalDateTime.parse(date, formatter);

                System.out.println("hora del mensaje: " + messageDate);

//                LocalDateTime targetDate =  LocalDateTime.now(ZoneId.of("GMT-4"));

                // Instancia de LocalDateTime en UTC
                LocalDateTime localDateTimeUtc = LocalDateTime.now(ZoneOffset.UTC).withSecond(0).withNano(0);
                // Zona horaria GMT-4 (AST)
                ZoneId zonaHorariaAst = ZoneId.of("GMT-4");
                // Convierte LocalDateTime en ZonedDateTime en UTC
                ZonedDateTime zonedDateTimeUtc = ZonedDateTime.of(localDateTimeUtc, ZoneOffset.UTC);
                // Cambia la zona horaria a GMT-4 (AST)
                ZonedDateTime zonedDateTimeAst = zonedDateTimeUtc.withZoneSameInstant(zonaHorariaAst);
                // Obtiene el LocalDateTime en GMT-4
                LocalDateTime targetDate = zonedDateTimeAst.toLocalDateTime();

                System.out.println("Hora objetivo: " + targetDate);

                if (messageDate.isEqual(targetDate) || messageDate.isAfter(targetDate)) {
                    break;
                }

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }


    private String parseDate(String dateIn){

        // Definir el patrón de formato de hora y minutos
        Pattern timePattern = Pattern.compile("(\\d{1,2}):(\\d{2}) (AM|PM)");
        Matcher matcher = timePattern.matcher(dateIn);

        if (matcher.find()) {
            // Obtener la hora y los minutos encontrados
            String hourStr = matcher.group(1);
            String minuteStr = matcher.group(2);
            String amPm = matcher.group(3);

            // Convertir a números y agregar cero a la izquierda si es necesario
            int hour = Integer.parseInt(hourStr);
            if (hour < 10) {
                hourStr = "0" + hourStr;
            }

            // Corregir la cadena de fecha y hora
            String correctedDateTime = dateIn.replaceFirst("\\d{1,2}:\\d{2} (AM|PM)", hourStr + ":" + minuteStr + " " + amPm);
            return correctedDateTime;
        } else {
            return null;
        }
    }

}
