package com.example.apitask;

import android.os.Parcel;
import android.os.Parcelable;

public class apps implements Parcelable{
    private int app_id;
    private String appName;
    private double appPrice;
    private int appAgeLimit;
    private String appImage;

    public apps(int app_id, String appName, double appPrice, int appAgeLimit, String appImage)
    {
        this.app_id = app_id;
        this.appName = appName;
        this.appPrice = appPrice;
        this.appAgeLimit = appAgeLimit;
        this.appImage = appImage;
    }

    protected apps(Parcel in)
    {
        app_id = in.readInt();
        appName = in.readString();
        appPrice = in.readFloat();
        appAgeLimit = in.readInt();
        appImage = in.readString();
    }

    public static final Parcelable.Creator<apps> CREATOR = new Parcelable.Creator<apps>() {
        @Override
        public apps createFromParcel(Parcel in) { return new apps(in); }

        @Override
        public apps[] newArray(int size) { return new apps[size]; }
    };

    public void setApp_id(int app_id) { this.app_id = app_id; }
    public void setAppName(String appName) { this.appName = appName; }
    public void setAppPrice(double appPrice) { this.appPrice = appPrice; }
    public void setAppAgeLimit(int appAgeLimit) { this.appAgeLimit = appAgeLimit; }
    public void setAppImage(String appImage) { this.appImage = appImage; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeInt(app_id);
        parcel.writeString(appName);
        parcel.writeDouble(appPrice);
        parcel.writeInt(appAgeLimit);
        parcel.writeString(appImage);
    }

    public int getApp_id() { return app_id; }
    public String getAppName() { return appName; }
    public double getAppPrice() { return appPrice; }
    public int getAppAgeLimit() { return appAgeLimit; }
    public String getAppImage() { return appImage; }
}
