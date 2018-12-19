package ch.epfl.sweng.runpharaa.tracks.properties;

import java.util.Set;

import ch.epfl.sweng.runpharaa.utils.Required;

public class TrackProperties {
    private double length;
    private double heightDifference;
    private AvgDuration avgDur;
    private AvgDifficulty avgDiff;
    private int likes;
    private int favorites;
    private Set<TrackType> trackType;

    //For Firebase
    public TrackProperties(int likes, int favorites, double length, double heightDiff, double durationTotal, int durationNbr, int difficultyTotal, int difficultyNbr, Set<TrackType> trackType){
        Required.greaterOrEqualZero(length, "The length of the track must be positive");
        Required.greaterOrEqualZero(likes, "The likes of the track must be positive");
        Required.greaterOrEqualZero(favorites, "The favorites of the track must be positive");
        Required.greaterOrEqualZero(durationTotal, "The total duration of the track must be positive");
        Required.greaterOrEqualZero(durationNbr, "The number of durations of the track must be positive");
        Required.greaterOrEqualZero(difficultyTotal, "The total difficulties of the track must be positive");
        Required.greaterOrEqualZero(difficultyNbr, "The number of difficulties of the track must be positive");
        Required.nonNull(trackType, "The set of types must be non-null");
        if(trackType.isEmpty()) throw new IllegalArgumentException("There must be at least one track type");

        this.likes = likes;
        this.favorites = favorites;
        this.length = length;
        this.heightDifference = heightDiff;
        avgDur = new AvgDuration(durationTotal, durationNbr);
        avgDiff = new AvgDifficulty(difficultyTotal, difficultyNbr);
        this.trackType = trackType;
    }

    public TrackProperties(double length, double heightDiff, double duration, int difficulty, Set<TrackType> trackType){
        Required.greaterOrEqualZero(length, "The length of the track must be positive");
        Required.greaterOrEqualZero(duration, "The duration of the track must be positive");
        if(difficulty < 0 || difficulty > 5) throw new IllegalArgumentException("The difficulty must be between 0 and 5");
        Required.nonNull(trackType, "The set of types must be non-null");
        if(trackType.isEmpty()) throw new IllegalArgumentException("There must be at least one track type");

        this.length = length;
        this.heightDifference = heightDiff;
        avgDur = new AvgDuration(duration);
        avgDiff = new AvgDifficulty(difficulty);
        this.trackType = trackType;
    }

    /**
     * Get average difficulty.
     *
     * @return double
     */
    public double getAvgDifficulty(){
        return avgDiff.getAverage();
    }

    /**
     * Add new difficulty.
     *
     * @param diff
     */
    public void addNewDifficulty(int diff){
        avgDiff.add(diff);
    }

    /**
     * Get total average difficulty.
     *
     * @return int
     */
    public int getAvgDifficultyTotal() { return avgDiff.getTotal();}

    /**
     * Get average difficulty number.
     *
     * @return int
     */
    public int getAvgDifficultyNbr() { return avgDiff.getNbr();}

    /**
     * Get average duration.
     *
     * @return
     */
    public double getAvgDuration(){
        return avgDur.getAverage();
    }

    /**
     * Add new duration.
     *
     * @param dur
     */
    public void addNewDuration(double dur){
        avgDur.add(dur);
    }

    /**
     * Get average total duration.
     *
     * @return double
     */
    public double getAvgDurationTotal() { return avgDur.getTotal();}

    /**
     * Get average duration number.
     *
     * @return int
     */
    public int getAvgDurationNbr() { return avgDur.getNbr();}

    /**
     * Add like.
     *
     */
    public void addLike(){ likes++; }

    /**
     * Remove like.
     *
     */
    public void removeLike(){ if(likes > 0) likes--; }

    /**
     * Get number of likes.
     *
     * @return int
     */
    public int getLikes(){ return likes; }

