package com.ce216;

import java.util.ArrayList;

public class Artifact {
    private String ID;
    private String name;
    private String category;
    private String discoveryLocation;
    private String composition;
    private String civilization;
    private String discoveryDate;
    private String currentPlace;
    private Dimension dimension;
    private long weight;
    private ArrayList<String> tags;

    public Artifact(String iD, String name, String category, String discoveryLocation, String composition,
            String civilization, String discoveryDate, String currentPlace, Dimension dimension, long weight,
            ArrayList<String> tags) {
        ID = iD;
        this.name = name;
        this.category = category;
        this.discoveryLocation = discoveryLocation;
        this.composition = composition;
        this.civilization = civilization;
        this.discoveryDate = discoveryDate;
        this.currentPlace = currentPlace;
        this.dimension = dimension;
        this.weight = weight;
        this.tags = tags;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDiscoveryLocation() {
        return discoveryLocation;
    }

    public void setDiscoveryLocation(String discoveryLocation) {
        this.discoveryLocation = discoveryLocation;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getCivilization() {
        return civilization;
    }

    public void setCivilization(String civilization) {
        this.civilization = civilization;
    }

    public String getDiscoveryDate() {
        return discoveryDate;
    }

    public void setDiscoveryDate(String discoveryDate) {
        this.discoveryDate = discoveryDate;
    }

    public String getCurrentPlace() {
        return currentPlace;
    }

    public void setCurrentPlace(String currentPlace) {
        this.currentPlace = currentPlace;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

}
