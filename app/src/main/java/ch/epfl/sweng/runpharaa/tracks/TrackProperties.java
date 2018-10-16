package ch.epfl.sweng.runpharaa.tracks;

public class TrackProperties {

    private double length;
    private double heightDifference;
    private AvgDuration avgDur;
    private AvgDifficulty avgDiff;
    private int likes = 0;
    private int favorites = 0;
    private TrackType trackType;


    public TrackProperties(double length, double heightDiff, double time, int difficulty, TrackType trackType){

        if(length < 0) throw new IllegalArgumentException("The length of the track must be positive");

        this.length = length;
        this.heightDifference = heightDiff;
        avgDur = new AvgDuration(time);
        avgDiff = new AvgDifficulty(difficulty);
        this.trackType = trackType;
    }

    public double getAvgDifficulty(){
        return avgDiff.getAverage();
    }
    public void addNewDifficulty(int diff){
        avgDiff.add(diff);
    }

    public double getAvgDuration(){
        return avgDur.getAverage();
    }
    public void addNewDuration(double dur){
        avgDur.add(dur);
    }


    public void addLike(){likes++;}
    public int getLikes(){return likes;}

    public void addFavorite(){favorites++;}
    public int getFavorites(){return favorites;}

    public TrackType getType(){return trackType;}

    public double getLength() {
        return length;
    }

    public double getHeightDifference() {
        return heightDifference;
    }

    /**
     * Class that represents the average difficulty of a track in a range from 0 to 5
     */
    private class AvgDifficulty {
        private int total;
        private int nbr;

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
