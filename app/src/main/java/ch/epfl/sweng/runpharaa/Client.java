package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {

    public final static String TRACKS_PATH = "tracks";
    public final static String TRACK_IMAGE_PATH = "TrackImages";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDataBaseRef;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageRef;


    public List<Track> tracksNearMe = new ArrayList<>();
    public List<Track> favouritesTracks = new ArrayList<>();
    public List<Track> createdTracks = new ArrayList<>();


    public Client(Context context)
    {


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDataBaseRef = mFirebaseDatabase.getReference();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageRef = mFirebaseStorage.getReference();



        /*
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

        writeNewTrack(new Track(BitmapFactory.decodeResource(context.getResources(), R.drawable.centre_sportif), "Banane -> Centre Sportif", Arrays.asList(coord1, coord2), 350, 10, 3, 4));
        writeNewTrack(new Track(BitmapFactory.decodeResource(context.getResources(), R.drawable.innovation_park), "Innovation Parc -> BC", Arrays.asList(coord4, coord3), 300, 2, 1, 1));
        writeNewTrack(new Track(BitmapFactory.decodeResource(context.getResources(), R.drawable.rolex), "Rolex -> Swisstech",Arrays.asList(coord5, coord6), 850, 8, 4, 2));
        writeNewTrack(new Track(BitmapFactory.decodeResource(context.getResources(), R.drawable.rolex), "Sat -> INM", Arrays.asList(coord7, coord0), 450, 5, 6, 7));
        writeNewTrack(new Track(BitmapFactory.decodeResource(context.getResources(), R.drawable.ouchy), "Ouchy -> Gare", Arrays.asList(coord8, coord9), 1300, 20, 10, 12));
        writeNewTrack(new Track(BitmapFactory.decodeResource(context.getResources(), R.drawable.saint_francois), "SF -> Cath -> Flon", Arrays.asList(coord10, coord11, coord12), 0, 0, 0,0));
        */

        /*If there is other thing that Track object on the data base then use addChildEventListener on Tracks
        /*mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                initTracksList(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        /*
        mStorageRef.child("TrackImages/centre_sportif.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("Read image uri", uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Read image uri", "Fail to read uri");
            }
        });
        */

        mDataBaseRef.child(TRACKS_PATH).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                initTracks(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO make something of it
            }
        });
    }

    /**
     * Track a {@link Track} and add it to the database
     * @param track
     */
    public void writeNewTrack(final Track track) {
        //Generate a new key in the database
        final String key = mDataBaseRef.child(TRACKS_PATH).push().getKey();

        //Upload image
        Bitmap bitmap = track.getImageBitMap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mStorageRef.child(TRACK_IMAGE_PATH).child(key).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Storage", "Failed to upload image to storage :" + e.getMessage());
            }
        });
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mStorageRef.child(TRACK_IMAGE_PATH).child(key).getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Storage", "Failed to download image url :"+ e.getMessage());
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                track.setImageStorageUri(task.getResult().toString());
                                track.setTrackUid(key);
                                mDataBaseRef.child(TRACKS_PATH).child(key).setValue(track).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Database", "Failed to upload new track :" + e.getMessage());
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    public void updateTrack(Track track){
        //TODO check if it work
        mDataBaseRef.child(TRACKS_PATH).child(track.getTrackUid()).setValue(track);
    }

    private void initTracks(DataSnapshot dataSnapshot){
        for(DataSnapshot c : dataSnapshot.getChildren()){
            initTracksNearMe(c);
            initFavouritesTracks(c);
            initCreatedTracks(c);
        }
    }

    private void initTracksNearMe(DataSnapshot c){
        CustLatLng userLocation = new CustLatLng(User.FAKE_USER.getLocation().latitude, User.FAKE_USER.getLocation().longitude);
        int userPreferredRadius = User.FAKE_USER.getPreferredRadius();
        if(distance(c.child("startingPoint").getValue(CustLatLng.class), userLocation) <= userPreferredRadius){
            tracksNearMe.add(c.getValue(Track.class));
        }
    }

    private void initCreatedTracks(DataSnapshot c){
        if(User.FAKE_USER.getCreatedTracksKeys() != null){
            if(User.FAKE_USER.getCreatedTracksKeys().contains(c.getKey())){
                createdTracks.add(c.getValue(Track.class));
            }
        }
    }

    private void initFavouritesTracks(DataSnapshot c){
        if(User.FAKE_USER.getFavoritesTracksKeys() != null){
            if(User.FAKE_USER.getFavoritesTracksKeys().contains(c.getKey())){
                favouritesTracks.add(c.getValue(Track.class));
            }
        }
    }

    //TODO Put in CustLatLng class
    public double distance(CustLatLng startingPoint, CustLatLng userLocation){
        int R = 6378137; //Earth's mean radius in meter

        //angular differences in radians
        double dLat = Math.toRadians(userLocation.getLatitude() - startingPoint.getLatitude());
        double dLong = Math.toRadians(userLocation.getLongitude() - startingPoint.getLongitude());

        //this' and other's latitudes in radians
        double lat1 = Math.toRadians(startingPoint.getLatitude());
        double lat2 = Math.toRadians(userLocation.getLatitude());

        //compute some factor a
        double a1 = Math.sin(dLat/2)*Math.sin(dLat/2);
        double a2 = Math.cos(lat1)*Math.cos(lat2);
        double a3 = Math.sin(dLong/2)*Math.sin(dLong/2);

        double a = a1 + a2*a3;

        //compute some factor c
        double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R*c;
    }
}
