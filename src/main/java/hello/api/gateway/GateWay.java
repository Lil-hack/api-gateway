package hello.api.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import hello.api.gateway.models.StatOnlineInfo;
import hello.api.gateway.models.StatisticInfo;
import hello.api.gateway.models.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
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


@RestController
@RequestMapping("/api-gateway")
public class GateWay {
    private static final Logger logger = LoggerFactory.getLogger(GateWay.class);
    //----------------------------------USERS API----------------------------------------
    static final String URL_API_USERS = "https://my-api-users.herokuapp.com/api-users/";

    static final String URL_API_USERS_LOGIN = URL_API_USERS.concat("login");
    static final String URL_API_USERS_REGISTR = URL_API_USERS.concat("create");

    static final String URL_API_USERS_UPDATE_VK = URL_API_USERS.concat("updateVK");
    static final String URL_API_USERS_UPDATE_UUID = URL_API_USERS.concat("updateUUID");
    static final String URL_API_USERS_GET = URL_API_USERS.concat("get");
    static final String URL_API_USERS_DELETE = URL_API_USERS.concat("delete");
    static final String URL_API_USERS_ALL = URL_API_USERS.concat("getAll");

    //----------------------------------STATISTIC  API----------------------------------------
    static final String URL_API_STATISTIC = "https://my-api-statistic.herokuapp.com/api-statistic/";

    static final String URL_API_STATISTIC_CREATE_STAT = URL_API_STATISTIC.concat("create");
    static final String URL_API_STATISTIC_UPDATE_UUID = URL_API_STATISTIC.concat("updateUUID");
    static final String URL_API_STATISTIC_UPDATE_VK = URL_API_STATISTIC.concat("updateVK");
    static final String URL_API_STATISTIC_GET_STAT = URL_API_STATISTIC.concat("get");
    static final String URL_API_STATISTIC_FIND_ALL_STATS = URL_API_STATISTIC.concat("getAll");
    static final String URL_API_STATISTIC_FIND_WEEK_STATS = URL_API_STATISTIC.concat("getWeek");
    static final String URL_API_STATISTIC_FIND_MONTH_STATS = URL_API_STATISTIC.concat("getMonth");
    static final String URL_API_STATISTIC_DELETE = URL_API_STATISTIC.concat("delete");

    //----------------------------------STATONLINE  API----------------------------------------
    static final String URL_API_STATONLINE = "https://my-api-statonline.herokuapp.com/api-statOnline/";

    static final String URL_API_STATONLINE_CREATE_STAT = URL_API_STATONLINE.concat("create");
    static final String URL_API_STATONLINE_GET_STAT = URL_API_STATONLINE.concat("get");
    static final String URL_API_STATONLINE_FIND_ALL_STATS = URL_API_STATONLINE.concat("getAll");
    static final String URL_API_STATONLINE_FIND_DAY_STATS = URL_API_STATONLINE.concat("getDay");
    static final String URL_API_STATONLINE_FIND_WEEK_STATS = URL_API_STATONLINE.concat("getWeek");
    static final String URL_API_STATONLINE_FIND_MONTH_STATS = URL_API_STATONLINE.concat("getMonth");
    static final String URL_API_STATONLINE_DELETE = URL_API_STATONLINE.concat("delete");


    ////////////////////////////////USER API////////////////////////////////////

