package ch.epfl.sweng.runpharaa;

public class Reactions {

    private final int likes;
    private final int dislikes;

    public Reactions()
    {
        this.likes = 0;
        this.dislikes = 0;
    }

    /**
     * Basic constructor with all arguments known
     * @param likes
     * @param dislikes
     */
    public Reactions(int likes, int dislikes) {
        if(likes < 0)
        {
            throw new IllegalArgumentException("likes argument strictly smaller than zero");
        }
        if(dislikes < 0)
        {
            throw new IllegalArgumentException("dislikes argument strictly smaller than zero");
        }
        this.likes = likes;
        this.dislikes = dislikes;
    }

    /**
     * likes getter
     * @return
     */
    public int getLikes() {
        return likes;
    }

    /**
     * dislikes getter
     * @return
     */
    public int getDislikes() {
        return dislikes;
    }

    /**
     * Create a new {@link Reactions} with the same attributes as the current one but with one more like
     * @return the new {@link Reactions}
     */
    public Reactions withNewLike()
    {
        return new Reactions(likes + 1, dislikes);
    }

    /**
     * Create a new {@link Reactions} with the same attributes as the current one but with one more dislike
     * @return the new {@link Reactions}
     */
    public Reactions withNewDislike()
    {
        return new Reactions(likes, dislikes + 1);
    }

    /**
     * Return the number of likes minus the number of dislikes
     * @return
     */
    public int getLikesCount()
    {
        return likes - dislikes;
    }

    /**
     * Compute the ratio of likes and return it,
     * It return 1, if there is no likes or dislikes
     * @return the ratio as a double
     */
    public double getLikesRatio()
    {
        if(getTotalReactions() > 0)
        {
            return likes / (likes + dislikes);
        }
        else
        {
            return 1;
        }
    }

    /**
     * Return the total number of reactions
     * @return likes + dislikes
     */
    private int getTotalReactions()
    {
        return likes + dislikes;
    }
}