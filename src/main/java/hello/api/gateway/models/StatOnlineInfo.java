package hello.api.gateway.models;

import java.util.Date;
import java.util.UUID;

public class StatOnlineInfo {

    private Date date;
    private boolean online;
    private boolean mobile;
    private UUID uid;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }
}
