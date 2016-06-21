package com.example.dinesh.wikisearch;

/**
 * Created by Dinesh on 6/18/2016.
 */
public class ImageStore {
    private String imgSrc;
    private String pgTitle;
    private int imgWidth;
    private int imgHeight;
    private int pgId;

    public ImageStore() {}

    public ImageStore(String imgSrc, String pgTitle, int imgWidth, int imgHeight, int pgId){
        this.imgSrc = imgSrc;
        this.pgTitle = pgTitle;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.pgId = pgId;
    }

    public String getImgSrc(){
        return imgSrc;
    }

    public void setImgSrc(String url){
        this.imgSrc = url;
    }

    public String getPgTitle(){
        return pgTitle;
    }

    public void setPgTitle(String title){
        this.pgTitle = title;
    }

    public int getImgWidth(){
        return imgWidth;
    }

    public void setImgWidth(int width){
        this.imgWidth = width;
    }

    public int getImgHeight(){
        return imgHeight;
    }

    public void setImgHeight(int height){
        this.imgHeight = height;
    }

    public int getPgId(){
        return pgId;
    }

    public void setPgId(int pgId){
        this.pgId = pgId;
    }
}
