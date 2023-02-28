package edu.northeastern.team1;

public class Message {
    private Integer messageID;
    private String username;
    private String imgUrl;
    private Integer sentOn;


    public Message(Integer messageID, String username, String imgUrl, Integer sentOn) {
        this.messageID = messageID;
        this.username = username;
        this.imgUrl = imgUrl;
        this.sentOn = sentOn;
    }

    public Integer getMessageID() {
        return messageID;
    }

    public String getUsername() {
        return username;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Integer getSentOn() {
        return sentOn;
    }
}
