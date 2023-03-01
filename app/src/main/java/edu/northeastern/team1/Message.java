package edu.northeastern.team1;

public class Message {
    private Integer mid;
    private String sender;
    private String image;

    public Message(Integer mid, String sender, String image) {
        this.mid = mid;
        this.sender = sender;
        this.image = image;
    }

    public Integer getMid() {
        return mid;
    }

    public String getSender() {
        return sender;
    }

    public String getImage() {
        return image;
    }
}
