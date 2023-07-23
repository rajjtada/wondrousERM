package cloudcode.helloworld.customer;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cloudcode.helloworld.services.SheetsService;
import cloudcode.helloworld.services.WhatsAppService;

@Service
public class HandleCustomer {
    @Autowired
    private WhatsAppService whatsAppService;

    @Autowired
    private SheetsService sheetsService;
    
    public void processCustomer(String jsonData){
        JSONObject obj = new JSONObject(jsonData);


        String phoneNumber = !obj.getJSONArray("entry")
                                    .getJSONObject(0)
                                    .getJSONArray("changes")
                                    .getJSONObject(0)
                                    .getJSONObject("value")
                                    .isNull("contacts")?obj.getJSONArray("entry")
                                                            .getJSONObject(0)
                                                            .getJSONArray("changes")
                                                            .getJSONObject(0)
                                                            .getJSONObject("value")
                                                            .getJSONArray("contacts")
                                                            .getJSONObject(0)
                                                            .getString("wa_id"):null;

        String userName = !obj.getJSONArray("entry")
                                .getJSONObject(0)
                                .getJSONArray("changes")
                                .getJSONObject(0)
                                .getJSONObject("value")
                                .isNull("contacts")?obj.getJSONArray("entry")
                                                        .getJSONObject(0)
                                                        .getJSONArray("changes")
                                                        .getJSONObject(0)
                                                        .getJSONObject("value")
                                                        .getJSONArray("contacts")
                                                        .getJSONObject(0)
                                                        .getJSONObject("profile")
                                                        .getString("name"):null;
        
        // entrying new user into the database
        if(userName != null && phoneNumber != null){

            String spreadSheetId = "1vX0vp8hPNeWH6Woliy5immp8Ec9QvYvE0UJi_i2ZXXU";
            String range = "Sheet1!F6";
            try{
                List<List<Object>> listObjectForGettingWhereToPutData = sheetsService.getDatabaseValues(spreadSheetId, range);
                int columnToFed = Integer.parseInt(listObjectForGettingWhereToPutData.get(0).get(0).toString())+3;
                List<List<Object>> values = new ArrayList<>();
                List<Object> valuesInisdevalues = new ArrayList<>();
                valuesInisdevalues.add(userName.toString());
                valuesInisdevalues.add(phoneNumber.toString());
                values.add(valuesInisdevalues);
                System.out.println(valuesInisdevalues);
                range = "Sheet1!B"+columnToFed+":C"+columnToFed;
                String response = sheetsService.setDatabaseValues(spreadSheetId, range, values);
                System.out.println(response);
            }
            catch(IOException e){
                System.out.println("Error fetching data for spreadsheet id ("+spreadSheetId+") -> "+e);
            }
            catch(GeneralSecurityException e){
                System.out.println("Error fetching data for spreadsheet id ("+spreadSheetId+") -> "+e);
            }                            
        }

        try{
            if(!obj.getJSONArray("entry")
                    .getJSONObject(0)
                    .getJSONArray("changes")
                    .getJSONObject(0)
                    .getJSONObject("value")
                    .isNull("messages")){
                
                if(!obj.getJSONArray("entry")
                        .getJSONObject(0)
                        .getJSONArray("changes")
                        .getJSONObject(0)
                        .getJSONObject("value")
                        .getJSONArray("messages")
                        .getJSONObject(0)
                        .isNull("interactive")){

                    if(obj.getJSONArray("entry")
                            .getJSONObject(0)
                            .getJSONArray("changes")
                            .getJSONObject(0)
                            .getJSONObject("value")
                            .getJSONArray("messages")
                            .getJSONObject(0)
                            .getJSONObject("interactive")
                            .getJSONObject("button_reply")
                            .getString("id").equals("inquiryForServices")){
                            //send google inquiry form(customer) for this purpose
                            
                            String requestBody = "{" +
                            "\"recipient_type\": \"individual\"," +
                            "\"to\": \"" + phoneNumber + "\"," +
                            "\"type\": \"text\"," +
                            "\"messaging_product\": \"whatsapp\"," + // Added messaging_product field
                            "\"text\": {" +
                            "    \"body\": \"Fill out this form ASAP! Our agent will contact you soon 👇😊\\nhttps://forms.gle/ZcKBwXhpEvJgqbjS6\\n\\n*DON'T WORRY LINK IS SECURE!*\\n*YOUR DETAILS ARE SAFE WITH US!*\"" +
                            "  }" +
                            "}";
                            whatsAppService.sendWhatsAppMessage(requestBody);
                        
                    }

                    else if(obj.getJSONArray("entry")
                            .getJSONObject(0)
                            .getJSONArray("changes")
                            .getJSONObject(0)
                            .getJSONObject("value")
                            .getJSONArray("messages")
                            .getJSONObject(0)
                            .getJSONObject("interactive")
                            .getJSONObject("button_reply")
                            .getString("id").equals("talktoUs")){
                            //send talk to us message 
                            String requestBody ="{" +
                            "'messaging_product': 'whatsapp'," +
                            "'to': '" + phoneNumber + "'," +
                            "'type': 'template'," +
                            "'template': {" +
                            "    'name': '3rd_cust_contactus'," +
                            "    'language': {" +
                            "        'code': 'en_US'" +
                            "    }" +
                            "  }" +
                            "}";; 
                            whatsAppService.sendWhatsAppMessage(requestBody);
                    }

                    else if(obj.getJSONArray("entry")
                            .getJSONObject(0)
                            .getJSONArray("changes")
                            .getJSONObject(0)
                            .getJSONObject("value")
                            .getJSONArray("messages")
                            .getJSONObject(0)
                            .getJSONObject("interactive")
                            .getJSONObject("button_reply")
                            .getString("id").equals("pastBills")){
                            //send past bills(implementation is left allover)
                            String requestBody = "{" +
                            "'messaging_product': 'whatsapp'," +
                            "'to': '" + phoneNumber + "'," +
                            "'type': 'template'," +
                            "'template': {" +
                            "    'name': '4th_cust_pastbills'," +
                            "    'language': {" +
                            "        'code': 'en_US'" +
                            "    }" +
                            "  }" +
                            "}";
                           whatsAppService.sendWhatsAppMessage(requestBody);
                            
                    }  
                }

                //if user messages first time (add user into the database(remaining))
                else if (!obj.getJSONArray("entry")
                            .getJSONObject(0)
                            .getJSONArray("changes")
                            .getJSONObject(0)
                            .getJSONObject("value")
                            .getJSONArray("messages")
                            .getJSONObject(0)
                            .isNull("text")){

                        String requestBody = "{\"recipient_type\": \"individual\",\"messaging_product\": \"whatsapp\",\"to\": \"" + phoneNumber + "\",\"type\": \"interactive\",\"interactive\": {" +
                        "\"type\": \"button\"," +
                        "\"header\": {" +
                            "\"type\": \"text\"," +
                            "\"text\": \"This is Wondrous,\"" +
                        "}," +
                        "\"body\": {" +
                            "\"text\": \"Hello 🙋‍♂️\"" +
                        "}," +
                        "\"action\": {" +
                            "\"buttons\": [" +
                                "{" +
                                    "\"type\": \"reply\"," +
                                    "\"reply\": {" +
                                        "\"id\": \"inquiryForServices\"," +
                                        "\"title\": \"Inquiry For Services\"" +
                                    "}" +
                                "}," +
                                "{" +
                                    "\"type\": \"reply\"," +
                                    "\"reply\": {" +
                                        "\"id\": \"talktoUs\"," +
                                        "\"title\": \"Talk To Us\"" +
                                    "}" +
                                "}," +
                                "{" +
                                    "\"type\": \"reply\"," +
                                    "\"reply\": {" +
                                        "\"id\": \"pastBills\"," +
                                        "\"title\": \"Past Bills\"" +
                                    "}" +
                                "}" +
                            "]" +
                        "}" +
                    "}}";
                        whatsAppService.sendWhatsAppMessage(requestBody);                        
                }
            }
        }
        catch(JSONException e){
            System.out.println("Error in handling the customer JSON DATA(JSON EXCEPTION) -> "+e);
        }
    }

