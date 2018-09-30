package ch.epfl.sweng.runpharaa;

public class CardItem {

    private int background;
    private String name;

    public CardItem() {

    }

    public CardItem(int background, String name) {
        this.background = background;
        this.name = name;
    }

    public int getBackground() {
        return background;
    }

    public String getName() {
        return name;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setName(String name) {
        this.name = name;
    }
}
