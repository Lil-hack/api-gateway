package hello.api.gateway;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import hello.api.gateway.Controllers.OauthController;
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
    public static final String URL_API_USERS = "https://api-my-users.herokuapp.com/api-users/";

    public static final String URL_API_USERS_LOGIN = URL_API_USERS.concat("login");
    public static final String URL_API_USERS_REGISTR = URL_API_USERS.concat("create");

    public static final String URL_API_USERS_UPDATE_VK = URL_API_USERS.concat("updateVK");
    public  static final String URL_API_USERS_UPDATE_UUID = URL_API_USERS.concat("updateUUID");
    public static final String URL_API_USERS_GET = URL_API_USERS.concat("get");
    public static final String URL_API_USERS_DELETE = URL_API_USERS.concat("delete");
    public static final String URL_API_USERS_ALL = URL_API_USERS.concat("getAll");

    //----------------------------------STATISTIC  API----------------------------------------
    public static final String URL_API_STATISTIC = "https://api-my-statistic.herokuapp.com/api-statistic/";

    public static final String URL_API_STATISTIC_CREATE_STAT = URL_API_STATISTIC.concat("create");
    public static final String URL_API_STATISTIC_UPDATE_UUID = URL_API_STATISTIC.concat("updateUUID");
    public static final String URL_API_STATISTIC_UPDATE_VK = URL_API_STATISTIC.concat("updateVK");
    public static final String URL_API_STATISTIC_GET_STAT = URL_API_STATISTIC.concat("get");
    public static final String URL_API_STATISTIC_FIND_ALL_STATS = URL_API_STATISTIC.concat("getAll");
    public static final String URL_API_STATISTIC_FIND_WEEK_STATS = URL_API_STATISTIC.concat("getWeek");
    public static final String URL_API_STATISTIC_FIND_MONTH_STATS = URL_API_STATISTIC.concat("getMonth");
    public static final String URL_API_STATISTIC_DELETE = URL_API_STATISTIC.concat("delete");

    //----------------------------------STATONLINE  API----------------------------------------
    public static final String URL_API_STATONLINE = "https://api-my-onlinestat.herokuapp.com/api-statOnline/";

    public static final String URL_API_STATONLINE_CREATE_STAT = URL_API_STATONLINE.concat("create");
    public static final String URL_API_STATONLINE_GET_STAT = URL_API_STATONLINE.concat("get");
    public static final String URL_API_STATONLINE_FIND_ALL_STATS = URL_API_STATONLINE.concat("getAll");
    public static final String URL_API_STATONLINE_FIND_DAY_STATS = URL_API_STATONLINE.concat("getDay");
    public static final String URL_API_STATONLINE_FIND_WEEK_STATS = URL_API_STATONLINE.concat("getWeek");
    public static final String URL_API_STATONLINE_FIND_MONTH_STATS = URL_API_STATONLINE.concat("getMonth");
    public static final String URL_API_STATONLINE_DELETE = URL_API_STATONLINE.concat("delete");

    //----------------------------------OAUTH2  API----------------------------------------
    public static final String URL_API_OAUTH = "http://ec2-52-59-241-115.eu-central-1.compute.amazonaws.com:8090";

    public static final String AUTH_CODE_URI = URL_API_OAUTH.concat("/oauth20/auth-codes");
    public static final String ACCESS_TOKEN_URI = URL_API_OAUTH.concat("/oauth20/tokens");
    public static final String ACCESS_TOKEN_VALIDATE_URI = URL_API_OAUTH.concat("/oauth20/tokens/validate");
    public static final String APPLICATION_URI =URL_API_OAUTH.concat("/oauth20/applications");



    @Autowired
    private OauthController oauth;
    @Autowired
    private RequestErrorRepository requestRepos;

    @Scheduled(cron = "*/30 * * * * *")
    public void repeatRequest() {

            oauth.access_token=oauth.OauthGetToken();
            System.out.println("Таймер и редис работает");
            List<RequestError> requestErrorList = new ArrayList<>();
            requestRepos.findAll().forEach(requestErrorList::add);
            if(requestErrorList.size()==0) {
                System.out.println("Буфер пустой");
                return;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+oauth.access_token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            requestErrorList.forEach(requst->     { System.out.println("Запрос с ошибкой для повтора транзакции:"+requst);

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
        

}