    public void processInquiryData(JSONObject obj) {
        // {"timestamp":"2023-07-23T12:22:28.111Z","responses":["fhdsjf","9712295489","sdljfh@dhgj","hello"],"typeOfData":"customerInquiryForm","role":"c"}
        String spreadSheetId = "1P1gz52x7J0Yt-eSBSVWDaY6r5NDdLYUqiRZdgZ-0Ld8";
        String range = "Sheet 1!J8";
        try{
            List<List<Object>> listObjectForGettingWhereToPutData = sheetsService.getDatabaseValues(spreadSheetId, range);
            int columnToFed = Integer.parseInt(listObjectForGettingWhereToPutData.get(0).get(0).toString())+3;
            List<List<Object>> values = new ArrayList<>();
            List<Object> valuesInisdevalues = new ArrayList<>();
            valuesInisdevalues.add(obj.getString("timestamp"));
            valuesInisdevalues.add(obj.getJSONArray("responses").get(0));
            valuesInisdevalues.add(obj.getJSONArray("responses").get(1));
            valuesInisdevalues.add(obj.getJSONArray("responses").get(2));
            valuesInisdevalues.add(obj.getJSONArray("responses").get(3));
            values.add(valuesInisdevalues);
            System.out.println(valuesInisdevalues);
            range = "Sheet 1!B"+columnToFed+":F"+columnToFed;
            String response = sheetsService.setDatabaseValues(spreadSheetId, range, values);
            System.out.println(response);
        }
        catch(IOException e){
            System.out.println("Error fetching data for spreadsheet id ("+spreadSheetId+") -> "+e);
        }
        catch(GeneralSecurityException e){
            System.out.println("Error fetching data for spreadsheet id ("+spreadSheetId+") -> "+e);
        }
    }
    
}

