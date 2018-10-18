package com.unlogicon.typegram.models.posts;

public class PostArticle {

    private String title;
    private String ogimage;
    private String body;
    private String tag;

    public PostArticle(String title, String ogimage, String body, String tag){
        this.title = title;
        this.ogimage = ogimage;
        this.body = body;
        this.tag = tag;
    }
}