    /**
     * Add favourite.
     *
     */
    public void addFavorite(){ favorites++; }

    /**
     * Remove favourite.
     *
     */
    public void removeFavorite(){ if(favorites > 0) favorites--; }

    /**
     * Get number of favourites.
     *
     * @return int
     */
    public int getFavorites(){ return favorites; }

    /**
     * Get track type.
     *
     * @return Set<TrackType>
     */
    public Set<TrackType> getType(){return trackType;}

    /**
     * Get length.
     *
     * @return double
     */
    public double getLength() {
        return length;
    }

    /**
     * Get height difference.
     *
     * @return double
     */
    public double getHeightDifference() {
        return heightDifference;
    }

    /**
     * Set length.
     *
     * @param length
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Set height difference.
     *
     * @param heightDifference
     */
    public void setHeightDifference(double heightDifference) {
        this.heightDifference = heightDifference;
    }

    /**
     * Set number of likes.
     *
     * @param likes
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }

    /**
     * Set number of favourites.
     *
     * @param favorites
     */
    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    /**
     * Class that represents the average difficulty of a track in a range from 0 to 5
     */
    private class AvgDifficulty {
        private int total;
        private int nbr;

        /**
         * Create an average difficulty for a track (To use when creating a new track)
         * @param firstDiff
         */
        AvgDifficulty(int firstDiff){
            if(firstDiff < 0 || firstDiff > 5) throw new IllegalArgumentException("AvgDifficulty must be in the range [0,5]");
            total = firstDiff;
            nbr = 1;
        }

        /**
         * Create an Average difficulty for a track
         * @param total the summed durations
         * @param nbr the number of durations
         */
        AvgDifficulty(int total, int nbr){
            if(total < 0) throw new IllegalArgumentException("Total difficulty must be positive");
            if(nbr <= 0) throw new IllegalArgumentException("Number of difficulties must be greater than zero");
            this.total = total;
            this.nbr = nbr;
        }

        /**
         * Add a new difficulty to the average
         * @param newDiff
         */
        void add(int newDiff){
            if(newDiff < 0 || newDiff > 5) throw new IllegalArgumentException("AvgDifficulty must be in the range [0,5]");
            total += newDiff;
            nbr++;
        }

        /**
         * Get average.
         *
         * @return double
         */
        double getAverage(){
            return (double)total/nbr;
        }

        /**
         * Get total.
         *
         * @return int
         */
        int getTotal() { return total;}

        /**
         * Get number.
         *
         * @return int
         */
        int getNbr() { return nbr; }
    }

    /**
     * Class that represents the average duration of a track in minutes
     */
    private class AvgDuration{
        private double total;
        private int nbr;

        /**
         * Create an average duration for a track (To use when creating a new track)
         * @param firstDuration
         */
        AvgDuration(double firstDuration){
            if(firstDuration < 0) throw new IllegalArgumentException("Duration for a track must be positive");
            nbr = 1;
            total = firstDuration;
        }

        /**
         * Create an Average duration for a track
         * @param total the summed durations
         * @param nbr the number of durations
         */
        AvgDuration(double total, int nbr){
            if(total < 0) throw new IllegalArgumentException("Total duration must be positive");
            if(nbr <= 0) throw new IllegalArgumentException("Number of durations must be greater than zero");
            this.total = total;
            this.nbr = nbr;
        }

        /**
         * Add a new duration to the average
         * @param newDuration
         */
        void add(double newDuration){
            if(newDuration < 0) throw new IllegalArgumentException("Duration for a track must be positive");
            nbr++;
            total += newDuration;
        }

        /**
         * Get average.
         *
         * @return double
         */
        double getAverage(){
            return total/nbr;
        }

        /**
         * Get total.
         *
         * @return double
         */
        double getTotal() { return total; }

        /**
         * Get number.
         *
         * @return int
         */
        int getNbr() { return nbr; }
    }
}
