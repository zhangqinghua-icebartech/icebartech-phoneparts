package com.icebartech.core.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SliderPhotos implements Serializable {

    private static final long serialVersionUID = 46637955973475860L;

    private String activityName;
    private Integer isActivity;
    private String activityUrl;
    private String imageUrl;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    // public Date getGmtModified() {
    // 	return (Date) gmtModified.clone();
    // }
    // public void setGmtModified(Date gmtModified) {
    // 	this.gmtModified = (Date) gmtModified.clone();
    // }
    // public Date getGmtCreated() {
    // 	return (Date) gmtCreated.clone();
    // }
    // public void setGmtCreated(Date gmtCreated) {
    // 	this.gmtCreated = (Date) gmtCreated.clone();
    // }
    // public String getActivityName() {
    // 	return activityName;
    // }
    // public void setActivityName(String activityName) {
    // 	this.activityName = activityName;
    // }
    // public Integer getIsActivity() {
    // 	return isActivity;
    // }
    // public void setIsActivity(Integer isActivity) {
    // 	this.isActivity = isActivity;
    // }
    // public String getActivityUrl() {
    // 	return activityUrl;
    // }
    // public void setActivityUrl(String activityUrl) {
    // 	this.activityUrl = activityUrl;
    // }
    // public String getImageUrl() {
    // 	return imageUrl;
    // }
    // public void setImageUrl(String imageUrl) {
    // 	this.imageUrl = imageUrl;
    // }
}
