package hello.api.gateway.models;

import java.util.Date;
import java.util.UUID;

public class StatisticInfo {

    private Integer subscribers;
    private Integer photo;
    private Integer wall;
    private String info;
    private Integer video;
    private Integer audio;
    private Integer groups;
    private String lastactive;
    private Date date;
    private UUID uid;


    public Integer getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Integer subscribers) {
        this.subscribers = subscribers;
    }

    public Integer getPhoto() {
        return photo;
    }

    public void setPhoto(Integer photo) {
        this.photo = photo;
    }

    public Integer getWall() {
        return wall;
    }

    public void setWall(Integer wall) {
        this.wall = wall;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getVideo() {
        return video;
    }

    public void setVideo(Integer video) {
        this.video = video;
    }

    public Integer getAudio() {
        return audio;
    }

    public void setAudio(Integer audio) {
        this.audio = audio;
    }

    public Integer getGroups() {
        return groups;
    }

    public void setGroups(Integer groups) {
        this.groups = groups;
    }

    public String getLastactive() {
        return lastactive;
    }

    public void setLastactive(String lastactive) {
        this.lastactive = lastactive;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }
}
