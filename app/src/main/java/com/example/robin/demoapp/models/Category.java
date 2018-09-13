package com.example.robin.demoapp.models;

import java.io.Serializable;

public class Category implements Serializable {

    private String id;      // Id de la catégorie
    private String label;   // Label de la catégorie

    public Category() {
    }

    public Category(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
