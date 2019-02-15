package hello.api.gateway.Controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import hello.api.gateway.GateWay;
import hello.api.gateway.models.ErrorCodes;
import hello.api.gateway.models.StatisticInfo;
import hello.api.gateway.models.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static hello.api.gateway.GateWay.*;

@RestController
@CrossOrigin
@RequestMapping("/api-gateway")
public class StatisticController {

    private static final Logger logger = LoggerFactory.getLogger(StatisticController.class);

    @Autowired
    private OauthController oauth;
    
    @PostMapping("/statistic.create")
    public ResponseEntity createStatistic(@RequestHeader(value="Authorization",required = false) String token, @RequestBody UserInfo info) {
        if(!oauth.OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(), HttpStatus.UNAUTHORIZED);
        if(oauth.access_token==null)
            oauth.access_token=oauth.OauthGetToken();
        try {

            RestTemplate restTemplate = new RestTemplate();

            String url = URL_API_STATISTIC_CREATE_STAT;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            String requestJson =  ow.writeValueAsString(info);
            System.out.println("jsoon"+requestJson);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer "+oauth.access_token);

            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
            restTemplate.postForObject(url, entity, String.class);

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("user.createError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATISTIC.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistic.getAll")
    public ResponseEntity getStatAll(@RequestHeader(value="Authorization",required = false) String token, @RequestParam UUID uuid) {
        System.out.println("user vce norm");
        System.out.println("user vce norm"+token);


        if(!oauth.OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);


        try {
            if(oauth.access_token==null)
                oauth.access_token=oauth.OauthGetToken();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_FIND_ALL_STATS)
                    .queryParam("uuid", uuid);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+oauth.access_token);
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
    public ResponseEntity getStatWeek(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        if(!oauth.OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(oauth.access_token==null)
            oauth.access_token=oauth.OauthGetToken();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_FIND_WEEK_STATS)
                    .queryParam("uuid", uuid);


            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+oauth.access_token);
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
    public ResponseEntity getStatMonth(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        if(!oauth.OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(oauth.access_token==null)
            oauth.access_token=oauth.OauthGetToken();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATISTIC_FIND_MONTH_STATS)
                    .queryParam("uuid", uuid);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+oauth.access_token);
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
    public ResponseEntity getStat(@RequestHeader(value="Authorization",required = false) String token, @RequestParam UUID uuid) {
        if(!oauth.OauthCheckToken(token))
            return   new ResponseEntity(ErrorCodes.ERROR_401.error(),HttpStatus.UNAUTHORIZED);
        if(oauth.access_token==null)
            oauth.access_token=oauth.OauthGetToken();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+oauth.access_token);
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
}
