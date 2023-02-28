package edu.northeastern.team1;

public class Conversations {

    private String user;
    private String conversation_id;

    public Conversations(String user, String conversation_id) {

        this.user = user;
        this.conversation_id = conversation_id;

    }

    public String getUser(){ return this.user;}

    public String getConversation_id() { return this.conversation_id; }
}
