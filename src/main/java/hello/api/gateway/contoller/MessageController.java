package hello.api.gateway.contoller;

import hello.api.gateway.config.Student;
import hello.api.gateway.config.StudentRepository;
import hello.api.gateway.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-redis-cache
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 23/12/17
 * Time: 06.07
 * To change this template use File | Settings | File Templates.
 */

@RestController
public class MessageController {
    @Autowired
    private CacheService cacheService;

    @Autowired
    private StudentRepository statisticRepos;

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    @ResponseBody
    public String greeting(String user) {

        Student student = new Student(
                "Eng2015001", "John Doe", Student.Gender.MALE, 1);
        statisticRepos.save(student);

        return "lox";
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    @ResponseBody
    public String saveGreeting(String user, String message) {

        cacheService.addMessage(user, message);

        return "OK";

    }
}
