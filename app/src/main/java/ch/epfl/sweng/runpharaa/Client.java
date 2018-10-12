package ch.epfl.sweng.runpharaa;

import android.app.admin.DeviceAdminInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {

    public final static String TRACKS_PATH = "tracks";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;


    public ArrayList<Track> tracks;


    public Client()
    {


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        CustLatLng coord0 = new CustLatLng(46.518577, 6.563165); //inm
        CustLatLng coord1 = new CustLatLng(46.522735, 6.579772); //Banane
        CustLatLng coord2 = new CustLatLng(46.519380, 6.580669); //centre sportif
        CustLatLng coord3 = new CustLatLng(46.518475, 6.561960); //BC
        CustLatLng coord4 = new CustLatLng(46.517563, 6.562350); //Innovation parc
        CustLatLng coord5 = new CustLatLng(46.518447, 6.568238); //Rolex
        CustLatLng coord6 = new CustLatLng(46.523206, 6.564945); //SwissTech
        CustLatLng coord7 = new CustLatLng(46.520566, 6.567820); //Sat
        CustLatLng coord8 = new CustLatLng(46.506279, 6.626111); //Ouchy
        CustLatLng coord9 = new CustLatLng(46.517210, 6.630105); //Gare
        CustLatLng coord10 = new CustLatLng(46.519531, 6.633149);// Saint-Francois
        CustLatLng coord11 = new CustLatLng(46.522638, 6.634971); //Cathédrale
        CustLatLng coord12 = new CustLatLng(46.521412, 6.627383); //Flon

        writeNewTrack(new Track(R.drawable.centre_sportif, "Banane -> Centre Sportif", Arrays.asList(coord1, coord2), 350, 10, 3, 4));

        /*If there is other thing that Track object on the data base then use addChildEventListener on Tracks
        /*mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                initTracksList(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        mRef.child(TRACKS_PATH).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    Track track = s.getValue(Track.class);
                    Log.d("Database Read location", track.getLocation());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO make something of it
            }
        });
    }

    private void writeNewTrack(Track track) {
        String key = mRef.child(TRACKS_PATH).push().getKey();
        track.setTrackUid(key);
        mRef.child(TRACKS_PATH).child(key).setValue(track);
    }

    /*
    private void initTracksList(DataSnapshot dataSnapshot) {
        //iterate on all tracks in the database, get values and add tracks to the tracks array
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            String location  = ds.child("Track").getValue(Track.class).getLocation();
            LatLng[] latLngs = ds.child("Track").getValue(Track.class).getPath();
            tracks.add(new Track(location, latLngs));
        }
    }
    */

}
