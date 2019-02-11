package hello;


import hello.api.gateway.config.Student;
import hello.api.gateway.config.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import redis.clients.jedis.Jedis;


import javax.annotation.Resource;
import java.io.IOException;
import java.net.ServerSocket;


@SpringBootApplication
public class Application {


    public static void main(String[] args) {


        SpringApplication.run(Application.class, args);

    }


}
