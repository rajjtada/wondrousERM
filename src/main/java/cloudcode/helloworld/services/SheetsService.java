package cloudcode.helloworld.services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class SheetsService {
    private static final String APPLICATION_NAME = "WONDROUS APPLICATION";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    // private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String SERVICE_ACCOUNT_JSON_PATH = "/credentials.json";


    private static Sheets sheetsService;

    public static Sheets getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        
        if (sheetsService == null) {
            //HttpTransport httpTransport = new NetHttpTransport();
            GoogleCredentials credentials = ServiceAccountCredentials.fromStream(
                    SheetsService.class.getResourceAsStream(SERVICE_ACCOUNT_JSON_PATH))
                    .createScoped(SCOPES);
            sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }
        return sheetsService;
  }

//   public static void main(String... args) throws IOException, GeneralSecurityException {
//         // Build a new authorized API client service.
//         final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//         final String spreadsheetId = "1vX0vp8hPNeWH6Woliy5immp8Ec9QvYvE0UJi_i2ZXXU";
//         String range = "Sheet1!F6";
//         // Sheets service =
//         //     new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//         //         .setApplicationName(APPLICATION_NAME)
//         //         .build();

//         // //this particular code is for reading the values of particular range
//         ValueRange response = getCredentials(HTTP_TRANSPORT).spreadsheets().values()
//             .get(spreadsheetId, range)
//             .execute();
//         List<List<Object>> values = response.getValues();
//         System.out.println("DATA GOT FROM EXCEL -> "+ values);
//         int columntofed = 0;
//         if (values == null || values.isEmpty()) {
//             System.out.println("No data found.");
//         } 
//         else {
//         //     System.out.println("Name, Major");
//             // for (List row : values) {
//             //     // Print columns A and E, which correspond to indices 0 and 4.
//             //     System.out.println(row.get(0));
//             // }
//             columntofed = Integer.parseInt(values.get(0).get(0).toString())+3;
//             System.out.println(columntofed);
//         }

//         //this particular code is for writing into single range
//         try {
//             List<List<Object>> values1 = new ArrayList<>();
//             List<Object> valuesin = new ArrayList<>();
//             valuesin.add("raj");
//             valuesin.add("9899999999");
//             values1.add(valuesin);
//             range = "Sheet1!B"+columntofed+":C"+columntofed;
//             // Updates the values in the specified range.
//             ValueRange body = new ValueRange()
//                 .setValues(values1);
//             UpdateValuesResponse result = getCredentials(HTTP_TRANSPORT).spreadsheets().values().update(spreadsheetId, range, body)
//                 .setValueInputOption("USER_ENTERED")
//                 .execute();
//             System.out.printf("%d cells updated.", result.getUpdatedCells());
//         } 
//         catch (GoogleJsonResponseException e) {
//             // TODO(developer) - handle error appropriately
//             GoogleJsonError error = e.getDetails();
//             if (error.getCode() == 404) {
//                 System.out.printf("Spreadsheet not found with id '%s'.\n", spreadsheetId);
//             } else {
//                 throw e;
//             }
//         }
//     }

    public List<List<Object>> getDatabaseValues(String spreadSheetId, String range) throws IOException, GeneralSecurityException{
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        
        ValueRange response = getCredentials(HTTP_TRANSPORT).spreadsheets().values()
        .get(spreadSheetId, range)
        .execute();

        return response.getValues();
    }

    public String setDatabaseValues(String spreadSheetId, String range, List<List<Object>> values) throws IOException, GeneralSecurityException{
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        
        try{
            ValueRange body = new ValueRange()
                .setValues(values);
            UpdateValuesResponse result = getCredentials(HTTP_TRANSPORT).spreadsheets().values().update(spreadSheetId, range, body)
                .setValueInputOption("USER_ENTERED")
                .execute();
            return "Cells Updated -> "+result.getUpdatedCells()+" of spreadsheet id ("+spreadSheetId+")";
        }
        catch(GoogleJsonResponseException e){
            GoogleJsonError error = e.getDetails();

            if(error.getCode() == 404){
                return "Spreadsheet not found -> "+spreadSheetId;
            } 
        }
        return "";
        
    }
}
