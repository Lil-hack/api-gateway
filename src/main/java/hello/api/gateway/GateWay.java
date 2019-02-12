package hello.api.gateway;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import hello.api.gateway.config.RequestError;
import hello.api.gateway.config.RequestErrorRepository;
import hello.api.gateway.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@EnableScheduling
@RestController
@CrossOrigin
@RequestMapping("/api-gateway")
public class GateWay {
    private static final Logger logger = LoggerFactory.getLogger(GateWay.class);
    //----------------------------------USERS API----------------------------------------
    static final String URL_API_USERS = "https://api-my-users.herokuapp.com/api-users/";

    static final String URL_API_USERS_LOGIN = URL_API_USERS.concat("login");
    static final String URL_API_USERS_REGISTR = URL_API_USERS.concat("create");

    static final String URL_API_USERS_UPDATE_VK = URL_API_USERS.concat("updateVK");
    static final String URL_API_USERS_UPDATE_UUID = URL_API_USERS.concat("updateUUID");
    static final String URL_API_USERS_GET = URL_API_USERS.concat("get");
    static final String URL_API_USERS_DELETE = URL_API_USERS.concat("delete");
    static final String URL_API_USERS_ALL = URL_API_USERS.concat("getAll");

    //----------------------------------STATISTIC  API----------------------------------------
    static final String URL_API_STATISTIC = "https://api-my-statistic.herokuapp.com/api-statistic/";

    static final String URL_API_STATISTIC_CREATE_STAT = URL_API_STATISTIC.concat("create");
    static final String URL_API_STATISTIC_UPDATE_UUID = URL_API_STATISTIC.concat("updateUUID");
    static final String URL_API_STATISTIC_UPDATE_VK = URL_API_STATISTIC.concat("updateVK");
    static final String URL_API_STATISTIC_GET_STAT = URL_API_STATISTIC.concat("get");
    static final String URL_API_STATISTIC_FIND_ALL_STATS = URL_API_STATISTIC.concat("getAll");
    static final String URL_API_STATISTIC_FIND_WEEK_STATS = URL_API_STATISTIC.concat("getWeek");
    static final String URL_API_STATISTIC_FIND_MONTH_STATS = URL_API_STATISTIC.concat("getMonth");
    static final String URL_API_STATISTIC_DELETE = URL_API_STATISTIC.concat("delete");

    //----------------------------------STATONLINE  API----------------------------------------
    static final String URL_API_STATONLINE = "https://api-my-onlinestat.herokuapp.com/api-statOnline/";

    static final String URL_API_STATONLINE_CREATE_STAT = URL_API_STATONLINE.concat("create");
    static final String URL_API_STATONLINE_GET_STAT = URL_API_STATONLINE.concat("get");
    static final String URL_API_STATONLINE_FIND_ALL_STATS = URL_API_STATONLINE.concat("getAll");
    static final String URL_API_STATONLINE_FIND_DAY_STATS = URL_API_STATONLINE.concat("getDay");
    static final String URL_API_STATONLINE_FIND_WEEK_STATS = URL_API_STATONLINE.concat("getWeek");
    static final String URL_API_STATONLINE_FIND_MONTH_STATS = URL_API_STATONLINE.concat("getMonth");
    static final String URL_API_STATONLINE_DELETE = URL_API_STATONLINE.concat("delete");

    //----------------------------------OAUTH2  API----------------------------------------
    static final String URL_API_OAUTH = "http://ec2-18-185-66-28.eu-central-1.compute.amazonaws.com:8090";

     static final String AUTH_CODE_URI = URL_API_OAUTH.concat("/oauth20/auth-codes");
     static final String ACCESS_TOKEN_URI = URL_API_OAUTH.concat("/oauth20/tokens");
     static final String ACCESS_TOKEN_VALIDATE_URI = URL_API_OAUTH.concat("/oauth20/tokens/validate");
     static final String APPLICATION_URI =URL_API_OAUTH.concat("/oauth20/applications");

