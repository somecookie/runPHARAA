package ch.epfl.sweng.runpharaa.tracks;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.runpharaa.utils.Required;

public class FilterProperties {

    //TODO Tweak it to find most stable parameters
    private static final int LENGTHCOEFF = 1;
    private static final int DIFFICULTYCOEFF = 1000;
    private static final int DURATIONCOEFF = 100;
    private static final int TYPECOEFF = 1000;


    private double lengthTot;
    private int nblength;
    private int difficultyTot;
    private int nbDiffiulties;
    private double durationTot;
    private int nbDurations;
    private Map<TrackType, Integer> types;

    public FilterProperties() {
        this.lengthTot = 0;
        this.nblength = 0;
        this.difficultyTot = 0;
        this.nbDiffiulties = 0;
        this.durationTot = 0;
        this.nbDurations = 0;
        this.types = new HashMap<>();
        for (TrackType trackType : TrackType.values()){
            this.types.put(trackType, 0);
        }
    }

    public void add(List<Track> tracks){
        for (Track track : tracks){
            add(track.getProperties().getLength(), track.getProperties().getAvgDifficulty(),
                    track.getProperties().getAvgDuration(), track.getProperties().getType());
        }
    }

    public void add(double length, double difficulty, double duration, Set<TrackType> t){
        Required.greaterOrEqualZero(length, "Length must be positive");
        Required.greaterOrEqualZero(difficulty, "Difficulty must be positive");
        Required.greaterOrEqualZero(duration, "Duration must be positive");
        Required.nonNull(t, "Types must be non mull");
        lengthTot += length;
        nblength++;
        difficultyTot += difficulty;
        nbDiffiulties++;
        durationTot += duration;
        nbDurations++;
        for (TrackType trackType : t){
            int prev = types.get(trackType);
            types.put(trackType, prev + 1);
        }
    }

    private double getPreferedLength(){
        return lengthTot / nblength;
    }

    private double getPreferedDifficulty(){
        return difficultyTot / nbDiffiulties;
    }

    private double getPreferedDuration(){
        return durationTot / nbDurations;
    }

    private TrackType getPreferedTrackType(){
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

    private Double computeCoeff(Track track){
        double lengthDiff = Math.abs(track.getProperties().getLength() - getPreferedLength());
        double difficultyDiff = Math.abs(track.getProperties().getAvgDifficulty() - getPreferedDifficulty());
        double durationDiff = Math.abs(track.getProperties().getAvgDuration() - getPreferedDuration());
        int typeDiff = 1;
        if(track.getProperties().getType().contains(getPreferedTrackType())){
            typeDiff = 0;
        }
        return (LENGTHCOEFF * lengthDiff) + (DIFFICULTYCOEFF * difficultyDiff) + (DURATIONCOEFF * durationDiff) + (TYPECOEFF * typeDiff);
    }
}
