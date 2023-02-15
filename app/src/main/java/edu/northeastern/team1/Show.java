package edu.northeastern.team1;

public class Show {

    private String name;

    private String description;

    private String year;

    private String pic_url;

    private double rating;

    public Show(String name, String des, String pic, double rate, String year ) {

        this.name = name;
        this.description = des;
        this.year = year;
        this.pic_url = pic;
        this.rating = rate;

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

    public double getRating(){
        return this.rating;
    }

    public String getDescription(){
        return this.description;
    }


}
