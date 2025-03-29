package org.example.artifactcatalog2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;


public class Artifact {

    public Artifact(String ID, String name, String category, String discoveryLocation, ArrayList<String> composition, String civilization, LocalDate discoveryDate, String currentPlace, Dimension dimension, long weight, ArrayList<String> tags) {
        this.ID = ID;
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

    public Artifact() {
    }


    private String ID;
    private String name;
    private String category;
    private String discoveryLocation;
    private ArrayList<String> composition;
    private String civilization;
    private LocalDate discoveryDate; //year-month-day
    private String currentPlace;
    private Dimension dimension;
    private long weight;
    private ArrayList<String> tags;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public ArrayList<String> getComposition() {
        return composition;
    }

    public void setComposition(ArrayList<String> composition) {
        this.composition = composition;
    }

    public String getCivilization() {
        return civilization;
    }

    public void setCivilization(String civilization) {
        this.civilization = civilization;
    }

    public LocalDate getDiscoveryDate() {
        return discoveryDate;
    }

    public void setDiscoveryDate(LocalDate discoveryDate) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Artifact artifact = (Artifact) o;
        return Objects.equals(ID, artifact.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
