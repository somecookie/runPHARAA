package ch.epfl.sweng.runpharaa.tracks;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.runpharaa.utils.Required;

public class FiltersProperties {

    //TODO Tweak it to find most stable parameters
    private final int LENGTHCOEFF = 1;
    private final int DIFFICULTYCOEFF = 1000;
    private final int DURATIONCOEFF = 100;
    private final int TYPECOEFF = 1000;


    private double lengthTot;
    private int nblength;
    private int difficultyTot;
    private int nbDiffiulties;
    private double durationTot;
    private int nbDurations;
    private Map<TrackType, Integer> types;

    public FiltersProperties() {
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

    public void add(double length, int difficulty, double duration, Set<TrackType> types){
        Required.greaterOrEqualZero(length, "Length must be positive");
        Required.greaterOrEqualZero(difficulty, "Difficulty must be positive");
        Required.greaterOrEqualZero(duration, "Duration must be positive");
        Required.nonNull(types, "Types must be non mull");
        lengthTot += length;
        nblength++;
        difficultyTot += difficulty;
        nbDiffiulties++;
        durationTot += duration;
        nbDurations++;
        for (TrackType trackType : types){
            int prev = this.types.get(trackType);
            this.types.put(trackType, prev++);
        }
    }

    private double getPreferedLength(){
        return lengthTot / nblength;
    }

    private double getPreferefDifficulty(){
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
        List<Pair<Track, Double>> coeffed = new ArrayList<>();
        for (Track track : tracks){
            coeffed.add(computeCoeff(track));
        }
        return findBest(coeffed);
    }

    private Pair<Track, Double> computeCoeff(Track track){
        double lengthDiff = Math.abs(track.getProperties().getLength() - getPreferedLength());
        double difficultyDiff = Math.abs(track.getProperties().getAvgDifficulty() - getPreferefDifficulty());
        double durationDiff = Math.abs(track.getProperties().getAvgDuration() - getPreferedDuration());
        int typeDiff = track.getProperties().getType().contains(getPreferedTrackType()) ? 0 : 1;
        double coeff = LENGTHCOEFF * lengthDiff + DIFFICULTYCOEFF * difficultyDiff + DURATIONCOEFF * durationDiff + TYPECOEFF * typeDiff;
        return new Pair<>(track, coeff);
    }

    private Track findBest(List<Pair<Track, Double>> coeffed){
        Track best = coeffed.get(0).first;
        double min = Double.MAX_VALUE;
        for (Pair<Track, Double> pair : coeffed){
            if(pair.second < min){
                min = pair.second;
                best = pair.first;
            }
        }
        return best;
    }

}
