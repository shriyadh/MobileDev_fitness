package edu.northeastern.team1;

public class Message {
    private Long mid;
    private String sender;
    private String image;

    public Message(Long mid, String sender, String image) {
        this.mid = mid;
        this.sender = sender;
        this.image = image;
    }

    public Long getMid() {
        return mid;
    }

    public String getSender() {
        return sender;
    }

    public String getImage() {
        return image;
    }
}
