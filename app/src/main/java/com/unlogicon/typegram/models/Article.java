package com.unlogicon.typegram.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Nikita Korovkin 09.10.2018.
 */
@Entity
public class Article {

    @PrimaryKey
    @SerializedName("ID")
    @Expose
    private Integer iD;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("Author")
    @Expose
    private String author;

    @SerializedName("Image")
    @Expose
    private String image;

    @SerializedName("ogimage")
    @Expose
    private String ogimage;

    @SerializedName("CreatedAt")
    @Expose
    private String createdAt;

    @SerializedName("Lang")
    @Expose
    private String lang;

    @SerializedName("HTML")
    @Expose
    private String hTML;

    @SerializedName("Plus")
    @Expose
    private Integer plus;

    @SerializedName("Minus")
    @Expose
    private Integer minus;

    @SerializedName("ReadingTime")
    @Expose
    private Integer readingTime;

    @SerializedName("WordCount")
    @Expose
    private Integer wordCount;

    @SerializedName("tag")
    @Expose
    private String tag;

    @Ignore
    @SerializedName("Comments")
    @Expose
    private List<Article> comments = new ArrayList<>();

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOgimage() {
        return ogimage;
    }

    public void setOgimage(String ogimage) {
        this.ogimage = ogimage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getHTML() {
        return hTML;
    }

    public void setHTML(String hTML) {
        this.hTML = hTML;
    }

    public Integer getPlus() {
        return plus;
    }

    public void setPlus(Integer plus) {
        this.plus = plus;
    }

    public Integer getMinus() {
        return minus;
    }

    public void setMinus(Integer minus) {
        this.minus = minus;
    }


    public Integer getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(Integer readingTime) {
        this.readingTime = readingTime;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<Article> getComments() {
        return comments;
    }

    public void setComments(List<Article> comments) {
        this.comments = comments;
    }
}
