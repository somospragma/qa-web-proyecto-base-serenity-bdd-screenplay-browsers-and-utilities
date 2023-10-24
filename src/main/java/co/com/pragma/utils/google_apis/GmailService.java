package co.com.pragma.utils.google_apis;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GmailService {
    private static final String APPLICATION_NAME = "Google Gmail";
    private static final String USER_ID = "me";
    private static final String QUERY = "is:unread";

    public static List<Message> readMessageList() throws GeneralSecurityException, IOException {
        Gmail service = GmailService.getGmailService();
        ListMessagesResponse listResponse = service.users().messages().list(USER_ID).execute();
        List<Message> messages = listResponse.getMessages();
        if (messages.isEmpty()) {
            System.out.println("No messages found.");
        } else {
            System.out.println("Messages:");
            for (Message message : messages) {
                System.out.println("ID: " + message.getId());
            }
        }
        return messages;
    }

    public static List<Message> getLastMessage(Gmail service) throws IOException {
        return service.users().messages().list(USER_ID).setQ(QUERY).setMaxResults(1L).execute().getMessages();
    }

    public static Date getLastMessageDate(Gmail service, Message message) throws IOException {
        Message fullMessage = service.users().messages().get(USER_ID, message.getId()).execute();
        List<MessagePartHeader> headers = fullMessage.getPayload().getHeaders();
        for (MessagePartHeader header : headers) {
            if (!header.getName().equals("Date")) continue;
            String dateStr = header.getValue();
            return GmailService.parseDate(dateStr);
        }
        return null;
    }

    public static void checkIfContainsTxt(Gmail service, Message message, String validate) throws IOException {
        Message fullMessage = service.users().messages().get(USER_ID, message.getId()).execute();
        MessagePart payload = fullMessage.getPayload();
        List<String> bodies = GmailService.getBodies(payload);
        int count = 0;
        for (String body : bodies) {
            if (body != null && body.contains(validate)) {
                System.out.println("La parte " + count + " del mensaje contiene el texto '" + validate + "'");
            } else {
                System.out.println("La parte " + count + " del mensaje no contiene el texto '" + validate + "'");
            }
            ++count;
        }
    }

    public static Gmail getGmailService() throws IOException, GeneralSecurityException {
        Credential credential = GmailService.authorize();
        if (credential.getAccessToken() == null) {
            credential.refreshToken();
        }
        return new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build();
    }

    private static Date parseDate(String dateStr) {
        String[] dateFormats = {
                "EEE, dd MMM yyyy HH:mm:ss Z", // For dates with timezone offset
                "EEE, dd MMM yyyy HH:mm:ss 'GMT'", // For dates with 'GMT'
        };
        for (String dateFormat : dateFormats) {
            try {
                return new SimpleDateFormat(dateFormat, Locale.US).parse(dateStr);
            }
            catch (ParseException parseException) {
                // Ignore and try the next format
            }
        }
        System.err.println("No se pudo analizar la fecha: " + dateStr);
        return null;
    }

    private static List<String> getBodies(MessagePart payload) {
        ArrayList<String> bodies = new ArrayList<>();
        int bandera = 0;
        if (payload.getParts() != null) {
            for (MessagePart part : payload.getParts()) {
                if (part.getParts() != null) {
                    bandera=1;
                    for (MessagePart segmentedPart : part.getParts()) {
                        if (segmentedPart.getBody().getSize() >= 0) {
                            String body = new String(Base64.getUrlDecoder().decode(segmentedPart.getBody().getData()));
                            bodies.add(body);
                        }
                    }
                }
            }
            if(bandera==0){
                for (MessagePart part : payload.getParts()) {
                    if (part.getBody().getSize() >= 0) {
                        String body = new String(Base64.getUrlDecoder().decode(part.getBody().getData()));
                        bodies.add(body);
                    }
                }
            }
        }
        return bodies;
    }

    private static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream in = GmailService.class.getResourceAsStream("/credenciales_correo_pruebas-pragma.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(in));
        List<String> scopes = List.of("https://mail.google.com/");
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    clientSecrets,
                    scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new File("CredencialesUserGmail")))
                .setAccessType("offline").build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
}