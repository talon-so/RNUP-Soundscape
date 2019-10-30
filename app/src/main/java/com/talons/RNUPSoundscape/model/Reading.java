package com.talons.RNUPSoundscape.model;

import java.io.Serializable;

public class Reading implements Serializable
{

    private String reportingDate;
    private String gpsLatitude;
    private String gpsLongitude;
    private String averageDecibel;
    private String deviceModel;
    private String deviceApiLevel;
    private String requestSignature;
    private final static long serialVersionUID = -2939883517946895169L;

    public Reading(Builder builder) {
        this.reportingDate = builder.reportingDate;
        this.gpsLatitude = builder.gpsLatitude;
        this.gpsLongitude = builder.gpsLongitude;
        this.averageDecibel = builder.averageDecibel;
        this.deviceModel = builder.deviceModel;
        this.deviceApiLevel = builder.deviceApiLevel;
        this.requestSignature = builder.requestSignature;

    }

    public String getRequestSignature() {
        return requestSignature;
    }

    public void setRequestSignature(String requestSignature) {
        this.requestSignature = requestSignature;
    }

    public String getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(String reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getGpsLatitude() {
        return gpsLatitude;
    }

    public void setGpsLatitude(String gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }

    public String getGpsLongitude() {
        return gpsLongitude;
    }

    public void setGpsLongitude(String gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    public String getAverageDecibel() {
        return averageDecibel;
    }

    public void setAverageDecibel(String averageDecibel) {
        this.averageDecibel = averageDecibel;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceApiLevel() {
        return deviceApiLevel;
    }

    public void setDeviceApiLevel(String deviceApiLevel) {
        this.deviceApiLevel = deviceApiLevel;
    }

    // Static class Builder
    public static class Builder {

        private String reportingDate;
        private String gpsLatitude;
        private String gpsLongitude;
        private String averageDecibel;
        private String deviceModel;
        private String deviceApiLevel;
        private String requestSignature;

        public static Builder newInstance() {
            return new Builder();
        }

        private Builder() {}

        // Setter methods
        public Builder setReportingDate(String reportingDate) {
            this.reportingDate = reportingDate;
            return this;
        }
        public Builder setGpsLat(String gpsLatitude) {
            this.gpsLatitude = gpsLatitude;
            return this;
        }
        public Builder setGpsLong(String gpsLongitude)
        {
            this.gpsLongitude = gpsLongitude;
            return this;
        }
        public Builder setAverageDecibel(String averageDecibel)
        {
            this.averageDecibel = averageDecibel;
            return this;
        }
        public Builder setDeviceModel(String deviceModel)
        {
            this.deviceModel = deviceModel;
            return this;
        }
        public Builder setDeviceApiLevel(String deviceApiLevel)
        {
            this.deviceApiLevel = deviceApiLevel;
            return this;
        }
        public Builder setRequestSignature(String requestSignature)
        {
            this.requestSignature = requestSignature;
            return this;
        }

        // build method to deal with outer class
        // to return outer instance
        public Reading build() {
            return new Reading(this);
        }
    }

}