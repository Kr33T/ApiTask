package com.example.apitask;

public class DataModal {

    // string variables for our name and job
    private String appName;
    private double appPrice;
    private int appAgeLimit;
    private String appImage;

    public DataModal(String appName, double appPrice, int appAgeLimit, String appImage) {
        this.appName = appName;
        this.appPrice = appPrice;
        this.appAgeLimit = appAgeLimit;
        this.appImage = appImage;
    }

    public void setAppName(String appName) { this.appName = appName; }
    public void setAppPrice(double appPrice) { this.appPrice = appPrice; }
    public void setAppAgeLimit(int appAgeLimit) { this.appAgeLimit = appAgeLimit; }
    public void setAppImage(String appImage) { this.appImage = appImage; }

    public String getAppName() { return appName; }
    public double getAppPrice() { return appPrice; }
    public int getAppAgeLimit() { return appAgeLimit; }
    public String getAppImage() { return appImage; }

}
