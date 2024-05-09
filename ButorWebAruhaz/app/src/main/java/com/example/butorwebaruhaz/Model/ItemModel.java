package com.example.butorwebaruhaz.Model;

public class ItemModel {

    String itemImage, itemTitle, itemDescription, documentId;

    public ItemModel() {
    }

    public ItemModel(String itemImage, String itemTitle, String itemDescription, String documentId) {
        this.itemImage = itemImage;
        this.itemTitle = itemTitle;
        this.itemDescription = itemDescription;
        this.documentId = documentId;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
