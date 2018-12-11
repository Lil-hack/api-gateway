package hello.api.gateway;

import com.google.gson.Gson;
import hello.api.gateway.models.UserInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GateWayTest {
    @Autowired
    private MockMvc mvc;

    private  final Gson gson=new Gson();

    static final String URL_API_GATEWAY = "http://localhost:8080/api-gateway/";


    static final String URL_API_GATEWAY_USER_REGISTR = URL_API_GATEWAY.concat("user.create");
    static final String URL_API_GATEWAY_USER_GET = URL_API_GATEWAY.concat("user.get");
    static final String URL_API_GATEWAY_USER_DELETE = URL_API_GATEWAY.concat("user.delete");
    static final String URL_API_GATEWAY_USER_ALL = URL_API_GATEWAY.concat("user.getAll");

    //----------------------------------STATISTIC  API----------------------------------------

    static final String URL_API_GATEWAY_STAT_GET = URL_API_GATEWAY.concat("statistic.get");
    static final String URL_API_GATEWAY_STATS_FIND_ALL = URL_API_GATEWAY.concat("statistic.getAll");
    static final String URL_API_GATEWAY_STATS_FIND_WEEK = URL_API_GATEWAY.concat("statistic.getWeek");
    static final String URL_API_GATEWAY_STATS_FIND_MONTH = URL_API_GATEWAY.concat("statistic.getMonth");


    //----------------------------------STATONLINE  API----------------------------------------

    static final String URL_API_GATEWAY_STATONLINE_GET = URL_API_GATEWAY.concat("statOnline.get");
    static final String URL_API_GATEWAY_STATONLINE_FIND_ALL = URL_API_GATEWAY.concat("statOnline.getAll");
    static final String URL_API_GATEWAY_STATONLINE_FIND_DAY = URL_API_GATEWAY.concat("statOnline.getDay");
    static final String URL_API_GATEWAY_STATONLINE_FIND_WEEK = URL_API_GATEWAY.concat("statOnline.getWeek");
    static final String URL_API_GATEWAY_STATONLINE_FIND_MONTH = URL_API_GATEWAY.concat("statOnline.getMonth");

    final String email= randomAlphabetic(3);
    final String vk="basta";
    final String password= randomAlphabetic(8);
    final UUID uuid=UUID.randomUUID();

    @Before
    public void setUp() throws Exception {

        UserInfo request= new UserInfo();
        request.setEmail(email);
        request.setVk(vk);
        request.setPassword(password);
        request.setUid(uuid);

        mvc.perform(post(URL_API_GATEWAY_USER_REGISTR).contentType(MediaType.APPLICATION_JSON).
                content(gson.toJson(request))
        ).andDo(print())
                .andExpect(status().isCreated());
    }

    @After
    public void tearDown() throws Exception {
        mvc.perform(delete(URL_API_GATEWAY_USER_DELETE).
                param("uuid", uuid.toString())).andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    public void getUser() throws Exception{
        mvc.perform(get(URL_API_GATEWAY_USER_GET).
                param("uuid", uuid.toString())).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUserAll() throws Exception{
        mvc.perform(get(URL_API_GATEWAY_USER_ALL).
                param("uuid", uuid.toString())).andDo(print())
                .andExpect(status().isOk());
    }
  
    @Test
    public void getStatAll() throws Exception{
        mvc.perform(get(URL_API_GATEWAY_STATS_FIND_ALL).
                param("uuid", uuid.toString())).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getStatWeek()throws Exception {
        mvc.perform(get(URL_API_GATEWAY_STATS_FIND_WEEK).
                param("uuid", uuid.toString())).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getStatMonth()throws Exception {
        mvc.perform(get(URL_API_GATEWAY_STATS_FIND_MONTH).
                param("uuid", uuid.toString())).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getStat() throws Exception{
        mvc.perform(get(URL_API_GATEWAY_STAT_GET).
                param("uuid", uuid.toString())).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getStatOnlineAll() throws Exception{
        mvc.perform(get(URL_API_GATEWAY_STATONLINE_FIND_ALL).
                param("uuid", uuid.toString())).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getStatOnlineDay() throws Exception{
        mvc.perform(get(URL_API_GATEWAY_STATONLINE_FIND_DAY).
                param("uuid", uuid.toString())).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getStatOnlineWeek() throws Exception{
        mvc.perform(get(URL_API_GATEWAY_STATONLINE_FIND_WEEK).
                param("uuid", uuid.toString())).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getStatOnlineMonth() throws Exception{
        mvc.perform(get(URL_API_GATEWAY_STATONLINE_FIND_MONTH).
                param("uuid", uuid.toString())).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getStatOnline() throws Exception{
        mvc.perform(get(URL_API_GATEWAY_STATONLINE_GET).
                param("uuid", uuid.toString())).andDo(print())
                .andExpect(status().isOk());
    }

   
}