package ch.epfl.sweng.runpharaa;

import android.app.admin.DeviceAdminInfo;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Client {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;

    public ArrayList<Track> tracks;


    public Client()
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        //If there is other thing that Track object on the data base then use addChildEventListener on Tracks
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                initTracksList(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initTracksList(DataSnapshot dataSnapshot) {
        //iterate on all tracks in the database, get values and add tracks to the tracks array
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            String location  = ds.child("Track").getValue(Track.class).getLocation();
            LatLng[] latLngs = ds.child("Track").getValue(Track.class).getPath();
            tracks.add(new Track(location, latLngs));
        }
    }


}
