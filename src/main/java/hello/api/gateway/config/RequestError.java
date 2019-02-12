package hello.api.gateway.config;


import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("RequestError")
public class RequestError implements Serializable {


    private String id;
    private UUID uuid;
    private String url;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}