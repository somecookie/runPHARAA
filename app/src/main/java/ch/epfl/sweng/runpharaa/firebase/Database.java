package ch.epfl.sweng.runpharaa.firebase;

import android.net.Uri;
import android.renderscript.Sampler;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.runpharaa.CustLatLng;
import ch.epfl.sweng.runpharaa.tracks.FirebaseTrackAdapter;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class Database {

    private static boolean shouldFail = false;
    private static boolean isCancelled = false;
    private static boolean userExists = false;


    private final static String s_tracks = "tracks";
    private final static String s_user = "users";
    private final static String s_favorite = "favoriteTracks";
    private final static String s_likes = "likedTracks";
    private final static String s_create = "createdTracks";
    private final static String s_key = "key";
    public final static String COMMENTS = "comments";


    private final static String keyWriteTrack = "key";

    private final static User fake_user = new User("Bob", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "1");

    //Tracks already in the fakeDB
    private final static String trackUID = "TrackID";
    private final static String trackName = "name";

    private FirebaseTrackAdapter t = new FirebaseTrackAdapter();

    //For all mocked objects
    //First Level
    @Mock
    private FirebaseDatabase firebaseDatabaseMock;

    @Mock
    private DatabaseReference databaseReferenceMock;


    //Second level
    @Mock
    private DatabaseReference drTracks;

    @Mock
    private DatabaseReference drUser;


    //Third level
    @Mock
    private DatabaseReference drTracksPush;

    @Mock
    private DatabaseReference drKey;

    @Mock
    private DatabaseReference drTracksUID;

    @Mock
    private DatabaseReference drTracksKey;

    @Mock
    private DatabaseReference drUserAnyChild;

    @Mock
    private DatabaseReference drUserAnyChildFavorites;

    @Mock
    private DatabaseReference drUserAnyChildLikes;

    @Mock
    private DatabaseReference drUserAnyChildCreate;

    @Mock
    private DatabaseReference drUserAnyChildName;

    @Mock
    private DatabaseReference drUserAnyChildPicture;


    @Mock
    private DataSnapshot snapInitUser;

    @Mock
    private DatabaseReference drUserAnyChildFavoritesChild;

    @Mock
    private DatabaseReference drUserAnyChildLikesChild;

    @Mock
    private DatabaseReference drUserAnyChildCreatesChild;

    @Mock
    private DatabaseReference drTracksUIDComment;

    @Mock
    private Task<Void> setValueTrack;

    @Mock
    private DataSnapshot snapOnDataChangeRead;

    @Mock
    private DataSnapshot snapOnDataChangeReadUser;

    @Mock
    private DataSnapshot snapInitFollow;

    @Mock
    private DataSnapshot snapInitCurUser;

    @Mock
    private DataSnapshot snapInitChildrenUser;

    @Mock
    private DataSnapshot snapInitFollowChildrens;

    @Mock
    private DataSnapshot snapOnDataChangeReadChildPath;

    @Mock
    private DataSnapshot snapOnDataChangedChildTrackUID;

    @Mock
    private DataSnapshot snapOnDataChangeReadChildPath0;

    @Mock
    private DatabaseError snapOnDataErrorRead;

    @Mock
    private DataSnapshot snapInitChildrenID;

    @Mock
    private DataSnapshot snapOnDataChangeUser;

    @Mock
    private DataSnapshot snapOnDataChangeUserChild;

    @Mock
    private DatabaseError snapOnDataErrorUser;

    @Mock
    private DataSnapshot snapInit;

    @Mock
    private DataSnapshot snapInitTrack;

    @Mock
    private DataSnapshot snapOnDataUserName;

    @Mock
    private DataSnapshot snapOnDataUserPicture;

    @Mock
    private DataSnapshot snapInitChildren;

    @Mock
    private FirebaseTrackAdapter track;

    @Mock
    private List<String> userFavoritesList;

    @Mock
    private List<String> userLikesList;

    @Mock
    private List<String> userCreatesList;

    @Mock
    private Task<Void> removeTask;

    @Mock
    private Task<Void> setTask;

    @Mock
    private Task<Void> addComment;

    @Mock
    private Task<Void> setValueTask;

    @Mock
    private Task<Void> setValueFavoriteTask;

    @Mock
    private Task<Void> setValueLikeTask;

    @Mock
    private Task<Void> userTask;

    @Mock
    private DatabaseReference drUserAnyChildFollow;


    private Database() {

    }

    public static FirebaseDatabase getInstance(){
        return (Config.isTest) ? new Database().instanciateMock() :  FirebaseDatabase.getInstance();
    }

    private FirebaseDatabase instanciateMock(){
        MockitoAnnotations.initMocks(this);
        createTrack();
        instanciateDB();
        instanciateDBRef();
        instanciatedrTracks();
        instanciatedrKeys();
        instanciatedrUsers();
        instanciateRead();
        instanciateSnapshots();
        return firebaseDatabaseMock;
    }

    private void instanciateDB() {
        when(firebaseDatabaseMock.getReference()).thenReturn(databaseReferenceMock);
    }

    private void instanciateDBRef() {
        when(databaseReferenceMock.child(s_tracks)).thenReturn(drTracks);
        when(databaseReferenceMock.child(s_key)).thenReturn(drKey);
        when(databaseReferenceMock.child(s_user)).thenReturn(drUser);
    }

    private void instanciateSnapshots() {
        when(snapInit.child(s_tracks)).thenReturn(snapInitTrack);

        when(snapOnDataChangeReadUser.getChildren()).thenReturn(Collections.singletonList(snapInitChildrenUser));
        when(snapOnDataChangeRead.getChildren()).thenReturn(Collections.singletonList(snapInitChildren));
        when(snapOnDataChangeRead.child(trackUID)).thenReturn(snapInitChildren);
        when(snapOnDataChangeRead.child("0")).thenReturn(snapInitChildren);

        when(snapOnDataChangeRead.child("1")).thenReturn(snapInitFollow);
        when(snapInitFollow.getChildren()).thenReturn(Collections.singletonList(snapInitFollowChildrens));
        when(snapInitFollowChildrens.getValue()).thenReturn(null);

        //changer le nom
        when(snapOnDataChangeReadUser.child("1")).thenReturn(snapInitCurUser);
        when(snapInitCurUser.child("followedUsers")).thenReturn(snapInitUser);
        when(snapInitChildrenUser.child("name")).thenReturn(snapInitUser);
        when(snapInitUser.getValue((String.class))).thenReturn("Bob");

        when(snapInitChildrenUser.child("uid")).thenReturn(snapInitChildrenID);
        when(snapInitChildrenID.getValue((String.class))).thenReturn("1");


        when(snapInitChildren.child(trackName)).thenReturn(snapOnDataChangeReadChildPath);
        when(snapInitChildren.getValue(FirebaseTrackAdapter.class)).thenReturn(t);
        when(snapInitChildren.child("path")).thenReturn(snapOnDataChangeReadChildPath);
        when(snapInitChildren.child("trackUid")).thenReturn(snapOnDataChangedChildTrackUID);
        when(snapInitChildren.getKey()).thenReturn("0");

        when(snapOnDataChangeReadChildPath.getValue((String.class))).thenReturn("Cours forest !");
        when(snapOnDataChangedChildTrackUID.getValue((String.class))).thenReturn(trackUID);
        when(snapOnDataChangeReadChildPath.child("0")).thenReturn(snapOnDataChangeReadChildPath0);
        when(snapOnDataChangeReadChildPath0.getValue(CustLatLng.class)).thenReturn(new CustLatLng(37.422, -122.084));

        when(snapOnDataChangeUser.child(any(String.class))).thenReturn(snapOnDataChangeUserChild);
        when(snapOnDataChangeUserChild.exists()).thenReturn(userExists);

        when(snapOnDataUserName.exists()).thenReturn(true);
        when(snapOnDataUserName.getValue((String.class))).thenReturn("Bob");

        when(snapOnDataUserPicture.exists()).thenReturn(true);
        when(snapOnDataUserPicture.getValue((String.class))).thenReturn("");


    }

    private void instanciatedrUsers() {
        when(drUser.child(any(String.class))).thenReturn(drUserAnyChild);

        //write new user
        doAnswer(new Answer<ValueEventListener>() {
            @Override
            public ValueEventListener answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener l = (ValueEventListener) invocation.getArguments()[0];
                if (isCancelled) {
                    l.onCancelled(snapOnDataErrorUser);
                } else {
                    l.onDataChange(snapOnDataChangeUser);
                }
                return l;
            }
        }).when(drUserAnyChild).addListenerForSingleValueEvent(any(ValueEventListener.class));


        //---

        when(drUserAnyChild.setValue(any(User.class))).thenReturn(userTask);

        when(userTask.addOnFailureListener(any(OnFailureListener.class))).thenAnswer(new Answer<Task<Void>>() {
            @Override
            public Task<Void> answer(InvocationOnMock invocation) throws Throwable {
                OnFailureListener l = (OnFailureListener) invocation.getArguments()[0];
                if(shouldFail){
                    l.onFailure(new IllegalStateException());
                }
                return userTask;
            }
        });

        respondToAddSuccessListener(userTask);

        //----

        when(drUserAnyChild.child(s_favorite)).thenReturn(drUserAnyChildFavorites);
        when(drUserAnyChild.child(s_likes)).thenReturn(drUserAnyChildLikes);
        when(drUserAnyChild.child(s_create)).thenReturn(drUserAnyChildCreate);
        when(drUserAnyChild.child("name")).thenReturn(drUserAnyChildName);
        when(drUserAnyChild.child("picture")).thenReturn(drUserAnyChildPicture);


        when(drUserAnyChild.child("followedUsers")).thenReturn(drUserAnyChildFollow);

        when(drUserAnyChildFavorites.setValue(userFavoritesList)).thenAnswer(new Answer<Task<Void>>() {
            @Override
            public Task<Void> answer(InvocationOnMock invocation) {
                fake_user.setFavoriteTracks(userFavoritesList);
                return null;
            }
        });

        when(drUserAnyChildLikes.setValue(userLikesList)).thenAnswer(new Answer<Task<Void>>() {
            @Override
            public Task<Void> answer(InvocationOnMock invocation) {
                fake_user.setLikedTracks(userLikesList);
                return null;
            }
        });

        when(drUserAnyChildLikes.setValue(userCreatesList)).thenAnswer(new Answer<Task<Void>>() {
            @Override
            public Task<Void> answer(InvocationOnMock invocation) {
                fake_user.setCreatedTracks(userCreatesList);
                return null;
            }
        });

        when(drUserAnyChildFavorites.child(any(String.class))).thenReturn(drUserAnyChildFavoritesChild);
        when(drUserAnyChildLikes.child(any(String.class))).thenReturn(drUserAnyChildLikesChild);
        when(drUserAnyChildCreate.child(any(String.class))).thenReturn(drUserAnyChildCreatesChild);


        when(drUserAnyChildCreatesChild.setValue(any(String.class))).thenReturn(setValueTask);

        when(drUserAnyChildFavorites.setValue(any(Object.class))).thenReturn(setValueFavoriteTask);
        when(drUserAnyChildLikes.setValue(any(Object.class))).thenReturn(setValueLikeTask);

        doAnswer(new Answer<ValueEventListener>() {
            @Override
            public ValueEventListener answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener l = (ValueEventListener) invocation.getArguments()[0];
                if (isCancelled) {
                    l.onCancelled(snapOnDataErrorRead);
                } else {
                    fake_user.addToFavorites("0");
                }
                return l;
            }
        }).when(drUserAnyChildFavoritesChild).addListenerForSingleValueEvent(any(ValueEventListener.class));

        doAnswer(new Answer<ValueEventListener>() {
            @Override
            public ValueEventListener answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener l = (ValueEventListener) invocation.getArguments()[0];
                if (isCancelled) {
                    l.onCancelled(snapOnDataErrorRead);
                } else {
                    fake_user.addToCreatedTracks("0");
                }
                return l;
            }
        }).when(drUserAnyChildCreatesChild).addListenerForSingleValueEvent(any(ValueEventListener.class));

        doAnswer(new Answer<ValueEventListener>() {
            @Override
            public ValueEventListener answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener l = (ValueEventListener) invocation.getArguments()[0];
                if (isCancelled) {
                    l.onCancelled(snapOnDataErrorRead);
                } else {
                    fake_user.like("0");
                }
                return l;
            }
        }).when(drUserAnyChildLikesChild).addListenerForSingleValueEvent(any(ValueEventListener.class));

        doAnswer(new Answer<ValueEventListener>() {
            @Override
            public ValueEventListener answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener l = (ValueEventListener) invocation.getArguments()[0];
                if (isCancelled) {
                    l.onCancelled(snapOnDataErrorRead);
                } else {
                    l.onDataChange(snapOnDataUserName);
                }
                return l;
            }
        }).when(drUserAnyChildName).addListenerForSingleValueEvent(any(ValueEventListener.class));

        doAnswer(new Answer<ValueEventListener>() {
            @Override
            public ValueEventListener answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener l = (ValueEventListener) invocation.getArguments()[0];
                if (isCancelled) {
                    l.onCancelled(snapOnDataErrorRead);
                } else {
                    l.onDataChange(snapOnDataUserPicture);
                }
                return l;
            }
        }).when(drUserAnyChildPicture).addListenerForSingleValueEvent(any(ValueEventListener.class));

        when(drUserAnyChildFavoritesChild.removeValue()).thenAnswer(new Answer<Task<Void>>() {
            @Override
            public Task<Void> answer(InvocationOnMock invocation) throws Throwable {
                int fav = t.getFavorites();
                if(fav >= 1){
                    //t.setFavorites(fav - 1);
                }
                return removeTask;
            }
        });

        when(drUserAnyChildLikesChild.removeValue())
                .thenAnswer(new Answer<Task<Void>>() {
                    @Override
                    public Task<Void> answer(InvocationOnMock invocation) throws Throwable {
                        int likes = t.getLikes();
                        if(likes >= 1){
                            //t.getProperties().setLikes(likes - 1);
                        }
                        return removeTask;
                    }
                });

        when(drUserAnyChildLikes.setValue(any(List.class))).thenReturn(setValueTask);
        when(drUserAnyChildFavorites.setValue(any(List.class))).thenReturn(setValueTask);
        when(drUserAnyChildCreate.setValue(any(List.class))).thenReturn(setValueTask);

        when(drUserAnyChildCreatesChild.setValue(any(String.class))).thenReturn(setValueTask);
        when(drUserAnyChildLikesChild.setValue(any(String.class))).thenReturn(setValueTask);
        when(drUserAnyChildFavoritesChild.setValue(any(String.class))).thenReturn(setValueTask);
        when(setValueTask.addOnFailureListener(any(OnFailureListener.class))).thenAnswer(new Answer<Task<Void>>() {
            @Override
            public Task<Void> answer(InvocationOnMock invocation) throws Throwable {
                OnFailureListener l = (OnFailureListener) invocation.getArguments()[0];
                if(shouldFail){
                    l.onFailure(new IllegalStateException("Cant set value"));
                }
                return setValueTrack;
            }
        });


        when(setValueFavoriteTask.addOnFailureListener(any(OnFailureListener.class))).thenAnswer(new Answer<Task<Void>>() {
            @Override
            public Task<Void> answer(InvocationOnMock invocation) throws Throwable {
                OnFailureListener l = (OnFailureListener) invocation.getArguments()[0];
                if(shouldFail){
                    l.onFailure(new IllegalStateException("Cant set value"));
                }
                return setValueFavoriteTask;
            }
        });

        when(setValueLikeTask.addOnFailureListener(any(OnFailureListener.class))).thenAnswer(new Answer<Task<Void>>() {
            @Override
            public Task<Void> answer(InvocationOnMock invocation) throws Throwable {
                OnFailureListener l = (OnFailureListener) invocation.getArguments()[0];
                if(shouldFail){
                    l.onFailure(new IllegalStateException("Cant set value"));
                }
                return setValueLikeTask;
            }
        });


        //when(drUserAnyChildLikeChild.removeValue())

    }

    private void instanciatedrTracks() {
        when(drTracks.push()).thenReturn(drTracksPush);
        when(drTracksPush.getKey()).thenReturn(keyWriteTrack);

        when(drTracks.child(trackUID)).thenReturn(drTracksUID);
        when(drTracksUID.setValue(track)).then(new Answer<Task<Void>>() {
            @Override
            public Task<Void> answer(InvocationOnMock invocation) {
                t = track;
                return null;
            }
        });

        when(drTracksUID.child(COMMENTS)).thenReturn(drTracksUIDComment);
        when(drTracksUIDComment.setValue(any(List.class))).thenReturn(addComment);

        respondToAddFailureListener(addComment);

        when(drTracks.child(keyWriteTrack)).thenReturn(drTracksKey);
        when(drTracksKey.setValue(any(FirebaseTrackAdapter.class))).thenReturn(setTask);

        respondToAddFailureListener(setTask);

        respondToAddSuccessListener(setTask);

    }

    private void respondToAddFailureListener(Task task) {
        when(task.addOnFailureListener(any(OnFailureListener.class))).thenAnswer((Answer<Task<Void>>) invocation -> {
            OnFailureListener l = (OnFailureListener) invocation.getArguments()[0];
            if(shouldFail){
                l.onFailure(new IllegalStateException());
            }
            return task;
        });
    }

    private void respondToAddSuccessListener(Task task) {
        when(task.addOnSuccessListener(any(OnSuccessListener.class))).thenAnswer((Answer<Task<Void>>) invocation -> {
            OnSuccessListener<Void> l = (OnSuccessListener<Void>) invocation.getArguments()[0];
            if(!shouldFail){
                l.onSuccess(null);
            }
            return task;
        });
    }

    private void instanciatedrKeys() {
        when(drKey.setValue(track)).thenReturn(setValueTrack);
        when(setValueTrack.addOnFailureListener(any(OnFailureListener.class))).thenAnswer(new Answer<Task<Void>>() {
            @Override
            public Task<Void> answer(InvocationOnMock invocation) throws Throwable {
                OnFailureListener l = (OnFailureListener) invocation.getArguments()[0];
                if (shouldFail) {
                    l.onFailure(new IllegalStateException("Could not add track to DB"));
                }
                return setValueTrack;
            }
        });
    }

    private void instanciateRead() {
        //Read tracks from drTracks
        doAnswer((Answer<ValueEventListener>) invocation -> {
            ValueEventListener l = (ValueEventListener) invocation.getArguments()[0];
            if (isCancelled) {
                l.onCancelled(snapOnDataErrorRead);
            } else {
                l.onDataChange(snapOnDataChangeRead);
            }
            return l;
        }).when(drTracks).addListenerForSingleValueEvent(any(ValueEventListener.class));

        doAnswer((Answer<ValueEventListener>) invocation -> {
            ValueEventListener l = (ValueEventListener) invocation.getArguments()[0];
            if (isCancelled) {
                l.onCancelled(snapOnDataErrorRead);
            } else {
                l.onDataChange(snapOnDataChangeRead);
            }
            return l;
        }).when(drTracks).addValueEventListener(any(ValueEventListener.class));

        //Read tracks from drKey
        doAnswer((Answer<ValueEventListener>) invocation -> {
            ValueEventListener l = (ValueEventListener) invocation.getArguments()[0];
            if (isCancelled) {
                l.onCancelled(snapOnDataErrorRead);
            } else {
                l.onDataChange(snapOnDataChangeRead);
            }
            return l;
        }).when(drKey).addListenerForSingleValueEvent(any(ValueEventListener.class));


        doAnswer(snapOnDataChangedAnswer).when(drUser).addListenerForSingleValueEvent(any(ValueEventListener.class));


        doAnswer(snapOnDataChangedAnswer).when(drUserAnyChildFollow).addListenerForSingleValueEvent(any(ValueEventListener.class));

    }

    private Answer snapOnDataChangedAnswer = invocation -> {
        ValueEventListener l = (ValueEventListener) invocation.getArguments()[0];
        if(isCancelled){
            l.onCancelled(snapOnDataErrorRead);
        } else {
            l.onDataChange(snapOnDataChangeReadUser);
        }
        return null;
    };

    private void createTrack() {
        List<String> types = new ArrayList<>();
        types.add(TrackType.FOREST.toString());
        CustLatLng coord0 = new CustLatLng(37.422, -122.084);
        CustLatLng coord1 = new CustLatLng(37.425, -122.082);
        int length = 100;
        int heigthDiff = 10;
        FirebaseTrackAdapter track = new FirebaseTrackAdapter("Cours forest !", trackUID, "BobUID", "Bob", Arrays.asList(coord0, coord1), "imageUri",
                types, length, heigthDiff, 1, 1, 1, 1, 0, 0, new ArrayList<>());

        t = track;

    }


    public static void setShouldFail(boolean shouldFail) {
        Database.shouldFail = shouldFail;
    }

    public static void setIsCancelled(boolean isCancelled) {
        Database.isCancelled = isCancelled;
    }

    public static void setUserExists(boolean userExists) {
        Database.userExists = userExists;
    }

    public static User getUser(){
        return fake_user;
    }
}
