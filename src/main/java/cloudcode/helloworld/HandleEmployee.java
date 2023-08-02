package cloudcode.helloworld;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cloudcode.helloworld.services.SheetsService;
import cloudcode.helloworld.services.WhatsAppService;

@Service
public class HandleEmployee {
    @Autowired
    private SheetsService sheetsService;

    @Autowired
    private WhatsAppService whatsAppService;

    public void addEmployee(JSONObject obj){
        String spreadSheetId = "1buTF085zIPC8mC-1bjpAchhJgWQROSA2fAK3gcsBoKU";
        String range = "Sheet1!H8";
        try{
            List<List<Object>> listObjectForGettingWhereToPutData = sheetsService.getDatabaseValues(spreadSheetId, range);
            int columnToFed = Integer.parseInt(listObjectForGettingWhereToPutData.get(0).get(0).toString())+3;
            List<List<Object>> values = new ArrayList<>();
            List<Object> valuesInisdevalues = new ArrayList<>();
            valuesInisdevalues.add(obj.getString("timestamp").toString());
            valuesInisdevalues.add(obj.getJSONArray("responses").getString(0).toString());
            valuesInisdevalues.add("91"+obj.getJSONArray("responses").getString(1).toString());
            valuesInisdevalues.add(obj.getJSONArray("responses").getString(2).toString());
            valuesInisdevalues.add(obj.getString("formURL").toString());
            values.add(valuesInisdevalues);
            System.out.println(valuesInisdevalues);
            range = "Sheet1!B"+columnToFed+":F"+columnToFed;
            String response = sheetsService.setDatabaseValues(spreadSheetId, range, values);
            System.out.print(response);
        }
        catch(IOException e){
            System.out.println("Error fetching data for spreadsheet id ("+spreadSheetId+") -> "+e);
        }
        catch(GeneralSecurityException e){
            System.out.println("Error fetching data for spreadsheet id ("+spreadSheetId+") -> "+e);
        }

        String text = "New Employee named *" + obj.getJSONArray("responses").getString(0)+"* has been added to your company by you. And his/her contact number is +91"+obj.getJSONArray("responses").getString(1)+" ✅";
        String phoneNumber = "919712295489";
        String requestBody = "{\n" +
        "    \"recipient_type\": \"individual\",\n" +
        "    \"messaging_product\": \"whatsapp\",\n" +
        "    \"to\": \""+phoneNumber+"\",\n" +
        "    \"type\": \"text\",\n" +
        "    \"text\": {\n" +
        "        \"body\": \""+text+"\"\n" +
        "    }\n" +
        "}";
        whatsAppService.sendWhatsAppMessage(requestBody);
    }
}
