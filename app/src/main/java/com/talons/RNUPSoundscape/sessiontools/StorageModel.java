package com.talons.RNUPSoundscape.sessiontools;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StorageModel implements Serializable {
    private long epochSeconds;
    private double averageDecibels;
    private String phoneModel;
    private String phoneApiLevel;
    private double latitude;
    private double longitude;

    public StorageModel(double averageDecibels, double latitude, double longitude, String phoneModel, String phoneApiLevel, long epochSeconds){
        this.epochSeconds = epochSeconds;
        this.averageDecibels = averageDecibels;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneModel = phoneModel;
        this.phoneApiLevel = phoneApiLevel;
    }
    /**
     * converts UNIX epoch time into SimpleDateFormat and returns it to user
     * @return a string of the departing time
     */
    public String getStandardTime(){

        SimpleDateFormat date = new SimpleDateFormat("MM/dd/yy h:mma", Locale.CANADA);
        // call depart time method to input epoch time into formatter
        // we * by 1000 to convert Unix epoch time to epoch time (seconds to milliseconds)
        String dateStr = date.format( new Date( (this.epochSeconds) * 1000 ) );
        return dateStr;
    }

    public long getEpochSeconds() {
        return epochSeconds;
    }

    public void setEpochSeconds(long epochSeconds) {
        this.epochSeconds = epochSeconds;
    }

    public double getAverageDecibels() {
        return averageDecibels;
    }

    public void setAverageDecibels(double averageDecibels) {
        this.averageDecibels = averageDecibels;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getPhoneApiLevel() {
        return phoneApiLevel;
    }

    public void setPhoneApiLevel(String phoneApiLevel) {
        this.phoneApiLevel = phoneApiLevel;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
