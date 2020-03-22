package com.example.mynews;

public class News {
    private String title;
    private  String url;
    private  String sectionName;
    //Constructor
    public News(String title,String url,String  name)
    {
        this.title=title;
        this.url= url;
        sectionName=name;
    }

    //getter method for title
    public String getTitle() {
        return title;
    }
    //getter method for url
    public String getUrl() {
        return url;
    }
    //getter method for section name
    public String getSectionName() {
        return sectionName;
    }
}
