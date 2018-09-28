package ch.epfl.sweng.runpharaa;

public class Reactions {

    private static int likes;
    private static int dislikes;

    public Reactions(int likes, int dislikes)
    {
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public static int getLikes() {
        return likes;
    }

    public static void setLikes(int likes) {
        Reactions.likes = likes;
    }

    public static int getDislikes() {
        return dislikes;
    }

    public static void setDislikes(int dislikes) {
        Reactions.dislikes = dislikes;
    }

    public static void addLike()
    {
        likes++;
    }

    public static void addDislike()
    {
        dislikes++;
    }

    public static int getLikesCount()
    {
        return likes - dislikes;
    }

    public static double getLikesRatio()
    {
        return likes / (likes + dislikes);
    }
}
