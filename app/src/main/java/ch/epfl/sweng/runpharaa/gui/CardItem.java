package ch.epfl.sweng.runpharaa.gui;

public class CardItem {
    private String name;
    private String imageUrl;

    CardItem() {
    }

    CardItem(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    /**
     * Get the CardItem name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set a CardItem name
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the CardItem image URL
     *
     * @return the image URL
     */
    public String getImageURL() {
        return this.imageUrl;
    }
}
