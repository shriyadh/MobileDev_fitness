package edu.northeastern.team1;

public class Show {
    private double score;

    private String name;

    private String description;

    private String year;

    private String pic_url;

    private String rating;

    public Show(double score, String name, String des, String pic, String rate, String year ) {
        this.score = score < 0 ? 0 : score;
        this.name = name == null ? "": name;
        this.description = des == null ? "" : des;
        this.year = year == null ? "" : year;
        this.pic_url = pic == null ? "" : pic;
        this.rating = rate == null ? "" : rate;

    }

    public double getScore() {
        return this.score;
    }

    public String getName(){
        return this.name;

    }

    public String getPicture(){
        return this.pic_url;
    }

    public String getYear(){
        return this.year;
    }

    public String getRating(){
        return this.rating;
    }

    public String getDescription(){
        return this.description;
    }


}
