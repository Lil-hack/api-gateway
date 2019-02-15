package hello.api.gateway.Controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import hello.api.gateway.GateWay;
import hello.api.gateway.config.RequestError;
import hello.api.gateway.config.RequestErrorRepository;
import hello.api.gateway.models.ErrorCodes;
import hello.api.gateway.models.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static hello.api.gateway.GateWay.*;

@RestController
@CrossOrigin
@RequestMapping("/api-gateway")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    //----------------------------------USERS API----------------------------------------

    @Autowired
    private OauthController oauth;

    @Autowired
    private RequestErrorRepository requestRepos;

    @PostMapping("/user.create")
    public ResponseEntity registrationUser(@RequestHeader(value="Authorization",required = false) String token, @RequestBody UserInfo requestUserDetails) {
        try {
            if(oauth.access_token==null)
                oauth.access_token=oauth.OauthGetToken();

            RestTemplate restTemplate = new RestTemplate();

            String url = URL_API_USERS_REGISTR;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            String requestJson =  ow.writeValueAsString(requestUserDetails);
            System.out.println("jsoon"+requestJson);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer "+oauth.access_token);
            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
            UUID uuid =   restTemplate.postForObject(url, entity, UUID.class);
            requestUserDetails.setUid(uuid);
            System.out.println("user vce norm");
            ObjectWriter ow2 = new ObjectMapper().writer().withDefaultPrettyPrinter();
            UserInfo usermmy=new UserInfo();
            usermmy.setUid(uuid);
            usermmy.setVk(requestUserDetails.getVk());
            String requestJson2 =  ow2.writeValueAsString(usermmy);
            RestTemplate restTemplate2 = new RestTemplate();

            String url2 = URL_API_STATISTIC_CREATE_STAT;
            HttpHeaders headers2 = new HttpHeaders();
            headers2.setContentType(MediaType.APPLICATION_JSON);
            headers2.set("Authorization","Bearer "+oauth.access_token);
            HttpEntity<String> entity2 = new HttpEntity<String>(requestJson2,headers2);
            restTemplate2.postForObject(url2, entity2, String.class);
            System.out.println("stat vce norm");

            System.out.println("jsoon2"+requestJson2);
            RestTemplate restTemplate3 = new RestTemplate();

            String url3 = URL_API_STATONLINE_CREATE_STAT;
            HttpHeaders headers3 = new HttpHeaders();
            headers3.setContentType(MediaType.APPLICATION_JSON);
            headers3.set("Authorization","Bearer "+oauth.access_token);
            HttpEntity<String> entity3 = new HttpEntity<String>(requestJson2,headers3);
            restTemplate3.postForObject(url3, entity3, String.class);
            System.out.println(" vce norm end");
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("user.createError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_USER.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user.get{uuid}")
    public ResponseEntity getUser(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        if(oauth.OauthCheckToken(token)==false)
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(oauth.access_token==null)
            oauth.access_token=oauth.OauthGetToken();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_USERS_GET)
                    .queryParam("uuid", uuid);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+oauth.access_token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            String result =
                    restTemplate.exchange(
                            builder.toUriString(), HttpMethod.GET, entity, String.class).getBody();
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (HttpServerErrorException e) {
            logger.error("user.getError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_USER.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user.getAll")
    public ResponseEntity getUserAll(@RequestHeader(value="Authorization",required = false) String token) {
        if(oauth.OauthCheckToken(token)==false) {
            return new ResponseEntity(ErrorCodes.ERROR_401.error(), HttpStatus.UNAUTHORIZED);
        }

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_USERS_ALL);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+oauth.access_token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            String result =
                    restTemplate.exchange(
                            builder.toUriString(), HttpMethod.GET, entity, String.class).getBody();

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (HttpServerErrorException e) {
            logger.error("user.getAllError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_USER.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user.login")
    public ResponseEntity loginUser(@RequestHeader(value="Authorization",required = false) String token,@RequestBody UserInfo requestUserDetails) {

        try {
            if(oauth.access_token==null)
                oauth.access_token=oauth.OauthGetToken();
            RestTemplate restTemplate = new RestTemplate();

            String url = URL_API_USERS_LOGIN;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            String requestJson =  ow.writeValueAsString(requestUserDetails);;
            System.out.println("jsoon"+requestJson);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+oauth.access_token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);

            UserInfo  result=   restTemplate.postForObject(url, entity, UserInfo.class);

            return new ResponseEntity(result,HttpStatus.OK);
        } catch (Exception e) {
            logger.error("user.createError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_USER_LOGIN.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user.updateUUID")
    public ResponseEntity updateUuidUser(@RequestHeader(value="Authorization",required = false) String token,@RequestBody UserInfo requestUserDetails) {
        if(oauth.OauthCheckToken(token)==false)
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(oauth.access_token==null)
            oauth.access_token=oauth.OauthGetToken();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders userInfohead = new HttpHeaders();
            userInfohead.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UserInfo> requestUserInfoEntity = new HttpEntity<>(requestUserDetails, userInfohead);
            restTemplate.exchange(URL_API_USERS_UPDATE_UUID,
                    HttpMethod.PUT, requestUserInfoEntity, new ParameterizedTypeReference<UserInfo>() {
                    });

            Map<String, String> info = new HashMap<String, String>();
            info.put("uid", requestUserDetails.getUid().toString());
            info.put("newUid", UUID.randomUUID().toString());

            RestTemplate restTemplate2 = new RestTemplate();
            HttpHeaders userInfohead2 = new HttpHeaders();
            userInfohead2.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> requestUserInfoEntity2 = new HttpEntity<>(info, userInfohead);
            restTemplate2.exchange(URL_API_STATISTIC_UPDATE_VK,
                    HttpMethod.PUT, requestUserInfoEntity2, new ParameterizedTypeReference<Map<String, String>>() {
                    });

            return new ResponseEntity(HttpStatus.OK);
        } catch (HttpServerErrorException e) {
            logger.error("user.updateUuidError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_USER.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user.updateVK")
    public ResponseEntity updateVkUser(@RequestHeader(value="Authorization",required = false) String token,@RequestBody UserInfo requestUserDetails) {
        if(oauth.OauthCheckToken(token)==false)
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(oauth.access_token==null)
            oauth.access_token=oauth.OauthGetToken();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders userInfohead = new HttpHeaders();
            userInfohead.setContentType(MediaType.APPLICATION_JSON);
            userInfohead.set("Authorization","Bearer "+oauth.access_token);
            HttpEntity<UserInfo> requestUserInfoEntity = new HttpEntity<>(requestUserDetails, userInfohead);
            restTemplate.exchange(URL_API_USERS_UPDATE_VK,
                    HttpMethod.PUT, requestUserInfoEntity, new ParameterizedTypeReference<UserInfo>() {
                    });

        } catch (HttpServerErrorException e) {
            logger.error("user.updateUuidError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_USER.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            UriComponentsBuilder builder3 = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_DELETE)
                    .queryParam("uuid", requestUserDetails.getUid());
            HttpHeaders headers2 = new HttpHeaders();
            headers2.set("Authorization", "Bearer " + oauth.access_token);
            HttpEntity<String> entity2 = new HttpEntity<>(headers2);

            RestTemplate restTemplate3 = new RestTemplate();

            restTemplate3.exchange(
                    builder3.toUriString(), HttpMethod.DELETE, entity2, String.class);


            UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_DELETE)
                    .queryParam("uuid", requestUserDetails.getUid());


            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + oauth.access_token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate2 = new RestTemplate();

            restTemplate2.exchange(
                    builder2.toUriString(), HttpMethod.DELETE, entity, String.class);


            ObjectWriter ow2 = new ObjectMapper().writer().withDefaultPrettyPrinter();
            UserInfo usermmy = new UserInfo();
            usermmy.setUid(requestUserDetails.getUid());
            usermmy.setVk(requestUserDetails.getVk());
            String requestJson2 = ow2.writeValueAsString(usermmy);

            RestTemplate restTemplate4 = new RestTemplate();

            String url2 = URL_API_STATISTIC_CREATE_STAT;
            HttpHeaders headers3 = new HttpHeaders();
            headers3.setContentType(MediaType.APPLICATION_JSON);
            headers3.set("Authorization", "Bearer " + oauth.access_token);

            HttpEntity<String> entity3 = new HttpEntity<String>(requestJson2, headers2);
            restTemplate4.postForObject(url2, entity3, String.class);

            RestTemplate restTemplate5 = new RestTemplate();

            String url3 = URL_API_STATONLINE_CREATE_STAT;
            HttpHeaders headers4 = new HttpHeaders();
            headers4.setContentType(MediaType.APPLICATION_JSON);
            headers4.set("Authorization", "Bearer " + oauth.access_token);
            HttpEntity<String> entity4 = new HttpEntity<String>(requestJson2, headers3);
            restTemplate5.postForObject(url3, entity4, String.class);
            System.out.println(" vce norm end");
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders userInfohead = new HttpHeaders();
            userInfohead.setContentType(MediaType.APPLICATION_JSON);
            userInfohead.set("Authorization","Bearer "+oauth.access_token);
            requestUserDetails.setVk("basta");
            HttpEntity<UserInfo> requestUserInfoEntity = new HttpEntity<>(requestUserDetails, userInfohead);
            restTemplate.exchange(URL_API_USERS_UPDATE_VK,
                    HttpMethod.PUT, requestUserInfoEntity, new ParameterizedTypeReference<UserInfo>() {
                    });
            return new ResponseEntity(ErrorCodes.ERROR_503_STATISTIC.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @DeleteMapping("/user.delete{uuid}")
    public ResponseEntity deleteUser(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        if(oauth.OauthCheckToken(token)==false)
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(oauth.access_token==null)
            oauth.access_token=oauth.OauthGetToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+oauth.access_token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_USERS_DELETE)
                    .queryParam("uuid", uuid);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(
                    builder.toUriString(), HttpMethod.DELETE, entity, String.class);
        } catch (HttpServerErrorException e) {
            RequestError requestError=new RequestError();
            requestError.setUuid(uuid.toString());
            requestError.setUrl(URL_API_USERS_DELETE);
            requestRepos.save(requestError);
        }
        try {
            UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_DELETE)
                    .queryParam("uuid", uuid);

            RestTemplate restTemplate2 = new RestTemplate();
            restTemplate2.exchange(
                    builder2.toUriString(), HttpMethod.DELETE, entity, String.class);
        }
        catch (HttpServerErrorException e) {
            RequestError requestError=new RequestError();
            requestError.setUuid(uuid.toString());
            requestError.setUrl(URL_API_STATONLINE_DELETE);
            requestRepos.save(requestError);  }
        try {
            UriComponentsBuilder builder3 = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_DELETE)
                    .queryParam("uuid", uuid);

            RestTemplate restTemplate3 = new RestTemplate();
            restTemplate3.exchange(
                    builder3.toUriString(), HttpMethod.DELETE, entity, String.class);
        }
        catch (HttpServerErrorException e) {
            RequestError requestError=new RequestError();
            requestError.setUuid(uuid.toString());
            requestError.setUrl(URL_API_STATISTIC_DELETE);
            requestRepos.save(requestError);
        }
        return new ResponseEntity(HttpStatus.OK);

    }
}
