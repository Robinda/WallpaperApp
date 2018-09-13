package com.example.robin.demoapp.models;

import java.util.ArrayList;

public class SectionDataModel {

    private String headerTitle;
    private ArrayList<SingleItemModel> allItemsInSection;
    private Category category;

    public SectionDataModel() {
    }

    public SectionDataModel(String headerTitle, ArrayList<SingleItemModel> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public Category getHeaderCategory() {
        return category;
    }

    public void setHeaderCategory(Category category) {
        this.category = category;
    }

    public ArrayList<SingleItemModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<SingleItemModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }
}
