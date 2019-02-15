package hello.api.gateway.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import hello.api.gateway.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.UUID;

import static hello.api.gateway.GateWay.*;

@RestController
@CrossOrigin
@RequestMapping("/api-gateway")
public class StatOnlineController {
    private static final Logger logger = LoggerFactory.getLogger(StatOnlineController.class);

    @Autowired
    private OauthController oauth;

    @PostMapping("/statOnline.create")
    public ResponseEntity getStatOnlineCreate(@RequestHeader(value="Authorization",required = false) String token, @RequestBody UserInfo info) {
        if(oauth.OauthCheckToken(token)==false) {
            return new ResponseEntity(ErrorCodes.ERROR_401.error(), HttpStatus.UNAUTHORIZED);
        }
        if(oauth.access_token==null) {
            oauth.access_token = oauth.OauthGetToken();
        }
        try {

            RestTemplate restTemplate = new RestTemplate();

            String url = URL_API_STATONLINE_CREATE_STAT;
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
            logger.error("statOnline.createError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATONLINE.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statOnline.getAll")
    public ResponseEntity getStatOnlineAll(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        if(oauth.OauthCheckToken(token)==false) {
            return new ResponseEntity(ErrorCodes.ERROR_401.error(), HttpStatus.UNAUTHORIZED);
        }
        if(oauth.access_token==null) {
            oauth.access_token = oauth.OauthGetToken();
        }
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_FIND_ALL_STATS)
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
            logger.error("statOnline.getAllError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATONLINE.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statOnline.getDay")
    public ResponseEntity getStatOnlineDay(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        if(oauth.OauthCheckToken(token)==false) {
            return new ResponseEntity(ErrorCodes.ERROR_401.error(), HttpStatus.UNAUTHORIZED);
        }
        if(oauth.access_token==null) {
            oauth.access_token = oauth.OauthGetToken();
        }
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_FIND_DAY_STATS)
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
            logger.error("statOnline.getDayError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATONLINE.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statOnline.getWeek")
    public ResponseEntity getStatOnlineWeek(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        if(oauth.OauthCheckToken(token)==false) {
            return new ResponseEntity(ErrorCodes.ERROR_401.error(), HttpStatus.UNAUTHORIZED);
        }
        if(oauth.access_token==null) {
            oauth.access_token = oauth.OauthGetToken();
        }
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_FIND_WEEK_STATS)
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
            logger.error("statOnline.getWeekError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATONLINE.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statOnline.getMonth")
    public ResponseEntity getStatOnlineMonth(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid) {
        if(oauth.OauthCheckToken(token)==false) {
            return new ResponseEntity(ErrorCodes.ERROR_401.error(), HttpStatus.UNAUTHORIZED);
        }
        if(oauth.access_token==null) {
            oauth.access_token = oauth.OauthGetToken();
        }
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_API_STATONLINE_FIND_MONTH_STATS)
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
            logger.error("statOnline.getMonthError", e);
            return new ResponseEntity(ErrorCodes.ERROR_503_STATONLINE.error(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statAll.get")
    public ResponseEntity getStatOnline(@RequestHeader(value="Authorization",required = false) String token,@RequestParam UUID uuid,@RequestParam String vk) {
        System.out.println("token"+token);
        if(oauth.OauthCheckToken(token)==false) {
            return new ResponseEntity(ErrorCodes.ERROR_401.error(), HttpStatus.UNAUTHORIZED);
        }
        if(oauth.access_token==null) {
            oauth.access_token = oauth.OauthGetToken();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + oauth.access_token);
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


        } catch (HttpServerErrorException e) {

            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                oauth.access_token = oauth.OauthGetToken();
            }
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
