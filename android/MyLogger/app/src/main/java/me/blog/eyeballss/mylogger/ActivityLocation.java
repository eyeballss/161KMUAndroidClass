package me.blog.eyeballss.mylogger;

/**
 * Created by kesl on 2016-06-02.
 */
public class ActivityLocation {

    private double latitude;
    private double longitude;

    ActivityLocation(double latitude, double longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

}
