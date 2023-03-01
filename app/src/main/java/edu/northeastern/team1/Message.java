package edu.northeastern.team1;

public class Message {
    private Integer mid;
    private String sentBy;
    private String image;

    public Message(Integer mid, String sentBy, String image) {
        this.mid = mid;
        this.sentBy = sentBy;
        this.image = image;
    }

    public Integer getMid() {
        return mid;
    }

    public String getSentBy() {
        return sentBy;
    }

    public String getImage() {
        return image;
    }
}
