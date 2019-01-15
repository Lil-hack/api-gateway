package hello.api.gateway.models;


import java.util.Date;
import java.util.UUID;

public class StatisticInfo {

    private String photoUrl;
    private String first_name;
    private String last_name;
    private Integer albums;
    private Integer videos;
    private Integer audios;
    private Integer notes;
    private Integer photos;
    private Integer groups;
    private Integer gifts;
    private Integer friends;
    private Integer online_friends;
    private Integer user_photos;
    private Integer followers;
    private Integer subscriptions;
    private Integer pages;
    private Date date;
    private UUID uid;

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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Integer getAlbums() {
        return albums;
    }

    public void setAlbums(Integer albums) {
        this.albums = albums;
    }

    public Integer getVideos() {
        return videos;
    }

    public void setVideos(Integer videos) {
        this.videos = videos;
    }

    public Integer getAudios() {
        return audios;
    }

    public void setAudios(Integer audios) {
        this.audios = audios;
    }

    public Integer getNotes() {
        return notes;
    }

    public void setNotes(Integer notes) {
        this.notes = notes;
    }

    public Integer getPhotos() {
        return photos;
    }

    public void setPhotos(Integer photos) {
        this.photos = photos;
    }

    public Integer getGroups() {
        return groups;
    }

    public void setGroups(Integer groups) {
        this.groups = groups;
    }

    public Integer getGifts() {
        return gifts;
    }

    public void setGifts(Integer gifts) {
        this.gifts = gifts;
    }

    public Integer getFriends() {
        return friends;
    }

    public void setFriends(Integer friends) {
        this.friends = friends;
    }

    public Integer getOnline_friends() {
        return online_friends;
    }

    public void setOnline_friends(Integer online_friends) {
        this.online_friends = online_friends;
    }

    public Integer getUser_photos() {
        return user_photos;
    }

    public void setUser_photos(Integer user_photos) {
        this.user_photos = user_photos;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Integer subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

}
