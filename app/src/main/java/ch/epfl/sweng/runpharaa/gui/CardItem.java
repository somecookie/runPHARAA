package ch.epfl.sweng.runpharaa.gui;

public class CardItem {
    private String name;
    private String imageUrl;

    CardItem() {}

    CardItem(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getImageURL() { return this.imageUrl; }
}
