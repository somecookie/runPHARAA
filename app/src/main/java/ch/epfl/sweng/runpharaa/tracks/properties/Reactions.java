package ch.epfl.sweng.runpharaa.tracks.properties;

class Reactions {

    private final int likes;
    private final int dislikes;

    Reactions() {
        this.likes = 0;
        this.dislikes = 0;
    }

    /**
     * Basic constructor with all arguments known
     * @param likes an int
     * @param dislikes an int
     */
    Reactions(int likes, int dislikes) {
        if (likes < 0) {
            throw new IllegalArgumentException("likes argument strictly smaller than zero");
        }
        if (dislikes < 0) {
            throw new IllegalArgumentException("dislikes argument strictly smaller than zero");
        }
        this.likes = likes;
        this.dislikes = dislikes;
    }

    /**
     * likes getter
     * @return the likes
     */
    int getLikes() {
        return likes;
    }

    /**
     * dislikes getter
     * @return the dislikes
     */
    int getDislikes() {
        return dislikes;
    }

    /**
     * Create a new {@link Reactions} with the same attributes as the current one but with one more like
     * @return the new {@link Reactions}
     */
    Reactions withNewLike()
    {
        return new Reactions(likes + 1, dislikes);
    }

    /**
     * Create a new {@link Reactions} with the same attributes as the current one but with one more dislike
     * @return the new {@link Reactions}
     */
    Reactions withNewDislike()
    {
        return new Reactions(likes, dislikes + 1);
    }

    /**
     * Return the number of likes minus the number of dislikes
     * @return difference of likes minus the dislikes
     */
    int getLikesCount()
    {
        return likes - dislikes;
    }

    /**
     * Compute the ratio of likes and return it,
     * It return 1, if there is no likes or dislikes
     * @return the ratio as a double
     */
    double getLikesRatio() {
        if (getTotalReactions() > 0) {
            return likes / (likes + dislikes);
        } else {
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