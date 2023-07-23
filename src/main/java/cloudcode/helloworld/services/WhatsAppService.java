package cloudcode.helloworld.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import cloudcode.helloworld.configurations.*;


@Service
public class WhatsAppService {

    @Autowired
    private WhatsAppConfig apiConfig;

    private RestTemplate restTemplate;

    public WhatsAppService(){
        restTemplate = new RestTemplate();
    }

    public void sendWhatsAppMessage(String requestBody){
        String url = apiConfig.getApiUrl() + "/messages";
        System.out.println(url);        
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiConfig.getAccessToken());
        
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            int maxTries = 4;
            int doneTries = 0;
            while(doneTries != maxTries){
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,request, String.class);
                if(response.getStatusCode().is2xxSuccessful()){
                    System.out.println(response.getBody());
                    System.out.println("Message Sent Successfully");
                    break;
                }

                else{
                    System.out.println("Failed to send the message (try == "+doneTries+"):"+response.getStatusCode());
                }
            }

            
        }

        catch(RestClientException e){
            System.err.println("Error while sending message: "+ e.getLocalizedMessage());
        }
    }
}
