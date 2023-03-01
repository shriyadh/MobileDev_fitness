package edu.northeastern.team1;

public class StickerLedger {
    private String count;
    private String url;

    public StickerLedger(String url, String count) {
        this.url = url;
        this.count = count;
    }

    public String getUrl(){ return this.url;}

    public String getCount(){ return this.count;}
}
