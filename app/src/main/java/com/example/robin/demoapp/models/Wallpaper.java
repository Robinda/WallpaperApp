package com.example.robin.demoapp.models;

import java.io.Serializable;

public class Wallpaper implements Serializable {

    private String id;
    private String name;
    private String author;
    private String downloads;
    private String url_image;
    private String url_thumbnail;
    private double rating;

    public Wallpaper() {
    }

    public Wallpaper(String id, String name, String url_image) {
        this.id = id;
        this.name = name;
        this.url_image = url_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDownloads() {
        return downloads;
    }

    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String getUrl_thumbnail() {
        return url_thumbnail;
    }

    public void setUrl_thumbnail(String url_thumbnail) {
        this.url_thumbnail = url_thumbnail;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}