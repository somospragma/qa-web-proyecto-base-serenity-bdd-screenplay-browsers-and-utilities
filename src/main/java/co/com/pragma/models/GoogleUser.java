package co.com.pragma.models;

import co.com.pragma.utils.GoogleSheetsReader;
import co.com.pragma.utils.UtilConstants;
import lombok.Data;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Data
public class GoogleUser {
    private String correo;
    private String contrasena;
    private String secret;

    public GoogleUser (Integer rowNumber)  {

        String range = UtilConstants.NAME_HOJA + "!" + UtilConstants.RANGE;
        List<List<Object>> values = null;
        try {
            values = GoogleSheetsReader.read(UtilConstants.SPREADSHEET_ID,range);
            if (values == null || values.isEmpty()) {
                throw new RuntimeException("No hay datos para el inicio de sesión.");
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("No se leyo el documento, error: "+ e.getMessage());
        }
        try {
                this.correo = String.valueOf((values).get(rowNumber).get(0));
                this.contrasena = String.valueOf((values).get(rowNumber).get(1));
                this.secret = String.valueOf((values).get(rowNumber).get(2));
        }catch (Exception e){
            throw new RuntimeException("credencial(es) vacia(s), error: "+e.getMessage());
        }

    }

    public static GoogleUser ofGoogleSheetsRow(Integer rowNumber){
        return new GoogleUser(rowNumber);
    }


}
