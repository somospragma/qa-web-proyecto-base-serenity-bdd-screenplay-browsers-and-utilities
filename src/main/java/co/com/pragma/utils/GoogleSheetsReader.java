package co.com.pragma.utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;

import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.security.GeneralSecurityException;

import java.util.Arrays;
import java.util.List;

public class GoogleSheetsReader {
    private static final String APPLICATION_NAME = "Google Sheets";

    public static List<List<Object>> read(String spreadSheetId, String range) throws GeneralSecurityException, IOException {
        Sheets sheetsService;
        sheetsService = getSheetsService();

        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadSheetId, range)
                .execute();

        return response.getValues();
    }

    public static Sheets getSheetsService() throws  IOException, GeneralSecurityException{
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),GsonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build();
    }

    private static Credential authorize () throws IOException, GeneralSecurityException{

        InputStream in = GoogleSheetsReader.class.getResourceAsStream("/credenciales.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(in));

        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("CredencialesUser")))
                .setAccessType("offline")
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

    }

}

