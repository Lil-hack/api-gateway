package hello.api.gateway.models;

import java.util.UUID;

public class UserInfo {
    private String vk;
    private String email;
    private String password;
    private boolean identify;
    private UUID uid;

    public String getVk() {
        return vk;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean getIdentify() {
        return identify;
    }

    public void setIdentify(boolean identify) {
        this.identify = identify;
    }

    public UUID getUid() {
        return uid;
    }

    public void setVk(String vk) {
        this.vk = vk;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }
}
