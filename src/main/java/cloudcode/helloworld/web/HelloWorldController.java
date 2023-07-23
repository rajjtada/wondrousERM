package cloudcode.helloworld.web;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cloudcode.helloworld.customer.HandleCustomer;

/** Defines a controller to handle HTTP requests */
@Controller
public final class HelloWorldController {
    
    @Autowired
    private HandleCustomer handleCustomer;

    
    @GetMapping("/receive_msg")
    public void sendWhatsApp(){

//         String recipentPhoneNumber = "919712295489";
//         String requestBody = "{\"recipient_type\": \"individual\",\"messaging_product\": \"whatsapp\",\"to\": \"" + recipentPhoneNumber + "\",\"type\": \"interactive\",\"interactive\": {" +
//     "\"type\": \"button\"," +
//     "\"header\": {" +
//         "\"type\": \"text\"," +
//         "\"text\": \"This is Wondrous,\"" +
//     "}," +
//     "\"body\": {" +
//         "\"text\": \"Hello 🙋‍♂️\"" +
//     "}," +
//     "\"action\": {" +
//         "\"buttons\": [" +
//             "{" +
//                 "\"type\": \"reply\"," +
//                 "\"reply\": {" +
//                     "\"id\": \"inquiryForServices\"," +
//                     "\"title\": \"Inquiry For Services\"" +
//                 "}" +
//             "}," +
//             "{" +
//                 "\"type\": \"reply\"," +
//                 "\"reply\": {" +
//                     "\"id\": \"talktoUs\"," +
//                     "\"title\": \"Talk To Us\"" +
//                 "}" +
//             "}," +
//             "{" +
//                 "\"type\": \"reply\"," +
//                 "\"reply\": {" +
//                     "\"id\": \"pastBills\"," +
//                     "\"title\": \"Past Bills\"" +
//                 "}" +
//             "}" +
//         "]" +
//     "}" +
// "}}";


//         whatsAppService.sendWhatsAppMessage(requestBody);

    }

    @PostMapping("/receive_msg")
    public ResponseEntity<String> getRawData(@RequestBody String rawData){
        System.out.println("POST DATA ->> " + rawData);

        boolean customer = true;
        if(customer){
            
           handleCustomer.processCustomer(rawData); 
        }

        return new ResponseEntity<>("Response get",HttpStatus.OK);
    }

    @PostMapping("/formDataCollection")
    public ResponseEntity<String> getCustomerInquiryResponses(@RequestBody String rawData){
        System.out.println("POST DATA CUSTOMER INQUIRY -> "+rawData);
        JSONObject obj = new JSONObject(rawData);

        //client inquiry form data collection
        if(obj.getString("typeOfData").equals("customerInquiryForm") && obj.getString("role").equals("c")){
            handleCustomer.processInquiryData(obj);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
