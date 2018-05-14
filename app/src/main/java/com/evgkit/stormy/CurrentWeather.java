package com.evgkit.stormy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CurrentWeather {
    private String locationLabel;
    private String icon;
    private Long time;
    private Double temperature;
    private Double humidity;
    private Double precipChance;
    private String summary;
    private String timezone;

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public String getIcon() {
        return icon;
    }

    public Integer getIconId() {
        try {
            return R.drawable.class
                    .getField(icon.replaceAll("-", "_"))
                    .getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getTime() {
        return time;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        return formatter.format(new Date(time * 1000));
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(Double precipChance) {
        this.precipChance = precipChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