    @PostMapping("/user.create")
    public ResponseEntity registrationUser(@RequestBody UserInfo requestUserDetails) {
        try {

            RestTemplate restTemplate = new RestTemplate();

            String url = URL_API_USERS_REGISTR;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            String requestJson =  ow.writeValueAsString(requestUserDetails);;
            System.out.println("jsoon"+requestJson);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
            UUID uuid =   restTemplate.postForObject(url, entity, UUID.class);
            requestUserDetails.setUid(uuid);
            RestTemplate restTemplate2 = new RestTemplate();

            String url2 = URL_API_STATISTIC_CREATE_STAT;
            HttpHeaders headers2 = new HttpHeaders();
            headers2.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity2 = new HttpEntity<String>(requestJson,headers2);
            restTemplate2.postForObject(url2, entity2, String.class);

            RestTemplate restTemplate3 = new RestTemplate();

            String url3 = URL_API_STATONLINE_CREATE_STAT;
            HttpHeaders headers3 = new HttpHeaders();
            headers3.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity3 = new HttpEntity<String>(requestJson,headers3);
            restTemplate3.postForObject(url3, entity3, String.class);

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("user.createError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user.get{uuid}")
    public ResponseEntity<UserInfo> getUser(@RequestParam UUID uuid) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_USERS_GET)
                    .queryParam("uuid", uuid);

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(builder.toUriString(), String.class);

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("user.getError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user.getAll")
    public ResponseEntity<List<UserInfo>> getUserAll() {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_USERS_ALL);


            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(builder.toUriString(), String.class);

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("user.getAllError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/user.login")
    public ResponseEntity loginUser(@RequestBody UserInfo requestUserDetails) {
        try {

            RestTemplate restTemplate = new RestTemplate();

            String url = URL_API_USERS_LOGIN;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            String requestJson =  ow.writeValueAsString(requestUserDetails);;
            System.out.println("jsoon"+requestJson);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);


            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("user.createError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/user.updateUUID")
    public ResponseEntity updateUuidUser(@RequestBody UserInfo requestUserDetails) {
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
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user.updateVK")
    public ResponseEntity updateVkUser(@RequestBody UserInfo requestUserDetails) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders userInfohead = new HttpHeaders();
            userInfohead.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UserInfo> requestUserInfoEntity = new HttpEntity<>(requestUserDetails, userInfohead);
            restTemplate.exchange(URL_API_USERS_UPDATE_VK,
                    HttpMethod.PUT, requestUserInfoEntity, new ParameterizedTypeReference<UserInfo>() {
                    });

            Map<String, String> info = new HashMap<String, String>();
            info.put("uid", requestUserDetails.getUid().toString());
            info.put("vk", requestUserDetails.getVk());

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
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/user.delete{uuid}")
    public ResponseEntity deleteUser(@RequestParam UUID uuid) {
        try {

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_USERS_DELETE)
                    .queryParam("uuid", uuid);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.delete(builder.toUriString(), String.class);

            UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_DELETE)
                    .queryParam("uuid", uuid);

            RestTemplate restTemplate2 = new RestTemplate();
            restTemplate2.delete(builder2.toUriString(), String.class);

            UriComponentsBuilder builder3 = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_DELETE)
                    .queryParam("uuid", uuid);

            RestTemplate restTemplate3 = new RestTemplate();
            restTemplate3.delete(builder3.toUriString(), String.class);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("user.deleteError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    ///////////////////////////////Statistic API///////////////////////////////
    @PostMapping("/statistic.create")
    public ResponseEntity createStatistic(@RequestBody UserInfo info) {
        try {

            RestTemplate restTemplate = new RestTemplate();

            String url = URL_API_STATISTIC_CREATE_STAT;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            String requestJson =  ow.writeValueAsString(info);
            System.out.println("jsoon"+requestJson);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
            restTemplate.postForObject(url, entity, String.class);

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("user.createError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistic.getAll")
    public ResponseEntity<List<StatisticInfo>> getStatAll(@RequestParam UUID uuid) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_FIND_ALL_STATS)
                    .queryParam("uuid", uuid);


            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(builder.toUriString(), String.class);

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statistic.getAllError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistic.getWeek")
    public ResponseEntity<List<StatisticInfo>> getStatWeek(@RequestParam UUID uuid) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_FIND_WEEK_STATS)
                    .queryParam("uuid", uuid);


            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(builder.toUriString(), String.class);

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statistic.getWeekError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistic.getMonth")
    public ResponseEntity<List<StatisticInfo>> getStatMonth(@RequestParam UUID uuid) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_FIND_MONTH_STATS)
                    .queryParam("uuid", uuid);


            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(builder.toUriString(), String.class);

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statistic.getMonthError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistic.get")
    public ResponseEntity<StatisticInfo> getStat( @RequestParam UUID uuid) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_USERS_GET)
                    .queryParam("uuid", uuid);

            RestTemplate restTemplate = new RestTemplate();
            UserInfo user = restTemplate.getForObject(builder.toUriString(), UserInfo.class);

            UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_GET_STAT)
                    .queryParam("vk", user.getVk()).queryParam("uuid", uuid);;

            RestTemplate restTemplate2 = new RestTemplate();
            String result = restTemplate2.getForObject(builder2.toUriString(), String.class);

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statistic.getError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    ///////////////////////////////Statistic Online  API///////////////////////////////
    @PostMapping("/statOnline.create")
    public ResponseEntity<List<StatOnlineInfo>> getStatOnlineCreate(@RequestBody UserInfo info) {
        try {

            RestTemplate restTemplate = new RestTemplate();

            String url = URL_API_STATONLINE_CREATE_STAT;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            String requestJson =  ow.writeValueAsString(info);
            System.out.println("jsoon"+requestJson);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
            restTemplate.postForObject(url, entity, String.class);

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("statOnline.createError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/statOnline.getAll")
    public ResponseEntity<List<StatOnlineInfo>> getStatOnlineAll(@RequestParam UUID uuid) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_FIND_ALL_STATS)
                    .queryParam("uuid", uuid);


            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(builder.toUriString(), String.class);

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statOnline.getAllError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/statOnline.getDay")
    public ResponseEntity<List<StatOnlineInfo>> getStatOnlineDay(@RequestParam UUID uuid) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_FIND_DAY_STATS)
                    .queryParam("uuid", uuid);


            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(builder.toUriString(), String.class);

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statOnline.getDayError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statOnline.getWeek")
    public ResponseEntity<List<StatOnlineInfo>> getStatOnlineWeek(@RequestParam UUID uuid) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_FIND_WEEK_STATS)
  .queryParam("uuid", uuid);

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(builder.toUriString(), String.class);

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statOnline.getWeekError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statOnline.getMonth")
    public ResponseEntity<List<StatOnlineInfo>> getStatOnlineMonth(@RequestParam UUID uuid) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_FIND_MONTH_STATS)
  .queryParam("uuid", uuid);

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(builder.toUriString(), String.class);

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statOnline.getMonthError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statOnline.get")
    public ResponseEntity<StatOnlineInfo> getStatOnline(@RequestParam UUID uuid) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_USERS_GET)
                   .queryParam("uuid", uuid);

           RestTemplate restTemplate = new RestTemplate();
           UserInfo user = restTemplate.getForObject(builder.toUriString(), UserInfo.class);

            UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_GET_STAT)
                    .queryParam("vk", user.getVk()).queryParam("uuid", uuid);;

            RestTemplate restTemplate2 = new RestTemplate();
            String result = restTemplate2.getForObject(builder2.toUriString(), String.class);

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("statOnline.getError", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
