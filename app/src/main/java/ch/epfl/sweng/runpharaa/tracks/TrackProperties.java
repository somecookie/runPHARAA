package ch.epfl.sweng.runpharaa.tracks;

import com.google.firebase.database.Exclude;

import java.util.Set;

public class TrackProperties {

    private double length;
    private double heightDifference;
    private AvgDuration avgDur;
    private AvgDifficulty avgDiff;
    private int likes = 0;
    private int favorites = 0;
    @Exclude
    private Set<TrackType> trackType;

    //For Firebase
    public TrackProperties(){ }

    public TrackProperties(double length, double heightDiff, double time, int difficulty, Set<TrackType> trackType){

        if(length < 0) throw new IllegalArgumentException("The length of the track must be positive");

        this.length = length;
        this.heightDifference = heightDiff;
        avgDur = new AvgDuration(time);
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

    @Exclude //TODO think about how to put it in databse
    public double getAvgDuration(){
        return avgDur.getAverage();
    }
    public void addNewDuration(double dur){
        avgDur.add(dur);
    }


    public void addLike(){likes++;}
    public void removeLike(){likes--;}
    public int getLikes(){return likes;}

    public void addFavorite(){favorites++;}
    public void removeFavorite(){favorites--;}
    public int getFavorites(){return favorites;}

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

        public AvgDifficulty(){
            //For database
        }

        public AvgDifficulty(int firstDiff){
            if(firstDiff < 0 || firstDiff > 5) throw new IllegalArgumentException("AvgDifficulty must be in the range [0,5]");
            total = firstDiff;
            nbr = 1;
        }

        public void add(int newDiff){
            if(newDiff < 0 || newDiff > 5) throw new IllegalArgumentException("AvgDifficulty must be in the range [0,5]");
            total += newDiff;
            nbr++;
        }

        public double getAverage(){
            return (double)total/nbr;
        }
    }

    /**
     * Class that represents the average duration of a track in minutes
     */
    private class AvgDuration{
        private double total;
        private int nbr;

        public AvgDuration(double firstDuration){
            if(firstDuration < 0) throw new IllegalArgumentException("Duration for a track must be positive");
            nbr = 1;
            total = firstDuration;
        }

        public void add(double newDuration){
            if(newDuration < 0) throw new IllegalArgumentException("Duration for a track must be positive");
            nbr++;
            total += newDuration;
        }

        public double getAverage(){
            return total/nbr;
        }
    }
}
