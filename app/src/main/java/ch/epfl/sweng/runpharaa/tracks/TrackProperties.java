package ch.epfl.sweng.runpharaa.tracks;

import com.google.firebase.database.Exclude;

import java.util.Set;

import ch.epfl.sweng.runpharaa.utils.Required;

public class TrackProperties {
    private double length;
    private double heightDifference;
    private AvgDuration avgDur;
    private AvgDifficulty avgDiff;
    private int likes;
    private int favorites;
    @Exclude
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

    @Exclude //TODO think about how to put it in databse
    public double getAvgDifficulty(){
        return avgDiff.getAverage();
    }
    public void addNewDifficulty(int diff){
        avgDiff.add(diff);
    }
    public int getAvgDifficultyTotal() { return avgDiff.getTotal();}
    public int getAvgDifficultyNbr() { return avgDiff.getNbr();}

    @Exclude //TODO think about how to put it in databse
    public double getAvgDuration(){
        return avgDur.getAverage();
    }
    public void addNewDuration(double dur){
        avgDur.add(dur);
    }
    public double getAvgDurationTotal() { return avgDur.getTotal();}
    public int getAvgDurationNbr() { return avgDur.getNbr();}

    public void addLike(){ likes++; }
    public void removeLike(){ if(likes > 0) likes--; }
    public int getLikes(){ return likes; }

    public void addFavorite(){ favorites++; }
    public void removeFavorite(){ if(favorites > 0) favorites--; }
    public int getFavorites(){ return favorites; }

    @Exclude //TODO exclude for now but make f(string) -> TrackType for database (@Hugo for more explications)
    public Set<TrackType> getType(){return trackType;}

    public double getLength() {
        return length;
    }

    public double getHeightDifference() {
        return heightDifference;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setHeightDifference(double heightDifference) {
        this.heightDifference = heightDifference;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

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
        public AvgDifficulty(int firstDiff){
            if(firstDiff < 0 || firstDiff > 5) throw new IllegalArgumentException("AvgDifficulty must be in the range [0,5]");
            total = firstDiff;
            nbr = 1;
        }

        /**
         * Create an Average difficulty for a track
         * @param total the summed durations
         * @param nbr the number of durations
         */
        public AvgDifficulty(int total, int nbr){
            if(total < 0) throw new IllegalArgumentException("Total difficulty must be positive");
            if(nbr <= 0) throw new IllegalArgumentException("Number of difficulties must be greater than zero");
            this.total = total;
            this.nbr = nbr;
        }

        /**
         * Add a new difficulty to the average
         * @param newDiff
         */
        public void add(int newDiff){
            if(newDiff < 0 || newDiff > 5) throw new IllegalArgumentException("AvgDifficulty must be in the range [0,5]");
            total += newDiff;
            nbr++;
        }

        public double getAverage(){
            return (double)total/nbr;
        }


        public int getTotal() { return total;}

        public int getNbr() { return nbr; }
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
        public AvgDuration(double firstDuration){
            if(firstDuration < 0) throw new IllegalArgumentException("Duration for a track must be positive");
            nbr = 1;
            total = firstDuration;
        }

        /**
         * Create an Average duration for a track
         * @param total the summed durations
         * @param nbr the number of durations
         */
        public AvgDuration(double total, int nbr){
            if(total < 0) throw new IllegalArgumentException("Total duration must be positive");
            if(nbr <= 0) throw new IllegalArgumentException("Number of durations must be greater than zero");
            this.total = total;
            this.nbr = nbr;
        }

        /**
         * Add a new duration to the average
         * @param newDuration
         */
        public void add(double newDuration){
            if(newDuration < 0) throw new IllegalArgumentException("Duration for a track must be positive");
            nbr++;
            total += newDuration;
        }

        public double getAverage(){
            return total/nbr;
        }

        public double getTotal() { return total; }

        public int getNbr() { return nbr; }
    }
}
