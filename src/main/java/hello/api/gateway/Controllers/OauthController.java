package hello.api.gateway.Controllers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import hello.api.gateway.GateWay;
import hello.api.gateway.models.AccessTokenApi;
import hello.api.gateway.models.OauthApp;
import hello.api.gateway.models.OauthToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Iterator;
import java.util.Map;

import static hello.api.gateway.GateWay.*;

@RestController
@CrossOrigin
@RequestMapping("/api-gateway")
public class OauthController {

    private static final Logger logger = LoggerFactory.getLogger(OauthController.class);


    static final String CLIENT_ID="9aa3ae11759ae257e2f29484a32820845ae59763";
    static final String CLIENT_SECRET="3997673d7814bbbcde139fe181e7fba723beb70a4e6e49363230ff78051f40d1";
    public String access_token;

    @Autowired
    private OauthController oauth;

    public boolean OauthCheckToken(String token)
    {

        if(token==null)
            return false;
        else {
            try {
                String token2 = token.replace("Bearer ", "");

                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ACCESS_TOKEN_VALIDATE_URI)
                        .queryParam("token", token2);
                RestTemplate restTemplate = new RestTemplate();
                String result = restTemplate.getForObject(builder.toUriString(), String.class);

                JsonFactory factory = new JsonFactory();

                ObjectMapper mapper = new ObjectMapper(factory);
                JsonNode rootNode = mapper.readTree(result);

                Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();

                while (fieldsIterator.hasNext()) {

                    Map.Entry<String, JsonNode> field = fieldsIterator.next();

                    System.out.println(field.getKey());
                    if (field.getKey() == "error") {
                        return false;
                    }

                }
                return  true;
            } catch (Exception e) {
                logger.error("oauth", e);
                return  false;
            }
        }
    }

    public String OauthGetToken()
    {

        try {

            RestTemplate restTemplate = new RestTemplate();

            String url = ACCESS_TOKEN_URI;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


            MultiValueMap<String, String> map =
                    new LinkedMultiValueMap<String, String>();
            map.add("client_id",CLIENT_ID);
            map.add("client_secret",CLIENT_SECRET);
            map.add("grant_type","client_credentials");


            HttpEntity<MultiValueMap<String, String>> entity =
                    new HttpEntity<MultiValueMap<String, String>>(map, headers);

            AccessTokenApi response= restTemplate.postForObject(url, entity, AccessTokenApi.class);
            System.out.println("user vce norm"+response);


            return response.getAccess_token();
        } catch (Exception e) {
            logger.error("user.createError", e);
            return null;
        }

    }

    @PostMapping("/oauth20/applications")
    public ResponseEntity<String> registrationApplic(@RequestHeader(value="Authorization",required = false) String token, @RequestBody OauthApp requestDetails) {
        try {

            RestTemplate restTemplate = new RestTemplate();

            String url = APPLICATION_URI;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            String requestJson =  ow.writeValueAsString(requestDetails);
            System.out.println("jsoon"+requestJson);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);

            String response= restTemplate.postForObject(url, entity, String.class);
            System.out.println("user vce norm"+response);

            //put method

//            RestTemplate restTemplate2 = new RestTemplate();
//
//            HttpHeaders headers2 = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<String> requestEntity2 = new HttpEntity<String>("{\"status\":\"1\"}", headers2);
//            restTemplate2.exchange(url, HttpMethod.PUT, requestEntity2, String.class);

            return new ResponseEntity(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("user.createError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/oauth20/authorize")
    public ResponseEntity<String> getCode(@RequestHeader(value="Authorization",required = false) String token,
                                          @RequestParam String client_id, @RequestParam String response_type, @RequestParam String redirect_uri) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(AUTH_CODE_URI)
                    .queryParam("client_id", client_id).queryParam("response_type", response_type).queryParam("redirect_uri", redirect_uri);

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(builder.toUriString(), String.class);

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("user.getError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/oauth20/tokens/validate")
    public ResponseEntity<String> validToken(
            @RequestParam String token) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ACCESS_TOKEN_VALIDATE_URI)
                    .queryParam("token", token);

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(builder.toUriString(), String.class);
            System.out.println(result);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("user.getError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/oauth20/tokens")
    public ResponseEntity<String> getToken(@RequestHeader(value="Authorization",required = false) String token,
                                           @RequestBody OauthToken requestDetails) {
        try {

            RestTemplate restTemplate = new RestTemplate();

            String url = ACCESS_TOKEN_URI;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


            MultiValueMap<String, String> map =
                    new LinkedMultiValueMap<String, String>();
            map.add("client_id",requestDetails.getClient_id());
            map.add("client_secret",requestDetails.getClient_secret());
            map.add("grant_type",requestDetails.getGrant_type());
            if(requestDetails.getCode()!=null)
                map.add("code",requestDetails.getCode());
            map.add("redirect_uri",requestDetails.getRedirect_uri());
            if(requestDetails.getRefresh_token()!=null)
                map.add("refresh_token",requestDetails.getRefresh_token());

            HttpEntity<MultiValueMap<String, String>> entity =
                    new HttpEntity<MultiValueMap<String, String>>(map, headers);

            String response= restTemplate.postForObject(url, entity, String.class);
            System.out.println("user vce norm"+response);


            return new ResponseEntity(response,HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("user.createError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
