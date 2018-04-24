package com.example.ubuntu.seefood;

import java.util.Map;

/**
 * Created by ubuntu on 13/3/18.
 */

public class UserPreferences {
    public String userName;
    public String userEmail;
    public String userPhoto;
    public String userPhoneNumber;
    public Map<String, Boolean> allergies;
    public Map<String, Boolean> courses;
    public Map<String, Boolean> cuisines;
    public Map<String, Boolean> diet;
    public Map<String, Boolean> flavors;
    public String maxPrepTimeInSeconds;

    public UserPreferences() {
    }

    public UserPreferences(String name, String email, String photoUrl, String phoneNumber, Map<String, Boolean> aller, Map<String, Boolean> cour,
                           Map<String, Boolean> cuis, Map<String, Boolean> diet, Map<String, Boolean> flavors, String maxPrepTimeInSeconds) {
        this.userName = name;
        this.userEmail = email;
        this.userPhoto = photoUrl;
        this.userPhoneNumber = phoneNumber;
        this.allergies = aller;
        this.courses = cour;
        this.cuisines = cuis;
        this.diet = diet;
        this.flavors = flavors;
        this.maxPrepTimeInSeconds = maxPrepTimeInSeconds;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public Object getAllergies() {
        return allergies;
    }

    public Object getCourses() {
        return courses;
    }

    public Object getCuisines() {
        return cuisines;
    }

    public Object getDiet() {
        return diet;
    }

    public Object getFlavors() {
        return flavors;
    }

    public String getMaxPrepTimeInSeconds() {
        return maxPrepTimeInSeconds;
    }
}