    static final String CLIENT_ID="9aa3ae11759ae257e2f29484a32820845ae59763";
    static final String CLIENT_SECRET="3997673d7814bbbcde139fe181e7fba723beb70a4e6e49363230ff78051f40d1";
    private String access_token;
    ////////////////////////////////OAUTH API////////////////////////////////////

    @Autowired
    private RequestErrorRepository requestRepos;

    @Scheduled(cron = "*/60 * * * * *")
    public void repeatRequest() {

            access_token=OauthGetToken();
            System.out.println("repeatRequest");
            List<RequestError> requestErrorList = new ArrayList<>();
            requestRepos.findAll().forEach(requestErrorList::add);
            if(requestErrorList.size()==0)
                return;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+access_token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            requestErrorList.forEach(requst->     { System.out.println("repeatRequest:"+requst);

                        switch(requst.getUrl()) {
                            case URL_API_USERS+"delete":
                                try {

                                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_USERS_DELETE)
                                            .queryParam("uuid", requst.getUuid());

                                    RestTemplate restTemplate = new RestTemplate();
                                    restTemplate.exchange(
                                            builder.toUriString(), HttpMethod.DELETE, entity, String.class);
                                    requestRepos.delete(requst);
                                }
                                catch (Exception e)
                                {
                                    logger.error("repeatRequest", e);
                                }
                                break;
                            case URL_API_STATISTIC+"delete":
                                try {
                                    UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_DELETE)
                                            .queryParam("uuid", requst.getUuid());

                                    RestTemplate restTemplate2 = new RestTemplate();
                                    restTemplate2.exchange(
                                            builder2.toUriString(), HttpMethod.DELETE, entity, String.class);
                                    requestRepos.delete(requst);
                                }  catch (Exception e)
                            {
                                logger.error("repeatRequest", e);
                            }
                                break;
                            case URL_API_STATONLINE+"delete":
                                try {
                                UriComponentsBuilder builder3 = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_DELETE)
                                        .queryParam("uuid", requst.getUuid());

                                RestTemplate restTemplate3 = new RestTemplate();
                                restTemplate3.exchange(
                                        builder3.toUriString(), HttpMethod.DELETE, entity, String.class);
                                    requestRepos.delete(requst);
                        }  catch (Exception e)
                            {
                                logger.error("repeatRequest", e);
                            }
                                break;

                        }
                    System.out.println(requst.getUrl());
        }

            );




    }
        private boolean OauthCheckToken(String token)
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

    private String OauthGetToken()
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

            return new ResponseEntity(response,HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("user.createError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/oauth20/authorize")
    public ResponseEntity<String> getCode(@RequestHeader(value="Authorization",required = false) String token,
                                          @RequestParam String client_id,@RequestParam String response_type,@RequestParam String redirect_uri) {
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

    ////////////////////////////////USER API////////////////////////////////////
    @PostMapping("/user.create")
    public ResponseEntity registrationUser(@RequestHeader(value="Authorization",required = false) String token,@RequestBody UserInfo requestUserDetails) {
        try {
            if(access_token==null)
                access_token=OauthGetToken();

            RestTemplate restTemplate = new RestTemplate();

            String url = URL_API_USERS_REGISTR;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            String requestJson =  ow.writeValueAsString(requestUserDetails);
            System.out.println("jsoon"+requestJson);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer "+access_token);
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
            headers2.set("Authorization","Bearer "+access_token);
            HttpEntity<String> entity2 = new HttpEntity<String>(requestJson2,headers2);
            restTemplate2.postForObject(url2, entity2, String.class);
            System.out.println("stat vce norm");

            System.out.println("jsoon2"+requestJson2);
            RestTemplate restTemplate3 = new RestTemplate();

            String url3 = URL_API_STATONLINE_CREATE_STAT;
            HttpHeaders headers3 = new HttpHeaders();
            headers3.setContentType(MediaType.APPLICATION_JSON);
            headers3.set("Authorization","Bearer "+access_token);
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
    public ResponseEntity<UserInfo> getUser(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        if(!OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(access_token==null)
            access_token=OauthGetToken();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_USERS_GET)
                    .queryParam("uuid", uuid);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+access_token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            String result =
                    restTemplate.exchange(
                            builder.toUriString(), HttpMethod.GET, entity, String.class).getBody();
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("user.getError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_USER.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user.getAll")
    public ResponseEntity<List<UserInfo>> getUserAll(@RequestHeader(value="Authorization",required = false) String token) {
    if(!OauthCheckToken(token))
       return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(access_token==null)
            access_token=OauthGetToken();

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_USERS_ALL);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+access_token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            String result =
                    restTemplate.exchange(
                            builder.toUriString(), HttpMethod.GET, entity, String.class).getBody();

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("user.getAllError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_USER.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/user.login")
    public ResponseEntity<UserInfo> loginUser(@RequestHeader(value="Authorization",required = false) String token,@RequestBody UserInfo requestUserDetails) {

        try {
            if(access_token==null)
                access_token=OauthGetToken();
            RestTemplate restTemplate = new RestTemplate();

            String url = URL_API_USERS_LOGIN;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            String requestJson =  ow.writeValueAsString(requestUserDetails);;
            System.out.println("jsoon"+requestJson);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+access_token);
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
        if(!OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(access_token==null)
            access_token=OauthGetToken();
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
        } catch (Exception e) {
            logger.error("user.updateUuidError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_USER.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user.updateVK")
    public ResponseEntity updateVkUser(@RequestHeader(value="Authorization",required = false) String token,@RequestBody UserInfo requestUserDetails) {
        if(!OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(access_token==null)
            access_token=OauthGetToken();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders userInfohead = new HttpHeaders();
            userInfohead.setContentType(MediaType.APPLICATION_JSON);
            userInfohead.set("Authorization","Bearer "+access_token);
            HttpEntity<UserInfo> requestUserInfoEntity = new HttpEntity<>(requestUserDetails, userInfohead);
            restTemplate.exchange(URL_API_USERS_UPDATE_VK,
                    HttpMethod.PUT, requestUserInfoEntity, new ParameterizedTypeReference<UserInfo>() {
                    });

        } catch (Exception e) {
            logger.error("user.updateUuidError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_USER.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            UriComponentsBuilder builder3 = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_DELETE)
                    .queryParam("uuid", requestUserDetails.getUid());
            HttpHeaders headers2 = new HttpHeaders();
            headers2.set("Authorization", "Bearer " + access_token);
            HttpEntity<String> entity2 = new HttpEntity<>(headers2);

            RestTemplate restTemplate3 = new RestTemplate();

            restTemplate3.exchange(
                    builder3.toUriString(), HttpMethod.DELETE, entity2, String.class);


            UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_DELETE)
                    .queryParam("uuid", requestUserDetails.getUid());


            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + access_token);
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
            headers3.set("Authorization", "Bearer " + access_token);

            HttpEntity<String> entity3 = new HttpEntity<String>(requestJson2, headers2);
            restTemplate4.postForObject(url2, entity3, String.class);

            RestTemplate restTemplate5 = new RestTemplate();

            String url3 = URL_API_STATONLINE_CREATE_STAT;
            HttpHeaders headers4 = new HttpHeaders();
            headers4.setContentType(MediaType.APPLICATION_JSON);
            headers4.set("Authorization", "Bearer " + access_token);
            HttpEntity<String> entity4 = new HttpEntity<String>(requestJson2, headers3);
            restTemplate5.postForObject(url3, entity4, String.class);
            System.out.println(" vce norm end");
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders userInfohead = new HttpHeaders();
            userInfohead.setContentType(MediaType.APPLICATION_JSON);
            userInfohead.set("Authorization","Bearer "+access_token);
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
        if(!OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(access_token==null)
            access_token=OauthGetToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+access_token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_USERS_DELETE)
                    .queryParam("uuid", uuid);

            RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(
                builder.toUriString(), HttpMethod.DELETE, entity, String.class);
        } catch (Exception e) {
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
        catch (Exception e) {
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
        catch (Exception e) {
            RequestError requestError=new RequestError();
            requestError.setUuid(uuid.toString());
            requestError.setUrl(URL_API_STATISTIC_DELETE);
            requestRepos.save(requestError);
        }
            return new ResponseEntity(HttpStatus.OK);

    }
    ///////////////////////////////Statistic API///////////////////////////////
    @PostMapping("/statistic.create")
    public ResponseEntity createStatistic(@RequestHeader(value="Authorization",required = false) String token,@RequestBody UserInfo info) {
        if(!OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(access_token==null)
            access_token=OauthGetToken();
        try {

            RestTemplate restTemplate = new RestTemplate();

            String url = URL_API_STATISTIC_CREATE_STAT;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            String requestJson =  ow.writeValueAsString(info);
            System.out.println("jsoon"+requestJson);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer "+access_token);

            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
            restTemplate.postForObject(url, entity, String.class);

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("user.createError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATISTIC.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistic.getAll")
    public ResponseEntity<List<StatisticInfo>> getStatAll(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        System.out.println("user vce norm");
        System.out.println("user vce norm"+token);


        if(!OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);


        try {
            if(access_token==null)
            access_token=OauthGetToken();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_FIND_ALL_STATS)
                    .queryParam("uuid", uuid);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+access_token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            String result =
            restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, entity, String.class).getBody();
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statistic.getAllError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATISTIC.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistic.getWeek")
    public ResponseEntity<List<StatisticInfo>> getStatWeek(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        if(!OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(access_token==null)
            access_token=OauthGetToken();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_FIND_WEEK_STATS)
                    .queryParam("uuid", uuid);


            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+access_token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            String result =
                    restTemplate.exchange(
                            builder.toUriString(), HttpMethod.GET, entity, String.class).getBody();

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statistic.getWeekError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATISTIC.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistic.getMonth")
    public ResponseEntity<List<StatisticInfo>> getStatMonth(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        if(!OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(access_token==null)
            access_token=OauthGetToken();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_FIND_MONTH_STATS)
                    .queryParam("uuid", uuid);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+access_token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            String result =
                    restTemplate.exchange(
                            builder.toUriString(), HttpMethod.GET, entity, String.class).getBody();

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statistic.getMonthError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATISTIC.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistic.get")
    public ResponseEntity<StatisticInfo> getStat(@RequestHeader(value="Authorization",required = false) String token, @RequestParam UUID uuid) {
        if(!OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(access_token==null)
            access_token=OauthGetToken();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+access_token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_USERS_GET)
                    .queryParam("uuid", uuid);

            RestTemplate restTemplate = new RestTemplate();
            UserInfo user = restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, entity, UserInfo.class).getBody();

            UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_GET_STAT)
                    .queryParam("vk", user.getVk()).queryParam("uuid", uuid);

            RestTemplate restTemplate2 = new RestTemplate();
            String result = restTemplate2.exchange(
                    builder2.toUriString(), HttpMethod.GET, entity, String.class).getBody();

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statistic.getError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATISTIC.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    ///////////////////////////////Statistic Online  API///////////////////////////////
    @PostMapping("/statOnline.create")
    public ResponseEntity<List<StatOnlineInfo>> getStatOnlineCreate(@RequestHeader(value="Authorization",required = false) String token,@RequestBody UserInfo info) {
        if(!OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(access_token==null)
            access_token=OauthGetToken();
        try {

            RestTemplate restTemplate = new RestTemplate();

            String url = URL_API_STATONLINE_CREATE_STAT;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            String requestJson =  ow.writeValueAsString(info);
            System.out.println("jsoon"+requestJson);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer "+access_token);
            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
            restTemplate.postForObject(url, entity, String.class);

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("statOnline.createError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATONLINE.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/statOnline.getAll")
    public ResponseEntity<List<StatOnlineInfo>> getStatOnlineAll(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        if(!OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(access_token==null)
            access_token=OauthGetToken();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_FIND_ALL_STATS)
                    .queryParam("uuid", uuid);


            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+access_token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            String result =
                    restTemplate.exchange(
                            builder.toUriString(), HttpMethod.GET, entity, String.class).getBody();

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statOnline.getAllError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATONLINE.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/statOnline.getDay")
    public ResponseEntity<List<StatOnlineInfo>> getStatOnlineDay(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        if(!OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(access_token==null)
            access_token=OauthGetToken();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_FIND_DAY_STATS)
                    .queryParam("uuid", uuid);


            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+access_token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            String result =
                    restTemplate.exchange(
                            builder.toUriString(), HttpMethod.GET, entity, String.class).getBody();

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statOnline.getDayError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATONLINE.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statOnline.getWeek")
    public ResponseEntity<List<StatOnlineInfo>> getStatOnlineWeek(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        if(!OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(access_token==null)
            access_token=OauthGetToken();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_FIND_WEEK_STATS)
  .queryParam("uuid", uuid);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+access_token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            String result =
                    restTemplate.exchange(
                            builder.toUriString(), HttpMethod.GET, entity, String.class).getBody();

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statOnline.getWeekError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATONLINE.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statOnline.getMonth")
    public ResponseEntity<List<StatOnlineInfo>> getStatOnlineMonth(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        if(!OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(access_token==null)
            access_token=OauthGetToken();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_FIND_MONTH_STATS)
  .queryParam("uuid", uuid);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+access_token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            String result =
                    restTemplate.exchange(
                            builder.toUriString(), HttpMethod.GET, entity, String.class).getBody();

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statOnline.getMonthError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATONLINE.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statAll.get")
    public ResponseEntity<StatOnlineInfo> getStatOnline(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid,@RequestParam String vk) {
        if(!OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(access_token==null)
            access_token=OauthGetToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + access_token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        StatAll statAll=new StatAll();
        try {

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_GET_STAT)
                    .queryParam("vk", vk).queryParam("uuid", uuid);

            RestTemplate restTemplate = new RestTemplate();
            StatisticInfo statisticInfo = restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, entity, StatisticInfo.class).getBody();


            statAll.setPhotoUrl(statisticInfo.getPhotoUrl());
            statAll.setFirst_name(statisticInfo.getFirst_name());
            statAll.setLast_name(statisticInfo.getLast_name());
            statAll.setAlbums(statisticInfo.getAlbums());
            statAll.setVideos(statisticInfo.getVideos());
            statAll.setAudios(statisticInfo.getAudios());
            statAll.setNotes(statisticInfo.getNotes());
            statAll.setPhotos(statisticInfo.getPhotos());
            statAll.setGroups(statisticInfo.getGroups());
            statAll.setGifts(statisticInfo.getGifts());
            statAll.setFriends(statisticInfo.getFriends());
            statAll.setUser_photos(statisticInfo.getUser_photos());
            statAll.setFollowers(statisticInfo.getFollowers());
            statAll.setSubscriptions(statisticInfo.getSubscriptions());
            statAll.setPages(statisticInfo.getPages());


        }catch (Exception e) {
            statAll.setPhotoUrl(null);

            }

        try {
            UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_GET_STAT)
                    .queryParam("vk", vk).queryParam("uuid", uuid);

            RestTemplate restTemplate2 = new RestTemplate();
            StatOnlineInfo statOnlineInfo = restTemplate2.exchange(
                    builder2.toUriString(), HttpMethod.GET, entity, StatOnlineInfo.class).getBody();
            statAll.setMobile(statOnlineInfo.isMobile());
            statAll.setOnline(statOnlineInfo.isOnline());
            statAll.setUid(uuid.toString());


        } catch (Exception e) {

            statAll.setUid(null);
        }

        return new ResponseEntity(statAll, HttpStatus.OK);
    }


}
