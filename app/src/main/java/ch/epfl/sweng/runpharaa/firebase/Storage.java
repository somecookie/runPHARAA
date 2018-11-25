package ch.epfl.sweng.runpharaa.firebase;

import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class Storage {
    public static Boolean isTest = false;
    private static Boolean shouldFail = false;
    private static Boolean shouldNotComplete = false;
    private final static String key = "key";
    private final static String s_trackImage = "TrackImages";

    @Mock
    private FirebaseStorage firebaseStorageMock;

    @Mock
    private StorageReference referenceStorageMock;

    @Mock
    private StorageReference rsTrackImage;

    @Mock
    private StorageReference rsTrackImageKey;

    @Mock
    private UploadTask uploadTask;

    @Mock
    private UploadTask uploadTaskSuccessful;

    @Mock
    private Task<Uri> downloadUrlTask;

    @Mock
    private Task<Uri> downloadUrlTaskSuccessful;

    @Mock
    private Task<Uri> addTrackToStorageTask;

    public static FirebaseStorage getInstance(){
        return (isTest)? new Storage().instanciateMock() : FirebaseStorage.getInstance();
    }

    private FirebaseStorage instanciateMock(){
        if(isTest){
            MockitoAnnotations.initMocks(this);
            instanciateStorage();
            instanciateTrackImage();
            return firebaseStorageMock;
        } else {
            return FirebaseStorage.getInstance();
        }
    }

    private void instanciateStorage(){
        //First layer
        when(firebaseStorageMock.getReference()).thenReturn(referenceStorageMock);

        //Second layer
        when(referenceStorageMock.child(s_trackImage)).thenReturn(rsTrackImage);

    }

    private void instanciateTrackImage(){
        when(rsTrackImage.child(key)).thenReturn(rsTrackImageKey);
        when(rsTrackImageKey.putBytes(any(byte[].class))).thenReturn(uploadTask);
        when(uploadTask.addOnFailureListener(any(OnFailureListener.class))).thenAnswer(new Answer<Task<Void>>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                OnFailureListener l = (OnFailureListener) invocation.getArguments()[0];
                if(shouldFail){
                    l.onFailure(new IllegalStateException("Failed to upload image"));
                }
                return uploadTask;
            }
        });
        when(uploadTask.addOnCompleteListener(any(OnCompleteListener.class))).thenAnswer(new Answer<Task<Void>>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                OnCompleteListener l = (OnCompleteListener) invocation.getArguments()[0];
                if(!shouldFail){
                    l.onComplete(uploadTaskSuccessful);
                }
                return uploadTask;
            }
        });

        when(uploadTaskSuccessful.isSuccessful()).thenReturn(true);



        when(rsTrackImageKey.getDownloadUrl()).thenReturn(downloadUrlTask);
        when(downloadUrlTask.addOnFailureListener(any(OnFailureListener.class))).thenAnswer(new Answer<Task<Void>>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                OnFailureListener l = (OnFailureListener) invocation.getArguments()[0];
                if(shouldFail){
                    l.onFailure(new IllegalStateException("Failed to download URL"));
                }
                return downloadUrlTask;
            }
        });

        when(downloadUrlTask.addOnCompleteListener(any(OnCompleteListener.class))).thenAnswer(new Answer<Task>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                OnCompleteListener l = (OnCompleteListener) invocation.getArguments()[0];
                l.onComplete(downloadUrlTaskSuccessful);
                return downloadUrlTaskSuccessful;
            }
        });

        when(downloadUrlTaskSuccessful.isSuccessful()).thenReturn(true);
        when(downloadUrlTaskSuccessful.getResult()).thenReturn(Uri.parse(""));



        when(addTrackToStorageTask.isSuccessful()).thenReturn(true);
    }
}
