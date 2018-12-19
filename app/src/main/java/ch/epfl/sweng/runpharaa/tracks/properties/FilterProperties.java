package ch.epfl.sweng.runpharaa.tracks.properties;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.utils.Required;

public class FilterProperties {

    //To tweak to find most stable parameters
    private static final int LENGTHCOEFF = 1;
    private static final int DIFFICULTYCOEFF = 1000;
    private static final int DURATIONCOEFF = 100;
    private static final int TYPECOEFF = 1000;


    private double lengthTot;
    private int nbLength;
    private int difficultyTot;
    private int nbDifficulties;
    private double durationTot;
    private int nbDurations;
    private Map<TrackType, Integer> types;

    public FilterProperties() {
        this.lengthTot = 0;
        this.nbLength = 0;
        this.difficultyTot = 0;
        this.nbDifficulties = 0;
        this.durationTot = 0;
        this.nbDurations = 0;
        this.types = new HashMap<>();
        for (TrackType trackType : TrackType.values()){
            this.types.put(trackType, 0);
        }
    }

    /**
     * Add a list of {@link Track} to the filtered tracks
     * @param tracks the list of {@link Track}
     */
    public void add(List<Track> tracks){
        for (Track track : tracks){
            add(track.getProperties().getLength(), track.getProperties().getAvgDifficulty(),
                    track.getProperties().getAvgDuration(), track.getProperties().getType());
        }
    }

    /**
     * Add the specified properties of a {@link Track} to the filtered tracks
     * @param length the length of the track (Greater or equal than zero)
     * @param difficulty the difficultly of th track (Greater or equal than zero)
     * @param duration the duration of the track (Greater or equal than zero)
     * @param t the {@link TrackType} of the track (Non-null)
     */
    public void add(double length, double difficulty, double duration, Set<TrackType> t){
        Required.greaterOrEqualZero(length, "Length must be positive");
        Required.greaterOrEqualZero(difficulty, "Difficulty must be positive");
        Required.greaterOrEqualZero(duration, "Duration must be positive");
        Required.nonNull(t, "Types must be non mull");
        lengthTot += length;
        nbLength++;
        difficultyTot += difficulty;
        nbDifficulties++;
        durationTot += duration;
        nbDurations++;
        for (TrackType trackType : t){
            int prev = types.get(trackType);
            types.put(trackType, prev + 1);
        }
    }

    /**
     * @return the average length of the filtered tracks
     */
    private double getPreferredLength(){
        return lengthTot / nbLength;
    }

    /**
     * @return the average difficulty of the filtered tracks
     */
    private double getPreferredDifficulty(){
        return difficultyTot / nbDifficulties;
    }

    /**
     * @return the average duration of the filtered tracks
     */
    private double getPreferredDuration(){
        return durationTot / nbDurations;
    }

    /**
     * @return the most recurrent {@link TrackType} of the filtered tracks
     */
    private TrackType getPreferredTrackType(){
        TrackType trackType = TrackType.BEACH;
        int max = 0;
        for (TrackType t : TrackType.values()){
            if(types.get(t) > max){
                max = types.get(t);
                trackType = t;
            }
        }
        return trackType;
    }

    /**
     * Given a list of {@link Track} and the previously added tracks, choose the {@link Track} that
     * match the most the previously added ones. (In term of length, duration, difficulty and {@link TrackType})
     * @param tracks the list of tracks
     * @return the lucky {@link Track} from the given list
     */
    public Track chooseLuckyTrack(List<Track> tracks){
        Track best = tracks.get(0);
        double min = Double.MAX_VALUE;
        for (Track track : tracks){
            double coeff = computeCoeff(track);
            if(coeff < min){
                min = coeff;
                best = track;
            }
        }
        return best;
    }

    /**
     * Compute the coefficient of matching of a track with the virtual perfect track
     * @param track the {@link Track} on which to compute the coefficient on
     * @return the coefficient in a {@link Double}
     */
    private Double computeCoeff(Track track){
        double lengthDiff = Math.abs(track.getProperties().getLength() - getPreferredLength());
        double difficultyDiff = Math.abs(track.getProperties().getAvgDifficulty() - getPreferredDifficulty());
        double durationDiff = Math.abs(track.getProperties().getAvgDuration() - getPreferredDuration());
        int typeDiff = 1;
        if(track.getProperties().getType().contains(getPreferredTrackType())){
            typeDiff = 0;
        }
        return (LENGTHCOEFF * lengthDiff) + (DIFFICULTYCOEFF * difficultyDiff) + (DURATIONCOEFF * durationDiff) + (TYPECOEFF * typeDiff);
    }
}
